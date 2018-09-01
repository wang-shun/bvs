package com.bizvisionsoft.sqldb;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ������DBConnectionManager֧�ֶ�һ�������������ļ���������ݿ�����
 * �صķ���.�ͻ�������Ե���getInstance()�������ʱ����Ψһʵ��.
 */
public class ConnectionManager {
	static private ConnectionManager instance; // Ψһʵ��
	static private int clients;

	private CopyOnWriteArrayList<Driver> drivers = new CopyOnWriteArrayList<Driver>();
	private Map<String, DBConnectionPool> pools = new ConcurrentHashMap<String, DBConnectionPool>();

	/**
	 * ����Ψһʵ��.����ǵ�һ�ε��ô˷���,�򴴽�ʵ��
	 * 
	 * @param dataSourcePath
	 * 
	 * 
	 * @return DBConnectionManager Ψһʵ��
	 */
	static synchronized public ConnectionManager getInstance(String dataSourcePath) {
		if (instance == null) {
			instance = new ConnectionManager(dataSourcePath);
		}
		clients++;
		return instance;
	}

	/**
	 * ��������˽���Է�ֹ�������󴴽�����ʵ��
	 * 
	 * @param dataSourcePath
	 */
	private ConnectionManager(String dataSourcePath) {
		init(dataSourcePath);
	}

	/**
	 * �����Ӷ��󷵻ظ�������ָ�������ӳ�
	 * 
	 * @param name
	 *            �������ļ��ж�������ӳ�����
	 * @param con
	 *            ���Ӷ���
	 */
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	/**
	 * ���һ�����õ�(���е�)����.���û�п�������,������������С����������� ����,�򴴽�������������
	 * 
	 * @param name
	 *            �������ļ��ж�������ӳ�����
	 * @return Connection �������ӻ�null
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			Connection conn = pool.getConnection();
			if (conn == null) {
				conn = pool.getConnection(5000);
			}
			return conn;
		}
		return null;
	}

	/**
	 * ���һ����������.��û�п�������,������������С���������������, �򴴽�������������.����,��ָ����ʱ���ڵȴ������߳��ͷ�����.
	 * 
	 * @param name
	 *            ���ӳ�����
	 * @param time
	 *            �Ժ���Ƶĵȴ�ʱ��
	 * @return Connection �������ӻ�null
	 */
	public Connection getConnection(String name, long time) {
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	public int getClient() {
		return clients;
	}

	/**
	 * �ر���������,�������������ע��
	 */
	public synchronized void release() {
		// �ȴ�ֱ�����һ���ͻ��������
		if (--clients != 0) {
			return;
		}

		Collection<DBConnectionPool> allPools = pools.values();
		Iterator<DBConnectionPool> iter = allPools.iterator();

		while (iter.hasNext()) {
			iter.next().release();
		}

		Iterator<Driver> iter2 = drivers.iterator();
		while (iter2.hasNext()) {
			Driver driver = iter2.next();
			try {
				DriverManager.deregisterDriver(driver);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����ָ�����Դ������ӳ�ʵ��.
	 * 
	 * @param props
	 *            ���ӳ�����
	 */
	private void createPools(Properties props) {
		Enumeration<?> propNames = props.propertyNames();
		while (propNames.hasMoreElements()) {
			String name = (String) propNames.nextElement();
			if (name.endsWith(".url")) {
				String poolName = name.substring(0, name.lastIndexOf("."));
				String url = props.getProperty(poolName + ".url");
				if (url == null) {
					System.err.println("û��Ϊ���ӳ�" + poolName + "ָ��URL");
					continue;
				}
				String user = props.getProperty(poolName + ".user");
				String password = props.getProperty(poolName + ".password");
				String maxconn = props.getProperty(poolName + ".maxconn", "0");

				int max;
				try {
					max = Integer.valueOf(maxconn).intValue();
				} catch (NumberFormatException e) {
					System.err.println("ERROR SETTING OF: MAX CONNECTION COUNT" + maxconn + " .POOL NAME: " + poolName);
					max = 10;
				}
				DBConnectionPool pool = new DBConnectionPool(poolName, url, user, password, max);
				pools.put(poolName, pool);
				System.out.println("CONNECTION POOL:" + poolName + " INIT...OK");
			}
		}
	}

	public Connection createConnection(String poolName) {
		DBConnectionPool dbConnectionPool = pools.get(poolName);
		if (dbConnectionPool == null) {
			return null;
		} else {
			return dbConnectionPool.newConnection();
		}
	}

	/**
	 * ��ȡ������ɳ�ʼ��
	 * 
	 * @param dataSourcePath
	 */
	private void init(String dataSourcePath) {
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(dataSourcePath));
			Properties dbProps = new Properties();
			dbProps.load(is);
			loadDrivers(dbProps);
			createPools(dbProps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * װ�غ�ע������JDBC��������
	 * 
	 * @param props
	 *            ����
	 */
	private void loadDrivers(Properties props) {
		String driverClasses = props.getProperty("drivers");
		StringTokenizer st = new StringTokenizer(driverClasses);
		while (st.hasMoreElements()) {
			String driverClassName = st.nextToken().trim();
			try {
				Driver driver = (Driver) Class.forName(driverClassName).newInstance();
				DriverManager.registerDriver(driver);
				drivers.add(driver);
				// System.out.println(" �ɹ�ע��JDBC��������" + driverClassName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// /**
	// * ���ı���Ϣд����־�ļ�
	// */
	// private void log(String msg) {
	// log.println(new Date() + ": " + msg);
	// }
	//
	// /**
	// * ���ı���Ϣ���쳣д����־�ļ�
	// */
	// private void log(Throwable e, String msg) {
	// log.println(new Date() + ": " + msg);
	// e.printStackTrace(log);
	// }

	/**
	 * ���ڲ��ඨ����һ�����ӳ�.���ܹ�����Ҫ�󴴽�������,ֱ��Ԥ������ ��������Ϊֹ.�ڷ������Ӹ��ͻ�����֮ǰ,���ܹ���֤���ӵ���Ч��.
	 */
	class DBConnectionPool {
		private int checkedOut;
		private CopyOnWriteArrayList<Connection> freeConnections = new CopyOnWriteArrayList<Connection>();
		private int maxConn;
		private String name;
		private String password;
		private String URL;
		private String user;

		/**
		 * �����µ����ӳ�
		 * 
		 * @param name
		 *            ���ӳ�����
		 * @param URL
		 *            ���ݿ��JDBC URL
		 * @param user
		 *            ���ݿ��ʺ�,�� null
		 * @param password
		 *            ����,�� null
		 * @param maxConn
		 *            �����ӳ������������������
		 */
		public DBConnectionPool(String name, String URL, String user, String password, int maxConn) {
			this.name = name;
			this.URL = URL;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;
		}

		/**
		 * ������ʹ�õ����ӷ��ظ����ӳ�
		 * 
		 * @param con
		 *            �ͻ������ͷŵ�����
		 */
		public synchronized void freeConnection(Connection con) {
			// ��ָ�����Ӽ��뵽����ĩβ
			if (con == null)
				return;
			freeConnections.add(con);
			checkedOut--;
			notifyAll();
		}

		/**
		 * �����ӳػ��һ����������.��û�п��е������ҵ�ǰ������С��������� ������,�򴴽�������.��ԭ���Ǽ�Ϊ���õ����Ӳ�����Ч,�������ɾ��֮,
		 * Ȼ��ݹ�����Լ��Գ����µĿ�������.
		 */
		@SuppressWarnings("resource")
		public synchronized Connection getConnection() {
			Connection con = null;
			if (freeConnections.size() > 0) {
				// ��ȡ�����е�һ����������
				con = freeConnections.get(0);
				freeConnections.remove(0);
				try {
					if (con.isClosed()) {
						// System.out.println("�����ӳ�" + name + "ɾ��һ����Ч����");
						// �ݹ�����Լ�,�����ٴλ�ȡ��������
						con = getConnection();
					}
				} catch (SQLException e) {
					System.out.println("�����ӳ�" + name + "ɾ��һ����Ч����");
					// �ݹ�����Լ�,�����ٴλ�ȡ��������
					con = getConnection();
				}
			} else if (maxConn == 0 || checkedOut < maxConn) {
				con = newConnection();
			}
			if (con != null) {
				checkedOut++;
			}
			return con;
		}

		/**
		 * �����ӳػ�ȡ��������.����ָ���ͻ������ܹ��ȴ����ʱ�� �μ�ǰһ��getConnection()����.
		 * 
		 * @param timeout
		 *            �Ժ���Ƶĵȴ�ʱ������
		 */
		public synchronized Connection getConnection(long timeout) {
			long startTime = new Date().getTime();
			Connection con;
			while ((con = getConnection()) == null) {
				try {
					wait(timeout);
				} catch (InterruptedException e) {
				}
				if ((new Date().getTime() - startTime) >= timeout) {
					// wait()���ص�ԭ���ǳ�ʱ
					return null;
				}
			}
			return con;
		}

		/**
		 * �ر���������
		 */
		public synchronized void release() {
			Iterator<Connection> allConnections = freeConnections.iterator();
			while (allConnections.hasNext()) {
				Connection con = allConnections.next();
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			freeConnections.clear();
		}

		/**
		 * �����µ�����
		 */
		public Connection newConnection() {
			Connection con = null;
			try {
				if (user == null) {
					con = DriverManager.getConnection(URL);
				} else {
					con = DriverManager.getConnection(URL, user, password);
				}
				// System.out.println("���ӳ�" + name + "����һ���µ�����");
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			return con;
		}
	}
}