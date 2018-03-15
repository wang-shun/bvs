package com.bizvisionsoft.bruiengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Hashtable;

import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.bizvisionsoft.bruicommons.model.Site;
import com.bizvisionsoft.bruiengine.session.SessionManager;
import com.google.gson.GsonBuilder;

public class Brui implements BundleActivator {

	private ServiceRegistration<?> registration;
	public static boolean reloadSiteForSession;
	public static SessionManager sessionManager;
	public static Site site;
	public static String sitePath;
	public static Bundle bundle;

	public void start(BundleContext context) throws Exception {
		bundle = context.getBundle();
		sitePath = context.getProperty("com.bizvisionsoft.bruiengine.site");
		reloadSiteForSession = "true".equals(context.getProperty("com.bizvisionsoft.bruiengine.reloadSiteForSession"));
		loadSite();
		sessionManager = new SessionManager().start();
		Hashtable<String, String> properties = new Hashtable<String, String>();
		registration = context.registerService(ApplicationConfiguration.class.getName(),
				new BruiApplicationConfiguration(), properties);
	}

	static void loadSite() throws FileNotFoundException {
		site = new GsonBuilder().create().fromJson(new FileReader(new File(Brui.sitePath)), Site.class);
	}

	public void stop(BundleContext context) throws Exception {
		sessionManager.stop();
		registration.unregister();
	}

}
