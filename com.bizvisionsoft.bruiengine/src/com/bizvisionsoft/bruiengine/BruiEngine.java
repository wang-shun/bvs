package com.bizvisionsoft.bruiengine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class BruiEngine {

	@ReadValue
	protected Class<?> clazz;
	protected Object target;

	protected BruiEngine(Class<?> clazz) {
		this.clazz = clazz;
	}

	protected BruiEngine(Object instance) {

		this.clazz = instance.getClass();
		target = instance;
	}

	protected BruiEngine(Class<?> clazz, Object instance) {
		this.clazz = clazz;
		target = instance;
	}

	protected BruiEngine() {
	}

	final protected <T extends Annotation> Object invokeMethod(Class<T> methodAnnotation, Object... arg) {
		return invokeMethodInjectParams(methodAnnotation, arg, null, null);
	}

	final protected <T extends Annotation> Object invokeMethodInjectParams(Class<T> methodAnnotation,
			Object[] parameters, String[] paramAnnotations, Object defaultValueForNoMethod) {
		return AUtil.getMethod(clazz, methodAnnotation).map(method -> {
			Object[] args;
			if (paramAnnotations == null) {
				args = parameters;
			} else {
				args = new Object[method.getParameterCount()];
				Parameter[] para = method.getParameters();
				for (int i = 0; i < para.length; i++) {
					MethodParam emp = para[i].getAnnotation(MethodParam.class);
					if (emp != null) {
						String paramName = emp.value();
						int idx = -1;
						for (int j = 0; j < paramAnnotations.length; j++) {
							if (paramAnnotations[j].equals(paramName)) {
								idx = j;
								break;
							}
						}
						if (idx != -1)
							args[i] = parameters[idx];
					}
				}
			}
			try {
				method.setAccessible(true);
				return method.invoke(getTarget(), args);
			} catch (IllegalAccessException | IllegalArgumentException e) {// 访问错误，参数错误视作没有定义该方法。
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
				throw new RuntimeException("注解为" + methodAnnotation.getName() + "调用目标错误。", e1);
			}
			return null;
		}).orElse(defaultValueForNoMethod);
	}

//	final protected <T extends Annotation> Object getFieldValue(Class<T> annoClass) {
//		try {
//			Field field = AUtil.getField(clazz, annoClass).orElse(null);
//			if (field != null) {
//				field.setAccessible(true);
//				return field.get(target);
//			} else {
//				return null;
//			}
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException("注解为" + annoClass + "的字段或方法无法访问。", e);
//		} catch (IllegalArgumentException e1) {
//			throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e1);
//		}
//	}

	final protected <T extends Annotation> Object getValue(Class<T> annoClass) {
		return AUtil.getValue(clazz,annoClass,target);
	}

	final protected <T extends Annotation> void setFieldValue(Class<T> annoClass, Object value) {
		try {
			Field field = AUtil.getField(clazz, annoClass).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				field.set(target, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("注解为" + annoClass + "的字段或方法无法访问。", e);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e1);
		}
	}

	public BruiEngine newInstance() {
		try {
			if (clazz != null)
				target = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			new RuntimeException(clazz + "无法实例化。", e);
		}
		return this;
	}

	protected BruiEngine init(IServiceWithId[] services) {
		if (clazz == null)
			return this;
		for (int i = 0; i < services.length; i++) {
			injectService(services[i]);
		}
		invokeMethod(Init.class);
		return this;
	}

	/**
	 * 注入服务
	 * 
	 * @param service
	 * @return
	 */
	private BruiEngine injectService(IServiceWithId service) {
		Arrays.asList(clazz.getDeclaredFields()).forEach(f -> {
			Inject anno = f.getAnnotation(Inject.class);
			if (anno != null) {
				Object value = null;
				String name = anno.name();
				if (name.isEmpty()) {
					Class<?> type = f.getType();
					Class<? extends Object> clas = service.getClass();
					if (type.isAssignableFrom(clas) || type.equals(clas)) {
						value = service;
					}
				} else if (service.getServiceId().equals(name)) {
					value = service;
				}

				if (value != null) {
					try {
						f.setAccessible(true);
						f.set(target, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("注解为" + Inject.class + "的字段或方法无法访问。", e);
					} catch (IllegalArgumentException e1) {
						throw new RuntimeException("注解为" + Inject.class + "的字段或方法参数错误。", e1);
					}
				}
			}
		});
		return this;
	}

	public Object getTarget() {
		return target;
	}

}
