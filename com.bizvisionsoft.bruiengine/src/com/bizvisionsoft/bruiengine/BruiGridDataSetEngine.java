package com.bizvisionsoft.bruiengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.internal.loader.EquinoxClassLoader;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
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
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.LIST, a -> a.value())
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
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.COUNT, a -> a.value())
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
		Method m = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), paramValue, a -> a.value())
				.orElse(null);
		if (m != null) {
			try {
				return m.invoke(getTarget());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}
		throw new RuntimeException("没有注解" + DataSet.class + " 值为" + paramValue + "的无参方法。");
	}

	/**
	 * 获得gantt图初始化时的时间范围
	 * 
	 * @return
	 */
	public Date[] getGanttInitDateRange() {
		return (Date[]) AUtil.read(clazz, DataSet.class, getTarget(), assembly.getName(), "initDateRange", null,
				a -> a.value());
	}

	public JsonObject transformToJsonInput(List<?> data, List<?> links) {
		// 准备数据转换函数
		BiFunction<String, Object, Object> convertor = (n, v) -> {
			if (v instanceof Date)
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
			return v;
		};

		// 处理模型
		JsonArray _data = new JsonArray();
		JsonArray _links = new JsonArray();

		if (data != null)
			data.forEach(o -> {
				JsonObject jo = AUtil.readJsonFrom(o.getClass(), o, assembly.getName(), true, true, true, convertor);
				String bundleId = null;
				ClassLoader loader = o.getClass().getClassLoader();
				if (loader instanceof EquinoxClassLoader) {
					bundleId = ((EquinoxClassLoader) loader).getBundle().getSymbolicName();
				}
				// 添加类信息
				jo.add("$classInfo",
						new JsonObject().add("bundleId", bundleId).add("className", o.getClass().getName()));
				_data.add(jo);
			});

		if (links != null)
			links.forEach(o -> {
				JsonObject jo = AUtil.readJsonFrom(o.getClass(), o, assembly.getName(), true, true, true, convertor);
				String bundleId = null;
				ClassLoader loader = o.getClass().getClassLoader();
				if (loader instanceof EquinoxClassLoader) {
					bundleId = ((EquinoxClassLoader) loader).getBundle().getSymbolicName();
				}
				// 添加类信息
				jo.add("$classInfo",
						new JsonObject().add("bundleId", bundleId).add("className", o.getClass().getName()));
				_links.add(jo);
			});

		return new JsonObject().add("data", _data).add("links", _links);
	}

	public List<?> getGanntInputLink(BasicDBObject linkFilter) {
		Method method;
		method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), "links", a -> a.value()).orElse(null);
		if (method != null) {
			Object[] args = new Object[method.getParameterCount()];
			Parameter[] para = method.getParameters();
			for (int i = 0; i < para.length; i++) {
				ServiceParam sp = para[i].getAnnotation(ServiceParam.class);
				if (ServiceParam.FILTER.equals(sp.value())) {
					args[i] = linkFilter;
				} else {
					args[i] = null;
				}
			}

			try {
				method.setAccessible(true);
				return (List<?>) method.invoke(getTarget(), args);
			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {// 访问错误，参数错误视作没有定义该方法。
			}
		} else {
		}
		return null;
	}

	public List<?> getGanntInputData(BasicDBObject workFilter) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), "data", a -> a.value())
				.orElse(null);
		if (method != null) {
			Object[] args = new Object[method.getParameterCount()];
			Parameter[] para = method.getParameters();
			for (int i = 0; i < para.length; i++) {
				ServiceParam sp = para[i].getAnnotation(ServiceParam.class);
				if (ServiceParam.FILTER.equals(sp.value())) {
					args[i] = workFilter;
				} else {
					args[i] = null;
				}
			}

			try {
				method.setAccessible(true);
				return (List<?>) method.invoke(getTarget(), args);
			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {// 访问错误，参数错误视作没有定义该方法。
			}
		} else {
			throw new RuntimeException("没有注解" + DataSet.class + " 值为 data的方法。");
		}
		return null;
	}

}
