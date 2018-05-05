package com.bizvisionsoft.bruicommons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.bizvisionsoft.bruicommons.model.Site;
import com.google.gson.GsonBuilder;

public class ModelLoader implements BundleActivator {

	public static boolean reloadSiteForSession;
	public static Site site;
	public static String sitePath;
	public static Bundle bundle;

	public void start(BundleContext context) throws Exception {
		bundle = context.getBundle();
		sitePath = context.getProperty("com.bizvisionsoft.bruiengine.site");
		reloadSiteForSession = "true".equals(context.getProperty("com.bizvisionsoft.bruiengine.reloadSiteForSession"));
		loadSite();
	}

	public static void loadSite() throws FileNotFoundException {
		FileReader reader = new FileReader(new File(ModelLoader.sitePath));
		site = new GsonBuilder().create().fromJson(reader, Site.class);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop(BundleContext context) throws Exception {
	}

}
