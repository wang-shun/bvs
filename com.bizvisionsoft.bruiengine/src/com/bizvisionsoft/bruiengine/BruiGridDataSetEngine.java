package com.bizvisionsoft.bruiengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.service.annotations.DataSet;
import com.bizvisionsoft.service.annotations.ServiceParam;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class BruiGridDataSetEngine extends BruiEngine {

	private Assembly assembly;

	public BruiGridDataSetEngine(Class<?> clazz) {
		super(clazz);
	}

	public BruiGridDataSetEngine(Object serivce) {
		super(serivce);
	}

	public BruiGridDataSetEngine(Class<?> clazz, Object serivce) {
		super(clazz, serivce);
	}

	public static BruiGridDataSetEngine create(Assembly grid, IServiceWithId... services) {
		return (BruiGridDataSetEngine) load(grid)// load
				.newInstance().init(services);
	}

	private static BruiGridDataSetEngine load(Assembly grid) {
		String bundleId = grid.getGridDataSetBundleId();
		String className = grid.getGridDataSetClassName();

		if (bundleId != null && !bundleId.isEmpty() && className != null && !className.isEmpty()) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiGridDataSetEngine(bundle.loadClass(className)).setAssembly(grid);
			} catch (Exception e) {
				throw new RuntimeException(e.getCause());
			}
		}

		String serivceName = grid.getGridDataSetService();
		if (serivceName != null) {
			Object[] service = Services.getService(serivceName);
			if (service != null) {
				return new BruiGridDataSetEngine((Class<?>) service[0], service[1]).setAssembly(grid);
			}
		}
		throw new RuntimeException();
	}

	private BruiGridDataSetEngine setAssembly(Assembly assembly) {
		this.assembly = assembly;
		return this;
	}

	public Object query(Integer skip, Integer limit, BasicDBObject filter) {
		Method method = getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.LIST, a -> a.value())
				.orElse(null);
		if (method != null) {
			Object[] args = new Object[method.getParameterCount()];
			Parameter[] para = method.getParameters();
			for (int i = 0; i < para.length; i++) {
				ServiceParam sp = para[i].getAnnotation(ServiceParam.class);
				if (sp != null) {
					String paramName = sp.value();
					// if (paramName.equals(ServiceParam.SKIP)) {
					// args[i] = skip;
					// } else if (paramName.equals(ServiceParam.LIMIT)) {
					// args[i] = limit;
					// } else if (paramName.equals(ServiceParam.FILTER)) {
					// args[i] = filter;
					// } else
					if (paramName.equals(ServiceParam.CONDITION)) {
						args[i] = new BasicDBObject().append("skip", skip).append("limit", limit).append("filter",
								filter);
					} else {
						args[i] = null;
					}
				} else {
					args[i] = null;
				}
			}

			try {
				method.setAccessible(true);
				return method.invoke(getTarget(), args);
			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {// 访问错误，参数错误视作没有定义该方法。
			}
		}
		throw new RuntimeException("没有注解" + DataSet.class + " 值为 list的方法。");
	}

	public long count(BasicDBObject filter) {
		Method method = getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.COUNT, a -> a.value())
				.orElse(null);
		if (method != null) {
			Object[] args = new Object[method.getParameterCount()];
			Parameter[] para = method.getParameters();
			for (int i = 0; i < para.length; i++) {
				ServiceParam sp = para[i].getAnnotation(ServiceParam.class);
				if (ServiceParam.FILTER.equals(sp.value())) {
					args[i] = filter;
				} else {
					args[i] = null;
				}
			}

			try {
				method.setAccessible(true);
				return (long) method.invoke(getTarget(), filter);
			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {// 访问错误，参数错误视作没有定义该方法。
			}
		}
		throw new RuntimeException("没有注解" + DataSet.class + " 值为 count的方法。");
	}

	public Object query() {
		return noParamDataSetMethodInvoke(DataSet.LIST);
	}

	private Object noParamDataSetMethodInvoke(String paramValue) {
		Method m = getContainerMethod(clazz, DataSet.class, assembly.getName(), paramValue, a -> a.value())
				.orElse(null);
		if (m != null) {
			try {
				return m.invoke(getTarget());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		throw new RuntimeException("没有注解" + DataSet.class + " 值为" + paramValue + "的无参方法。");
	}

	// private boolean match(String assemblyName, String useage, DataSet lf) {
	// return Optional.ofNullable(lf).map(a -> a.value()).map(vs -> {
	// for (int i = 0; i < vs.length; i++) {
	// String[] loc = vs[i].split("#");
	// if (loc.length == 1 && useage.equals(loc[0].trim())) {
	// return true;
	// } else if (loc.length > 1 && assemblyName.equals(loc[0].trim()) &&
	// useage.equals(loc[1].trim())) {
	// return true;
	// }
	// }
	// return false;
	// }).orElse(false);
	// }

}
