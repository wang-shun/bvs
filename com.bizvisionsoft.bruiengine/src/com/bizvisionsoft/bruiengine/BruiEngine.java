package com.bizvisionsoft.bruiengine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;

import com.bizvisionsoft.bruicommons.annotation.Init;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.annotation.MethodParam;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.annotations.ReadOptions;
import com.bizvisionsoft.service.annotations.ReadValidation;
import com.bizvisionsoft.service.annotations.ReadValue;
import com.bizvisionsoft.service.annotations.Structure;
import com.bizvisionsoft.service.annotations.WriteValue;
import com.google.gson.GsonBuilder;

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
		return getMethod(clazz, methodAnnotation).map(method -> {
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
				throw new RuntimeException("注解为" + methodAnnotation + "调用目标错误。", e1);
			}
			return null;
		}).orElse(defaultValueForNoMethod);
	}

	final protected <T extends Annotation> Object getFieldValue(Class<T> annoClass) {
		try {
			Field field = getField(clazz, annoClass).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				return field.get(target);
			} else {
				return null;
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("注解为" + annoClass + "的字段或方法无法访问。", e);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e1);
		}
	}

	final protected <T extends Annotation> Object getValue(Class<T> annoClass) {
		try {
			Field field = getField(clazz, annoClass).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				return field.get(target);
			}
			Method method = getMethod(clazz, annoClass).orElse(null);
			if (method != null) {
				method.setAccessible(true);
				return method.invoke(target);
			} else {
				// throw new BruiEngineError("没有注解为" + annoClass + "的字段或方法。");
				return null;
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("注解为" + annoClass + "的字段或方法无法访问。", e);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e1);
		} catch (InvocationTargetException e2) {
			throw new RuntimeException("注解为" + annoClass + "调用目标错误。", e2);
		}
	}

	final protected <T extends Annotation> void setFieldValue(Class<T> annoClass, Object value) {
		try {
			Field field = getField(clazz, annoClass).orElse(null);
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

	////////////////////////////////////////////////////////////////////////////////////////////////
	// 处理Structure 标记
	public static Object getStructureValue(Object element, String cName, String fName, Object defaultValue) {
		return read(element.getClass(), Structure.class, element, cName, fName, defaultValue, a -> a.value());
	}

	//
	////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public static Map<String, Object> readOptions(Object element, String cName, String fName) {
		return (Map<String, Object>) read(element.getClass(), ReadOptions.class, element, cName, fName,
				new LinkedHashMap<String, Object>(), a -> a.value());
	}

	public static Object readValidation(Object element, String cName, String fName) {
		return read(element.getClass(), ReadValidation.class, element, cName, fName, null, a -> a.value());
	}

	/**
	 * 读取对象注解为 ReadValue的
	 * 值。先读字段，后调用方法。没有对应字段或方法（包括字段类型、方法参数不对时，都视作不对应）时，返回defaultValue
	 * 注解形式为ReadValue("容器名 # 字段名")
	 * 
	 * @param element
	 *            目标对象
	 * @param cName
	 *            ReadValue注解的 容器名
	 * @param fName
	 *            注解的 字段名
	 * @param defaultValue
	 *            无匹配时返回的默认值
	 * @return
	 */
	public static Object readValue(Object element, String cName, String fName, Object defaultValue) {
		if (element instanceof Map<?, ?>) {
			return ((Map<?, ?>) element).get(fName);
		} else {
			return read(element.getClass(), ReadValue.class, element, cName, fName, defaultValue, a -> a.value());
		}
	}

	@SuppressWarnings("unchecked")
	public static void writeValue(Object element, String cName, String fName, Object value) {
		if (element instanceof Map<?, ?>) {
			((Map<String, Object>) element).put(fName, value);
		} else {
			write(element.getClass(), WriteValue.class, element, cName, fName, value, a -> a.value());
		}
	}

	public static <T extends Annotation> Object read(Class<?> c, Class<T> annoClass, Object element, String cName,
			String fName, Object defaultValue, Function<T, String[]> func) {
		Field f = getContainerField(c, annoClass, cName, fName, func).orElse(null);
		if (f != null)
			try {
				f.setAccessible(true);
				return f.get(element);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("注解为" + annoClass + "的字段无法访问。", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("注解为" + annoClass + "的字段参数错误。", e);
			}

		Method m = getContainerMethod(c, annoClass, cName, fName, func).orElse(null);
		if (m != null)
			try {
				m.setAccessible(true);
				return m.invoke(element);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("注解为" + annoClass + "的方法无法访问。", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("注解为" + annoClass + "调用目标错误。", e);
			}

		return defaultValue;
	}

	public static <T extends Annotation> void write(Class<?> c, Class<T> annoClass, Object element, String cName,
			String fName, Object value, Function<T, String[]> func) {
		Field f = getContainerField(c, annoClass, cName, fName, func).orElse(null);
		if (f != null)
			try {
				f.setAccessible(true);
				f.set(element, value);
				return;
			} catch (IllegalAccessException e) {
				throw new RuntimeException("注解为" + annoClass + "的字段无法访问。", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("注解为" + annoClass + "的字段参数错误。", e);
			}

		Method m = getContainerMethod(c, annoClass, cName, fName, func).orElse(null);
		if (m != null)
			try {
				m.setAccessible(true);
				m.invoke(element, value);
				return;
			} catch (IllegalAccessException e) {
				throw new RuntimeException("注解为" + annoClass + "的方法无法访问。", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("注解为" + annoClass + "调用目标错误。", e);
			}
	}

	public static <T extends Annotation> Optional<Field> getContainerField(Class<?> clazz, Class<T> annoClass,
			String cName, String fName, Function<T, String[]> func) {
		return Arrays.asList(clazz.getDeclaredFields()).stream()
				.filter(e -> Optional.ofNullable(e)
						.map(f -> match((T) f.getAnnotation(annoClass), f.getName(), cName, fName, func)).orElse(false))
				.findFirst();
	}

	public static <T extends Annotation> Optional<Method> getContainerMethod(Class<?> clazz, Class<T> aclass,
			String cName, String fName, Function<T, String[]> func) {
		return Arrays.asList(clazz.getDeclaredMethods()).stream()
				.filter(e -> Optional.ofNullable(e)
						.map(f -> match((T) f.getAnnotation(aclass), f.getName(), cName, fName, func)).orElse(false))
				.findFirst();
	}

	/**
	 * 
	 * @param clazz
	 *            目标类
	 * @param annoClass
	 *            注解类
	 * @param annoValue
	 *            注解值
	 * @param valueFunction
	 *            注解取值函数
	 * @return
	 */
	final public static <T extends Annotation> Optional<Field> getField(Class<?> clazz, Class<T> annoClass,
			String annoValue, Function<T, String> valueFunction) {
		return Arrays.asList(clazz.getDeclaredFields()).stream().filter(f -> {
			T anno = f.getAnnotation(annoClass);
			if (valueFunction == null && annoValue == null) {
				return anno != null;
			} else {
				return anno != null && valueFunction.apply(anno).equals(annoValue);
			}
		}).findFirst();
	}

	final public static <T extends Annotation> Optional<Field> getField(Class<?> clazz, Class<T> annoClass,
			Object annoValue, Function<T, Object> valueFunction, boolean withNameMatch) {
		return Arrays.asList(clazz.getDeclaredFields()).stream().filter(f -> {
			T anno = f.getAnnotation(annoClass);
			if (anno == null) {// 没有注解
				// 但有字段名称匹配的 返回真
				if ((anno == null) && withNameMatch && f.getName().equals(annoValue)) {
					return true;
				} else {// 否则都不匹配
					return false;
				}
			} else {// 有注解
				if (valueFunction == null) {// 未指定取值函数的，注解匹配就可以
					return true;
				} else {
					// 根据取值函数获取注解值
					Object value = valueFunction.apply(anno);
					if (value instanceof Object[]) {// 如果注解值是数组的，单一元素匹配就可以了
						return Arrays.asList((Object[]) value).stream().allMatch(e -> e != null && e.equals(annoValue));
					} else {
						return value != null && value.equals(annoValue);
					}
				}
			}

		}).findFirst();
	}

	final public static <T extends Annotation> Optional<Field> getField(Class<?> clazz, Class<T> annoClass) {
		return getField(clazz, annoClass, null, null);
	}

	/**
	 * 
	 * @param clazz
	 *            目标类
	 * @param annoClass
	 *            注解类
	 * @param annoValue
	 *            注解值
	 * @param valueFunction
	 *            注解取值函数
	 * @return
	 */
	final public static <T extends Annotation> Optional<Method> getMethod(Class<?> clazz, Class<T> annoClass,
			String annoValue, Function<T, String> valueFunction) {
		return Arrays.asList(clazz.getDeclaredMethods()).stream().filter(f -> {
			T anno = f.getAnnotation(annoClass);
			if (valueFunction == null && annoValue == null) {
				return anno != null;
			} else {
				return anno != null && valueFunction.apply(anno).equals(annoValue);
			}
		}).findFirst();
	}

	final public static <T extends Annotation> Optional<Method> getMethod(Class<?> clazz, Class<T> annoClass) {
		return getMethod(clazz, annoClass, null, null);
	}

	final public static <T extends Annotation> Optional<Method> getMethod(Class<?> clazz, Class<T> annoClass,
			Object annoValue, Function<T, Object> valueFunction, boolean withNameMatch) {
		return Arrays.asList(clazz.getDeclaredMethods()).stream().filter(f -> {
			T anno = f.getAnnotation(annoClass);
			if (anno == null) {// 没有注解
				// 但有字段名称匹配的 返回真
				if ((anno == null) && withNameMatch && f.getName().equals(annoValue)) {
					return true;
				} else {// 否则都不匹配
					return false;
				}
			} else {// 有注解
				if (valueFunction == null) {// 未指定取值函数的，注解匹配就可以
					return true;
				} else {
					// 根据取值函数获取注解值
					Object value = valueFunction.apply(anno);
					if (value instanceof Object[]) {// 如果注解值是数组的，单一元素匹配就可以了
						return Arrays.asList((Object[]) value).stream().allMatch(e -> e != null && e.equals(annoValue));
					} else {
						return value != null && value.equals(annoValue);
					}
				}
			}

		}).findFirst();
	}

	/**
	 * 
	 * 容器注解匹配，形式如:</br>
	 * 注解({容器1 # 字段1 , 容器2 # 字段2})</br>
	 * 注解({字段1 , 容器2 # 字段2})</br>
	 * 注解(字段)</br>
	 * 注解（这时匹配fieldOrMethodName与fName相同）</br>
	 * 如果注解中不包括容器，该注解则表示在各类容器中通用
	 * 
	 * @param ann
	 *            注解
	 * @param fieldOrMethodName
	 *            被注解的字段名或方法名,可以空
	 * @param cName
	 *            注解容器名,可以null
	 * @param fName
	 *            注解字段名,不可为null
	 * @param value
	 *            注解取值函数 不可为null 取值函数必须返回 String[]
	 * @return
	 */
	private static <T> boolean match(T ann, String fieldOrMethodName, String cName, String fName,
			Function<T, String[]> value) {
		return Optional.ofNullable(ann).map(a -> value.apply(a)).map(v -> {
			if (fieldOrMethodName != null && fieldOrMethodName.equals(fName) && v.length == 1 && v[0].equals("")) {
				return true;
			}
			for (int i = 0; i < ((String[]) v).length; i++) {
				String[] loc = ((String[]) v)[i].split("#");
				if (loc.length == 1 && fName.equals(loc[0].trim())) {
					return true;
				} else if (loc.length > 1 && cName.equals(loc[0].trim()) && fName.equals(loc[1].trim())) {
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
	 * 复制对象的属性,只复制字段值
	 * 
	 * @param info
	 * @param elem
	 * @return
	 */
	public static Object simpleCopy(Object source, Object target) {
		Arrays.asList(source.getClass().getDeclaredFields()).forEach(srcField -> {
			try {
				Field tgtField = target.getClass().getDeclaredField(srcField.getName());
				tgtField.setAccessible(true);
				srcField.setAccessible(true);
				Object srcValue = srcField.get(source);
				tgtField.set(target, srcValue);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			}
		});
		return target;
	}

	public static Object deepCopy(Object elem) {
		String json = new GsonBuilder().create().toJson(elem);
		return new GsonBuilder().create().fromJson(json, elem.getClass());
	}

	public static <T> Object createObjectFrom(Class<T> clazz, JsonObject json, String cName, boolean ignoreEmptyCName,
			boolean ignoreEmptyFName, boolean ignoreNull, BiFunction<String, Object, Object> valueConvertor) {
		T object = null;
		try {
			object = clazz.newInstance();
			json.names();

		} catch (InstantiationException | IllegalAccessException e) {
		}

		return object;

	}

	/**
	 * 容器名称,根据容器名称读取Json字符串 不支持数组类型！！！
	 * 
	 * @param <R>
	 * @param <T>
	 * 
	 * @param clazz,
	 *            被读取对象的类（带有注解的），对象可继承于该类
	 * @param element,被读取的对象
	 * @param cName
	 *            容器名称
	 * @param ignoreEmptyCName
	 *            是否忽略容器名，如果忽略容器名，所有ReadValue 空注解的都将被读取
	 * @param ignoreEmptyFName
	 *            是否忽略字段名，如果忽略字段名，将采用类的字段名称读取
	 * @param valueConvertor
	 *            数据转换函数
	 * @return
	 */
	public static JsonObject readJsonFrom(Class<?> clazz, Object element, String cName, boolean ignoreEmptyCName,
			boolean ignoreEmptyFName, boolean ignoreNull, BiFunction<String, Object, Object> valueConvertor) {

		JsonObject result = new JsonObject();
		// 检查
		if (Util.isEmptyOrNull(cName))
			throw new IllegalArgumentException("容器名称为空");
		if (element == null)
			throw new IllegalArgumentException("目标对象为空");
		if (!clazz.isAssignableFrom(element.getClass()))
			throw new IllegalArgumentException("目标对象是指定类（或继承）的实例");
		// 处理字段
		Arrays.asList(clazz.getDeclaredFields()).stream().forEach(e -> {
			String nName = e.getName();
			ReadValue ano = e.getAnnotation(ReadValue.class);
			String targetField = checkField(cName, ignoreEmptyCName, ignoreEmptyFName, nName, ano);
			if (!Util.isEmptyOrNull(targetField))
				try {
					e.setAccessible(true);
					putJsonValue(element, ignoreNull, valueConvertor, result, targetField, e.get(element));
				} catch (IllegalArgumentException | IllegalAccessException e1) {
				}
		});
		// 处理方法
		Arrays.asList(clazz.getDeclaredMethods()).stream().forEach(e -> {
			String nName = e.getName();
			ReadValue ano = e.getAnnotation(ReadValue.class);
			String targetField = checkField(cName, ignoreEmptyCName, ignoreEmptyFName, nName, ano);

			if (!Util.isEmptyOrNull(targetField))
				try {
					e.setAccessible(true);
					putJsonValue(element, ignoreNull, valueConvertor, result, targetField, e.invoke(element));
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e1) {
				}
		});
		return result;
	}

	private static String checkField(String cName, boolean ignoreEmptyCName, boolean ignoreEmptyFName, String nName,
			ReadValue ano) {
		if (ano == null)
			return null;
		String[] v = ano.value();
		if (v.length == 1 && v[0].equals("")) {// 没有写注解内容，使用的默认值
			if (ignoreEmptyCName && ignoreEmptyFName) {// 如果忽略容器名和字段名
				return nName;
			} else {
				return null;
			}
		} else {
			for (int i = 0; i < v.length; i++) {
				String[] loc = ((String[]) v)[i].split("#");
				if (loc.length == 1 && ignoreEmptyCName) {// 注解的是fName// 如果忽略容器名
					return loc[0].trim();
				} else if (loc.length > 1 && cName.equals(loc[0].trim())) {// 容器名称匹配
					return loc[1].trim();
				}
			}
		}
		return null;
	}

	private static void putJsonValue(Object element, boolean ignoreNull,
			BiFunction<String, Object, Object> valueConvertor, JsonObject result, String targetField, Object value) {
		if (valueConvertor != null)
			value = valueConvertor.apply(targetField, value);
		if (value == null && !ignoreNull) {
			result.add(targetField, JsonValue.NULL);
		} else if (value instanceof JsonValue) {
			result.add(targetField, (JsonValue) value);
		} else if (value instanceof String) {
			result.add(targetField, (String) value);
		} else if (value instanceof Integer) {
			result.add(targetField, (Integer) value);
		} else if (value instanceof Boolean) {
			result.add(targetField, (Boolean) value);
		} else if (value instanceof Double) {
			result.add(targetField, (Double) value);
		} else if (value instanceof Float) {
			result.add(targetField, (Float) value);
		} else if (value instanceof Long) {
			result.add(targetField, (Long) value);
		} else {
			throw new IllegalArgumentException("不支持的类型");
		}
	}

}
