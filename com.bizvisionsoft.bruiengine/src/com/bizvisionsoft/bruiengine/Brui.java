package com.bizvisionsoft.bruiengine;

import java.util.Hashtable;

import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.bizvisionsoft.bruiengine.session.SessionManager;

public class Brui implements BundleActivator {

	private ServiceRegistration<?> registration;
	public static SessionManager sessionManager;
	public static Bundle bundle;

	public void start(BundleContext context) throws Exception {
		bundle = context.getBundle();
		sessionManager = new SessionManager().start();
		Hashtable<String, String> properties = new Hashtable<String, String>();
		registration = context.registerService(ApplicationConfiguration.class.getName(),
				new BruiApplicationConfiguration(), properties);
	}


	public void stop(BundleContext context) throws Exception {
		sessionManager.stop();
		registration.unregister();
	}

}
