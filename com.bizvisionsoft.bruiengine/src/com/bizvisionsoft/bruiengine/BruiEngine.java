package com.bizvisionsoft.bruiengine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.bizvisionsoft.bruicommons.annotation.Init;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.annotation.MethodParam;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.service.annotations.ReadOptions;
import com.bizvisionsoft.service.annotations.ReadValue;
import com.bizvisionsoft.service.annotations.Structure;
import com.bizvisionsoft.service.annotations.WriteValue;

public class BruiEngine {

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

	final protected Object invokeMethod(Class<?> methodAnnotation, Object... arg) {
		return invokeMethodInjectParams(methodAnnotation, arg, null, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	final protected Object invokeMethodInjectParams(Class methodAnnotation, Object[] parameters,
			String[] paramAnnotations, Object defaultValueForNoMethod) {
		return getMethod(clazz, e -> e.getAnnotation(methodAnnotation) != null).map(method -> {
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
				throw new RuntimeException(e1);
			}
			return null;
		}).orElse(defaultValueForNoMethod);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	final protected Object getFieldValue(Class aClass) {
		try {
			Field field = getField(clazz, f -> f.getAnnotation(aClass) != null).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				return field.get(target);
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RuntimeException(e.getCause());
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	final public static Optional<Method> getMethod(Class clazz, Predicate<? super Method> predicate) {
		return Arrays.asList(clazz.getDeclaredMethods()).stream().filter(predicate).findFirst();
	}

	@SuppressWarnings("rawtypes")
	final public static Optional<Field> getField(Class clazz, Predicate<? super Field> predicate) {
		return Arrays.asList(clazz.getDeclaredFields()).stream().filter(predicate).findFirst();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	final protected Object getValue(Class annoClass) {
		try {
			Field field = getField(clazz, f -> f.getAnnotation(annoClass) != null).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				return field.get(target);
			}
			Method method = getMethod(clazz, f -> f.getAnnotation(annoClass) != null).orElse(null);
			if (method != null) {
				method.setAccessible(true);
				return method.invoke(target);
			}
			return null;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e.getCause());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	final protected void setFieldValue(Class annotationClass, Object value) {
		try {
			Field field = getField(clazz, f -> f.getAnnotation(annotationClass) != null).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				field.set(target, value);
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new RuntimeException(e.getCause());
		}
	}

	public BruiEngine newInstance() {
		try {
			if (clazz != null)
				target = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			new RuntimeException(e.getCause());
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
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// 处理Structure 标记
	public static Object getStructureValue(Object element, String name, Object exceptionValue) {
		Class<? extends Object> eClass = element.getClass();
		return getField(eClass, f -> {
			Structure an = f.getAnnotation(Structure.class);
			return an != null && an.name().equals(name);
		}).map(p -> {
			p.setAccessible(true);
			try {
				return p.get(element);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return null;
			}
		}).orElse(getMethod(eClass, f -> {
			Structure an = f.getAnnotation(Structure.class);
			return an != null && an.name().equals(name);
		}).map(p -> {
			p.setAccessible(true);
			try {
				return p.invoke(element);
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				return null;
			}
		}).orElse(exceptionValue));
	}

	//
	////////////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readOptions(Object element, String containerName, String fieldName) {
		Class<? extends Object> clazz = element.getClass();
		return getField(clazz,
				e -> matchOptions(containerName, fieldName, e.getName(), e.getAnnotation(ReadOptions.class)))//
						.map(f -> {
							f.setAccessible(true);
							try {
								return (Map<String, Object>) f.get(element);
							} catch (Exception e1) {
								return null;
							}
						})
						.orElse(getMethod(clazz,
								e -> matchOptions(containerName, fieldName, null, e.getAnnotation(ReadOptions.class)))//
										.map(f -> {
											f.setAccessible(true);
											try {
												return (Map<String, Object>) f.invoke(element);
											} catch (Exception e1) {
												return null;
											}
										}).orElse(null));
	}

	
	public static Object readValue(Object element, String containerName, String fieldName) {
		return readValue(element, containerName, fieldName, null);
	}

	public static Object readValue(Object element, String containerName, String fieldName, Object defaultValue) {
		Class<? extends Object> clazz = element.getClass();
		return getField(clazz, e -> matchRead(containerName, fieldName, e.getName(), e.getAnnotation(ReadValue.class)))//
				.map(f -> {
					f.setAccessible(true);
					try {
						return f.get(element);
					} catch (Exception e1) {
						return null;
					}
				})
				.orElse(getMethod(clazz,
						e -> matchRead(containerName, fieldName, null, e.getAnnotation(ReadValue.class)))//
								.map(f -> {
									f.setAccessible(true);
									try {
										return f.invoke(element);
									} catch (Exception e1) {
										return null;
									}
								}).orElse(defaultValue));
	}

	public static void writeValue(Object element, String containerName, String fieldName, Object value) {
		Class<? extends Object> clazz = element.getClass();
		getField(clazz, e -> matchWrite(containerName, fieldName, e.getName(), e.getAnnotation(WriteValue.class)))//
				.map(f -> {
					f.setAccessible(true);
					try {
						f.set(element, value);
					} catch (Exception e1) {
					}
					return null;
				})
				.orElse(getMethod(clazz,
						e -> matchWrite(containerName, fieldName, null, e.getAnnotation(WriteValue.class)))//
								.map(f -> {
									f.setAccessible(true);
									try {
										f.invoke(element, value);
									} catch (Exception e1) {
									}
									return null;
								}).orElse(null));
	}

	private static boolean matchOptions(String containerName, String fieldName, String fName, ReadOptions lf) {
		return Optional.ofNullable(lf).map(a -> a.value()).map(vs -> {
			if (fieldName.equals(fName) && vs.length == 1 && vs[0].equals("")) {
				return true;
			}
			for (int i = 0; i < vs.length; i++) {
				String[] loc = vs[i].split("#");
				if (loc.length == 1 && fieldName.equals(loc[0].trim())) {
					return true;
				} else if (loc.length > 1 && containerName.equals(loc[0].trim()) && fieldName.equals(loc[1].trim())) {
					return true;
				}
			}
			return false;
		}).orElse(false);
	}

	private static boolean matchRead(String containerName, String fieldName, String fName, ReadValue lf) {
		return Optional.ofNullable(lf).map(a -> a.value()).map(vs -> {
			if (fieldName.equals(fName) && vs.length == 1 && vs[0].equals("")) {
				return true;
			}
			for (int i = 0; i < vs.length; i++) {
				String[] loc = vs[i].split("#");
				if (loc.length == 1 && fieldName.equals(loc[0].trim())) {
					return true;
				} else if (loc.length > 1 && containerName.equals(loc[0].trim()) && fieldName.equals(loc[1].trim())) {
					return true;
				}
			}
			return false;
		}).orElse(false);
	}

	private static boolean matchWrite(String containerName, String fieldName, String fName, WriteValue lf) {
		return Optional.ofNullable(lf).map(a -> a.value()).map(vs -> {
			if (fieldName.equals(fName) && vs.length == 1 && vs[0].equals("")) {
				return true;
			}
			for (int i = 0; i < vs.length; i++) {
				String[] loc = vs[i].split("#");
				if (loc.length == 1 && fieldName.equals(loc[0].trim())) {
					return true;
				} else if (loc.length > 1 && containerName.equals(loc[0].trim()) && fieldName.equals(loc[1].trim())) {
					return true;
				}
			}
			return false;
		}).orElse(false);
	}

	public Object getTarget() {
		return target;
	}

	/**
	 * 复制对象的属性
	 * 
	 * @param info
	 * @param elem
	 */
	public static void copy(Object source, Object target) {
		Arrays.asList(source.getClass().getDeclaredFields()).forEach(srcField -> {
			try {
				Field tgtField = target.getClass().getDeclaredField(srcField.getName());
				tgtField.setAccessible(true);
				srcField.setAccessible(true);
				Object srcValue = srcField.get(source);
				tgtField.set(target, srcValue);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});
	}

}
