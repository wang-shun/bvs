package com.bizvisionsoft.bruiengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.Listener;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.util.EngUtil;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class BruiDataSetEngine extends BruiEngine {

	private String cName;
	private String modelClassName;

	public BruiDataSetEngine(Class<?> clazz) {
		super(clazz);
	}

	public BruiDataSetEngine(Object serivce) {
		super(serivce);
	}

	public BruiDataSetEngine(Class<?> clazz, Object serivce) {
		super(clazz, serivce);
	}

	public static BruiDataSetEngine create(Assembly grid, IServiceWithId... services) {
		BruiDataSetEngine eng = (BruiDataSetEngine) load(grid);// load
		if (eng != null)
			return (BruiDataSetEngine) eng.newInstance().init(services);
		return null;
	}

	private static BruiDataSetEngine load(Assembly grid) {
		
		String bundleId = grid.getGridDataSetBundleId();
		String className = grid.getGridDataSetClassName();

		if (bundleId != null && !bundleId.isEmpty() && className != null && !className.isEmpty()) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiDataSetEngine(bundle.loadClass(className)).setAssembly(grid);
			} catch (Exception e) {
				throw new RuntimeException(grid.getName() + "数据源定义错误。");
			}
		}

		String serivceName;
		if(!EngUtil.isEmptyOrNull(grid.getModelClassName())){
			serivceName ="com.bizvisionsoft.service.UniversalDataService";
		}else {
			serivceName = grid.getGridDataSetService();
		}
		if (!EngUtil.isEmptyOrNull(serivceName)) {
			Object[] service = Services.getService(serivceName);
			if (service != null) {
				return new BruiDataSetEngine((Class<?>) service[0], service[1]).setAssembly(grid);
			}
		}

		return null;
	}

	public BruiDataSetEngine setAssembly(Assembly assembly) {
		cName = assembly.getName();
		modelClassName = assembly.getModelClassName();
		return this;
	}

	public Object query(Integer skip, Integer limit, BasicDBObject filter, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, DataSet.LIST, a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			names.add(MethodParam.SKIP);
			values.add(skip);

			names.add(MethodParam.LIMIT);
			values.add(limit);

			names.add(MethodParam.FILTER);
			values.add(filter);

			names.add(MethodParam.CONDITION);
			values.add(new BasicDBObject().append("skip", skip).append("limit", limit).append("filter", filter));

			injectCommonParameters(context, names, values, modelClassName);

			return invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]), MethodParam.class,
					t -> t.value());

		}
		throw new RuntimeException(cName + "的数据源没有注解DataSet的list方法。");
	}

	public long count(BasicDBObject filter, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, DataSet.COUNT, a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();

			names.add(MethodParam.FILTER);
			values.add(filter);

			injectCommonParameters(context, names, values, modelClassName);

			return (long) invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]),
					MethodParam.class, t -> t.value());
		}
		throw new RuntimeException(cName + " 数据源没有注解DataSet值为 count的方法。");
	}

	@Deprecated
	public Object query() {
		return noParamDataSetMethodInvoke(DataSet.LIST);
	}

	@Deprecated
	private Object noParamDataSetMethodInvoke(String paramValue) {
		Method m = AUtil.getContainerMethod(clazz, DataSet.class, cName, paramValue, a -> a.value()).orElse(null);
		if (m != null) {
			try {
				return m.invoke(getTarget());
			} catch (IllegalAccessException | IllegalArgumentException e) {
			} catch (InvocationTargetException e) {
				throw new RuntimeException(cName + " 数据源注解DataSet值为" + paramValue + "的无参方法调用错误。",
						e.getTargetException());
			}
		}
		throw new RuntimeException(cName + " 数据源没有注解DataSet值为" + paramValue + "的无参方法。");
	}

	/**
	 * 获得gantt图初始化时的时间范围
	 * 
	 * @return
	 */
	@Deprecated
	public Date[] getGanttInitDateRange() {
		return (Date[]) AUtil.read(clazz, DataSet.class, getTarget(), cName, "initDateRange", null, a -> a.value());
	}

	public Object getGanntInputLink(BasicDBObject linkFilter, IBruiContext context) {
		try {
			return query(linkFilter, context, "links");
		} catch (Exception e) {
		}
		return null;
	}

	public Object getGanntInputData(BasicDBObject workFilter, IBruiContext context) {
		return query(workFilter, context, "data");
	}

	/**
	 * 非表格类GridPart的使用
	 * 
	 * @param filter
	 * @param context
	 * @param fName
	 * @return
	 */
	public Object query(BasicDBObject filter, IBruiContext context, String fName) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, fName, a -> a.value()).orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			if (filter != null) {
				names.add(MethodParam.FILTER);
				values.add(filter);
			}
			
			injectCommonParameters(context, names, values, modelClassName);

			return invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]), MethodParam.class,
					t -> t.value());

		}
		throw new RuntimeException(cName + " 数据源没有注解DataSet值为 " + fName + "的方法。");
	}

	public void replace(Object element, BasicDBObject data, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, DataSet.UPDATE, a -> a.value())
				.orElse(null);
		if (method != null) {
			try {
				if (!data.containsField("_id")) {
					// 没有_id字段的不会保存到数据库
					return;
				}
				Object _id = data.get("_id");
				data.removeField("_id");
				BasicDBObject filterAndUpdate = new FilterAndUpdate().filter(new BasicDBObject("_id", _id)).set(data)
						.bson();
				method.setAccessible(true);
				if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(BasicDBObject.class)) {
					// 兼容简写模式，无注解
					method.invoke(getTarget(), filterAndUpdate);
				} else {
					List<String> names = new ArrayList<String>();
					List<Object> values = new ArrayList<Object>();
					names.addAll(Arrays
							.asList(new String[] { MethodParam._ID, MethodParam.OBJECT, MethodParam.FILTER_N_UPDATE }));
					values.addAll(Arrays
							.asList(new Object[] { EngUtil.getBson(element).get("_id"), element, filterAndUpdate }));
					injectCommonParameters(context, names, values, modelClassName);
					invokeMethodInjectParams(method, values.toArray(new Object[0]), names.toArray(new String[0]),
							MethodParam.class, t -> t.value());
				}
			} catch (IllegalAccessException | IllegalArgumentException e) {
				logger.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException().getMessage());
			}
		}
	}

	public Object insert(Object parent, Object element, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, DataSet.INSERT, a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();

			if (parent == null) {
				names.addAll(Arrays.asList(new String[] { MethodParam._ID, MethodParam.OBJECT }));
				values.addAll(Arrays.asList(new Object[] { EngUtil.getBson(element).get("_id"), element }));
			} else {
				names.addAll(Arrays.asList(new String[] { MethodParam.PARENT_ID, MethodParam.PARENT_OBJECT,
						MethodParam._ID, MethodParam.OBJECT }));
				values.addAll(Arrays.asList(new Object[] { EngUtil.getBson(parent).get("_id"), parent,
						EngUtil.getBson(element).get("_id"), element }));
			}

			injectCommonParameters(context, names, values, modelClassName);

			return invokeMethodInjectParams(method, values.toArray(new Object[0]), names.toArray(new String[0]),
					MethodParam.class, t -> t.value());
		}
		return new RuntimeException("DateSet缺少Insert注解的方法");
	}

	public void delete(Object element, Object parent, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, DataSet.DELETE, a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();

			if (parent == null) {
				names.addAll(Arrays.asList(new String[] { MethodParam._ID, MethodParam.OBJECT }));
				values.addAll(Arrays.asList(new Object[] { EngUtil.getBson(element).get("_id"), element }));
			} else {
				names.addAll(Arrays.asList(new String[] { MethodParam.PARENT_ID, MethodParam.PARENT_OBJECT,
						MethodParam._ID, MethodParam.OBJECT }));
				values.addAll(Arrays.asList(new Object[] { EngUtil.getBson(parent).get("_id"), parent,
						EngUtil.getBson(element).get("_id"), element }));
			}

			injectCommonParameters(context, names, values, modelClassName);

			invokeMethodInjectParams(method, values.toArray(new Object[0]), names.toArray(new String[0]),
					MethodParam.class, t -> t.value());
		}
	}

	public Object query(Object element, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, DataSet.GET, a -> a.value()).orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			names.addAll(Arrays.asList(new String[] { MethodParam._ID, MethodParam.OBJECT }));
			values.addAll(Arrays.asList(new Object[] { EngUtil.getBson(element).get("_id"), element }));
			injectCommonParameters(context, names, values, element.getClass().getName());

			return invokeMethodInjectParams(method, values.toArray(new Object[0]), names.toArray(new String[0]),
					MethodParam.class, t -> t.value());		}
		return null;
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
					} else if (loc.length > 1 && cName.equals(loc[0].trim())) {
						listenerName = loc[1].trim();
					}
					if (listenerName != null) {
						con.accept(listenerName, m);
					}
				}

			}
		});
	}

	public BruiDataSetEngine newInstance() {
		return (BruiDataSetEngine) super.newInstance();
	}

}
