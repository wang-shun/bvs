package com.bizvisionsoft.bruiengine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

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
			return invokeMethodInjectParams(method, parameters, paramAnnotations, MethodParam.class,
					m -> m.value());
		}).orElse(defaultValueForNoMethod);
	}

	protected <T extends Annotation> Object invokeMethodInjectParams(Method method, Object[] parameters,
			String[] paramAnnotations, Class<T> parameterAnnotationClass,
			Function<T, String> howToGetParameterNameFromAnnotation) {
		return AUtil.invokeMethodInjectParams(getTarget(), method, parameters, paramAnnotations, parameterAnnotationClass,
				howToGetParameterNameFromAnnotation);
	}

	// final protected <T extends Annotation> Object getFieldValue(Class<T>
	// annoClass) {
	// try {
	// Field field = AUtil.getField(clazz, annoClass).orElse(null);
	// if (field != null) {
	// field.setAccessible(true);
	// return field.get(target);
	// } else {
	// return null;
	// }
	// } catch (IllegalAccessException e) {
	// throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶλ򷽷��޷����ʡ�", e);
	// } catch (IllegalArgumentException e1) {
	// throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ�����������", e1);
	// }
	// }

	final protected <T extends Annotation> Object getValue(Class<T> annoClass) {
		return AUtil.getValue(clazz, annoClass, target);
	}

	final protected <T extends Annotation> void setFieldValue(Class<T> annoClass, Object value) {
		try {
			Field field = AUtil.getField(clazz, annoClass).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				field.set(target, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶλ򷽷��޷����ʡ�", e);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ�����������", e1);
		}
	}

	public BruiEngine newInstance() {
		try {
			if (clazz != null)
				target = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			new RuntimeException(clazz + "�޷�ʵ������", e);
		}
		return this;
	}

	public BruiEngine init(IServiceWithId[] services) {
		if (clazz == null)
			return this;
		for (int i = 0; i < services.length; i++) {
			injectService(services[i]);
		}
		invokeMethod(Init.class);
		return this;
	}

	/**
	 * ע�����
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
					} catch (NullPointerException e) {
						throw new RuntimeException("ע���ֶ�"+f.getName()+"ʱĿ������ָ���쳣��", e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("ע���ֶ�"+f.getName()+"ʱĿ������޷����ʡ�", e);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException("ע���ֶ�"+f.getName()+"ʱĿ������������", e);
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
