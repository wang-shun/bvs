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
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class BruiDataSetEngine extends BruiEngine {

	private Assembly assembly;

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
		return (BruiDataSetEngine) load(grid)// load
				.newInstance().init(services);
	}

	private static BruiDataSetEngine load(Assembly grid) {
		String bundleId = grid.getGridDataSetBundleId();
		String className = grid.getGridDataSetClassName();

		if (bundleId != null && !bundleId.isEmpty() && className != null && !className.isEmpty()) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiDataSetEngine(bundle.loadClass(className)).setAssembly(grid);
			} catch (Exception e) {
				throw new RuntimeException(e.getCause());
			}
		}

		String serivceName = grid.getGridDataSetService();
		if (serivceName != null) {
			Object[] service = Services.getService(serivceName);
			if (service != null) {
				return new BruiDataSetEngine((Class<?>) service[0], service[1]).setAssembly(grid);
			}
		}
		throw new RuntimeException(grid.getName() + "缺少数据源定义。请在BruiDesigner组件页面中定义基于插件的数据源或直接指定服务。");
	}

	public BruiDataSetEngine setAssembly(Assembly assembly) {
		this.assembly = assembly;
		return this;
	}

	public Object query(Integer skip, Integer limit, BasicDBObject filter, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.LIST, a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			names.add(ServiceParam.SKIP);
			values.add(skip);

			names.add(ServiceParam.LIMIT);
			values.add(limit);

			names.add(ServiceParam.FILTER);
			values.add(filter);

			names.add(ServiceParam.CONDITION);
			values.add(new BasicDBObject().append("skip", skip).append("limit", limit).append("filter", filter));

			if (context != null) {
				injectContextInputParameters(context, names, values);

				injectRootContextInputParameters(context, names, values);
			}

			injectUserParameters(names, values);

			return invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]), ServiceParam.class,
					t -> t.value());

			// try {
			// method.setAccessible(true);
			// return method.invoke(getTarget(), args);
			// } catch (IllegalAccessException | IllegalArgumentException e) {//
			// 访问错误，参数错误视作没有定义该方法。
			//
			// } catch (InvocationTargetException e) {
			// String message = assembly.getName() + "的数据源注解DataSet的list方法调用出错。" +
			// e.getTargetException().getMessage();
			// throw new RuntimeException(message, e.getTargetException());
			// }
		}
		throw new RuntimeException(assembly.getName() + "的数据源没有注解DataSet的list方法。");
	}

	public long count(BasicDBObject filter, IBruiContext context) {
		Method method = AUtil
				.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.COUNT, a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();

			names.add(ServiceParam.FILTER);
			values.add(filter);

			injectContextInputParameters(context, names, values);

			injectRootContextInputParameters(context, names, values);

			injectUserParameters(names, values);

			return (long) invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]),
					ServiceParam.class, t -> t.value());
		}
		throw new RuntimeException(assembly.getName() + " 数据源没有注解DataSet值为 count的方法。");
	}

	private void injectContextInputParameters(IBruiContext context, List<String> names, List<Object> values) {
		if (context != null) {
			Object input = context.getInput();
			if (input != null) {
				names.add(ServiceParam.CONTEXT_INPUT_OBJECT);
				values.add(input);

				Object _id = Util.getBson(input).get("_id");
				if (_id != null) {
					names.add(ServiceParam.CONTEXT_INPUT_OBJECT_ID);
					values.add(_id);
				}
			}
		}
	}

	private void injectRootContextInputParameters(IBruiContext context, List<String> names, List<Object> values) {
		if (context != null) {
			Object input = context.getRootInput();
			if (input != null) {
				names.add(ServiceParam.ROOT_CONTEXT_INPUT_OBJECT);
				values.add(input);

				Object _id = Util.getBson(input).get("_id");
				if (_id != null) {
					names.add(ServiceParam.ROOT_CONTEXT_INPUT_OBJECT_ID);
					values.add(_id);
				}
			}
		}
	}

	private void injectUserParameters(List<String> names, List<Object> values) {
		try {
			User user = Brui.sessionManager.getSessionUserInfo();
			if (user != null) {
				names.add(ServiceParam.CURRENT_USER);
				values.add(user);

				names.add(ServiceParam.CURRENT_USER_ID);
				values.add(user.getUserId());
			}
		} catch (Exception e) {
		}
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
			} catch (IllegalAccessException | IllegalArgumentException e) {
			} catch (InvocationTargetException e) {
				throw new RuntimeException(assembly.getName() + " 数据源注解DataSet值为" + paramValue + "的无参方法调用错误。",
						e.getTargetException());
			}
		}
		throw new RuntimeException(assembly.getName() + " 数据源没有注解DataSet值为" + paramValue + "的无参方法。");
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

	public List<?> getGanntInputLink(BasicDBObject linkFilter, IBruiContext context) {
		Method method;
		method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), "links", a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			if (linkFilter != null) {
				names.add(ServiceParam.FILTER);
				values.add(linkFilter);
			}

			injectContextInputParameters(context, names, values);

			injectRootContextInputParameters(context, names, values);

			injectUserParameters(names, values);

			return (List<?>) invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]),
					ServiceParam.class, t -> t.value());

		}
		return null;
	}

	public List<?> getGanntInputData(BasicDBObject workFilter, IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), "data", a -> a.value())
				.orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();
			if (workFilter != null) {
				names.add(ServiceParam.FILTER);
				values.add(workFilter);
			}

			injectContextInputParameters(context, names, values);

			injectRootContextInputParameters(context, names, values);

			injectUserParameters(names, values);

			return (List<?>) invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]),
					ServiceParam.class, t -> t.value());
		} else {
			throw new RuntimeException(assembly.getName() + " 数据源没有注解DataSet值为 data的方法。");
		}
	}

	public void replace(Object element, BasicDBObject data) {
		Method method = AUtil
				.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.UPDATE, a -> a.value())
				.orElse(null);
		if (method != null) {
			try {
				if (!data.containsField("_id")) {
					return;
				}
				Object _id = data.get("_id");
				data.removeField("_id");
				BasicDBObject filterAndUpdate = new FilterAndUpdate().filter(new BasicDBObject("_id", _id)).set(data)
						.bson();
				method.invoke(getTarget(), filterAndUpdate);
			} catch (IllegalAccessException | IllegalArgumentException e) {
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException().getMessage());
			}
		}
	}

	public Object insert(Object parent, Object element) {
		Method method = AUtil
				.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.INSERT, a -> a.value())
				.orElse(null);
		if (method != null) {
			Object[] values;
			String[] names;
			if (parent == null) {
				values = new Object[] { Util.getBson(element).get("_id"), element };
				names = new String[] { ServiceParam._ID, ServiceParam.OBJECT };
			} else {
				values = new Object[] { Util.getBson(parent).get("_id"), parent, Util.getBson(element).get("_id"),
						element };
				names = new String[] { ServiceParam.PARENT_ID, ServiceParam.PARENT_OBJECT, ServiceParam._ID,
						ServiceParam.OBJECT };
			}
			return invokeMethodInjectParams(method, values, names, ServiceParam.class, t -> t.value());
		}
		return new RuntimeException("DateSet缺少Insert注解的方法");
	}

	public void delete(Object element, Object parent) {
		Method method = AUtil
				.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.DELETE, a -> a.value())
				.orElse(null);
		if (method != null) {
			Object[] values;
			String[] names;
			if (parent == null) {
				values = new Object[] { Util.getBson(element).get("_id"), element };
				names = new String[] { ServiceParam._ID, ServiceParam.OBJECT };
			} else {
				values = new Object[] { Util.getBson(parent).get("_id"), parent, Util.getBson(element).get("_id"),
						element };
				names = new String[] { ServiceParam.PARENT_ID, ServiceParam.PARENT_OBJECT, ServiceParam._ID,
						ServiceParam.OBJECT };
			}
			invokeMethodInjectParams(method, values, names, ServiceParam.class, t -> t.value());
		}
	}

	public Object query(Object element) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.GET, a -> a.value())
				.orElse(null);
		if (method != null) {
			Object[] values;
			String[] names;
			values = new Object[] { Util.getBson(element).get("_id"), element };
			names = new String[] { ServiceParam._ID, ServiceParam.OBJECT };
			return invokeMethodInjectParams(method, values, names, ServiceParam.class, t -> t.value());
		}
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

	public BruiDataSetEngine newInstance() {
		return (BruiDataSetEngine) super.newInstance();
	}

}
