package com.bizvisionsoft.bruiengine;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.md.service.Listener;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class BruiEventEngine extends BruiEngine {

	private Assembly assembly;

	public BruiEventEngine(Class<?> clazz) {
		super(clazz);
	}

	public BruiEventEngine(Object serivce) {
		super(serivce);
	}

	public BruiEventEngine(Class<?> clazz, Object serivce) {
		super(clazz, serivce);
	}

	public static BruiEventEngine create(Assembly asembly, IServiceWithId... services) {
		return (BruiEventEngine) Optional.ofNullable(load(asembly)).map(m -> m.newInstance().init(services))
				.orElse(null);
	}

	private static BruiEventEngine load(Assembly assembly) {
		String bundleId = assembly.getEventHandlerBundleId();
		String className = assembly.getEventHandlerClassName();

		if (bundleId != null && !bundleId.isEmpty() && className != null && !className.isEmpty()) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiEventEngine(bundle.loadClass(className)).setAssembly(assembly);
			} catch (Exception e) {
			}
		}
		return null;
	}

	private BruiEventEngine setAssembly(Assembly assembly) {
		this.assembly = assembly;
		return this;
	}

	public void attachListener(BiConsumer<String, Method> con) {
		Arrays.asList(clazz.getDeclaredMethods()).stream().forEach(m -> {
			Listener anno = m.getAnnotation(Listener.class);
			if (anno != null) {
				String[] values = anno.value();
				for (int i = 0; i < values.length; i++) {
					String listenerName = null;
					String[] loc = values[i].split("/");
					if (loc.length == 1) {
						listenerName = loc[0].trim();
					} else if (loc.length > 1 && assembly.getName().equals(loc[0].trim())) {
						listenerName = loc[1].trim();
					}
					if (listenerName != null) {
						con.accept(listenerName, m);
					}
				}

			}
		});
	}


}
