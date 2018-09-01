package com.bizvisionsoft.sqldb;

import java.sql.Connection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private ConnectionManager connectionManager;

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
		Activator.context = bundleContext;
		initConnection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		destoryConnection();
		Activator.context = null;
	}

	private void initConnection() {
		connectionManager = ConnectionManager.getInstance();
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
