package com.bizvisionsoft.bruiengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
		throw new RuntimeException(grid.getName() + "ȱ������Դ���塣����BruiDesigner���ҳ���ж�����ڲ��������Դ��ֱ��ָ������");
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
			} catch (IllegalAccessException | IllegalArgumentException e) {// ���ʴ��󣬲�����������û�ж���÷�����

			} catch (InvocationTargetException e) {
				throw new RuntimeException(assembly.getName() + "������Դע��DataSetֵΪ list�ķ������ó���", e.getTargetException());
			}
		}
		throw new RuntimeException(assembly.getName() + "������Դû��ע��DataSetֵΪ list�ķ�����");
	}

	public long count(BasicDBObject filter) {
		Method method = AUtil
				.getContainerMethod(clazz, DataSet.class, assembly.getName(), DataSet.COUNT, a -> a.value())
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
			} catch (IllegalAccessException | IllegalArgumentException e) {// ���ʴ��󣬲�����������û�ж���÷�����
			} catch (InvocationTargetException e) {
				throw new RuntimeException(assembly.getName() + " ����Դcount��������", e.getTargetException());
			}
		}
		throw new RuntimeException(assembly.getName() + " ����Դû��ע��DataSetֵΪ count�ķ�����");
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
				throw new RuntimeException(assembly.getName() + " ����Դע��DataSetֵΪ" + paramValue + "���޲η������ô���",
						e.getTargetException());
			}
		}
		throw new RuntimeException(assembly.getName() + " ����Դû��ע��DataSetֵΪ" + paramValue + "���޲η�����");
	}

	/**
	 * ���ganttͼ��ʼ��ʱ��ʱ�䷶Χ
	 * 
	 * @return
	 */
	public Date[] getGanttInitDateRange() {
		return (Date[]) AUtil.read(clazz, DataSet.class, getTarget(), assembly.getName(), "initDateRange", null,
				a -> a.value());
	}

	public List<?> getGanntInputLink(BasicDBObject linkFilter) {
		Method method;
		method = AUtil.getContainerMethod(clazz, DataSet.class, assembly.getName(), "links", a -> a.value())
				.orElse(null);
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
			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {// ���ʴ��󣬲�����������û�ж���÷�����
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
			} catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {// ���ʴ��󣬲�����������û�ж���÷�����
			}
		} else {
			throw new RuntimeException(assembly.getName() + " ����Դû��ע��DataSetֵΪ data�ķ�����");
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

}
