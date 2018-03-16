package com.bizvisionsoft.bruiengine;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class BruiQueryEngine extends BruiEngine {

	public static BruiQueryEngine create(String bundleId, String className, IServiceWithId... services) {
		return (BruiQueryEngine) load(bundleId, className).newInstance().init(services);
	}

	private BruiQueryEngine(Class<?> clazz) {
		super(clazz);
	}

	private static BruiQueryEngine load(String bundleId, String className) {
		if (bundleId == null || bundleId.isEmpty())
			throw new RuntimeException("插件Id为空");
		Bundle bundle = Platform.getBundle(bundleId);

		if (bundle == null)
			throw new RuntimeException("无法获得插件" + bundleId);
		try {
			return new BruiQueryEngine(bundle.loadClass(className));
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
	}

}
