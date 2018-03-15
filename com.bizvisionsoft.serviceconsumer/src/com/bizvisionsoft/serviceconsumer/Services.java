package com.bizvisionsoft.serviceconsumer;

import java.util.HashMap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bizvisionsoft.service.FileService;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.UserService;

public class Services implements BundleActivator {

	private static BundleContext bundleContext;

	private static HashMap<Class<?>, ServiceReference<?>> registry;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Services.bundleContext = bundleContext;
		registry = new HashMap<Class<?>, ServiceReference<?>>();
		// ×¢²á·þÎñ
		register(bundleContext, FileService.class);
		register(bundleContext, UserService.class);
		register(bundleContext, OrganizationService.class);

	}

	private void register(BundleContext bundleContext, Class<?> type) {
		ServiceReference<?> reference = bundleContext.getServiceReference(type);
		registry.put(type, reference);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		registry.values().forEach(reference -> bundleContext.ungetService(reference));
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) {
		return (T) bundleContext.getService(registry.get(clazz));
	}

	public static Object get(String classname) {
		return registry.keySet().stream().filter(c -> c.getName().equals(classname)).findFirst().map(c -> get(c))
				.orElseThrow(NoClassDefFoundError::new);
	}

	public static Object[] getService(String classname) {
		return registry.keySet().stream().filter(c -> c.getName().equals(classname)).findFirst()
				.map(c -> new Object[] { c, get(c) }).orElseThrow(NoClassDefFoundError::new);
	}

}
