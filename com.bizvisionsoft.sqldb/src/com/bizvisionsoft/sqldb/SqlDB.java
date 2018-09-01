package com.bizvisionsoft.sqldb;

import java.sql.Connection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SqlDB implements BundleActivator {

	private static BundleContext context;

	private ConnectionManager connectionManager;

	public static SqlDB s;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		SqlDB.context = bundleContext;
		initConnection();
		s = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		s = null;
		destoryConnection();
		SqlDB.context = null;
	}

	private void initConnection() {
		String dataSourcePath = context.getProperty("com.bizvisionsoft.sqldb.datasource");
		if(dataSourcePath==null||dataSourcePath.isEmpty()) {
			throw new IllegalArgumentException("缺少SqlDB数据源定义。启动参数:com.bizvisionsoft.sqldb.datasource");
		}
		connectionManager = ConnectionManager.getInstance(dataSourcePath);
	}

	private void destoryConnection() {
		connectionManager.release();		
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	public Connection getConnection(String dataSourceName) {
		return connectionManager.getConnection(dataSourceName);
	}

	public Connection createConnection(String dataSourceName) {
		return connectionManager.createConnection(dataSourceName);
	}

	public void freeConnection(String poolName, Connection connection) {
		connectionManager.freeConnection(poolName, connection);
	}
	

}
