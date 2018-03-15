package com.bizvisionsoft.mongocodex;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	// private static final String PLUGINID = "com.bizvisionsoft.mongocodex";
	// private static BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
		// Activator.context = context;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static void logInfo(String message) {
		// getLog().log(new Status(Status.INFO, PLUGINID, message));
	}

	public static void logWarning(String message) {
		// getLog().log(new Status(Status.WARNING, PLUGINID, message));
	}

	public static void logError(String message) {
		// getLog().log(new Status(Status.ERROR, PLUGINID, message));
	}

	public static void logError(String message, Throwable throwable) {
		// getLog().log(new Status(Status.ERROR, PLUGINID, message,throwable));
	}

}
