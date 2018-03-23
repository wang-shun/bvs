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
			} catch (IllegalAccessException | IllegalArgumentException e) {// ���ʴ��󣬲�����������û�ж���÷�����
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
				throw new RuntimeException("ע��Ϊ" + methodAnnotation + "����Ŀ�����", e1);
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
			throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶλ򷽷��޷����ʡ�", e);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ�����������", e1);
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
				// throw new BruiEngineError("û��ע��Ϊ" + annoClass + "���ֶλ򷽷���");
				return null;
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶλ򷽷��޷����ʡ�", e);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ�����������", e1);
		} catch (InvocationTargetException e2) {
			throw new RuntimeException("ע��Ϊ" + annoClass + "����Ŀ�����", e2);
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
					} catch (IllegalAccessException e) {
						throw new RuntimeException("ע��Ϊ" + Inject.class + "���ֶλ򷽷��޷����ʡ�", e);
					} catch (IllegalArgumentException e1) {
						throw new RuntimeException("ע��Ϊ" + Inject.class + "���ֶλ򷽷���������", e1);
					}
				}
			}
		});
		return this;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// ����Structure ���
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
	 * ��ȡ����ע��Ϊ ReadValue��
	 * ֵ���ȶ��ֶΣ�����÷�����û�ж�Ӧ�ֶλ򷽷��������ֶ����͡�������������ʱ������������Ӧ��ʱ������defaultValue
	 * ע����ʽΪReadValue("������ # �ֶ���")
	 * 
	 * @param element
	 *            Ŀ�����
	 * @param cName
	 *            ReadValueע��� ������
	 * @param fName
	 *            ע��� �ֶ���
	 * @param defaultValue
	 *            ��ƥ��ʱ���ص�Ĭ��ֵ
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
				throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶ��޷����ʡ�", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶβ�������", e);
			}

		Method m = getContainerMethod(c, annoClass, cName, fName, func).orElse(null);
		if (m != null)
			try {
				m.setAccessible(true);
				return m.invoke(element);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ����޷����ʡ�", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ�����������", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "����Ŀ�����", e);
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
				throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶ��޷����ʡ�", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "���ֶβ�������", e);
			}

		Method m = getContainerMethod(c, annoClass, cName, fName, func).orElse(null);
		if (m != null)
			try {
				m.setAccessible(true);
				m.invoke(element, value);
				return;
			} catch (IllegalAccessException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ����޷����ʡ�", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "�ķ�����������", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("ע��Ϊ" + annoClass + "����Ŀ�����", e);
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
	 *            Ŀ����
	 * @param annoClass
	 *            ע����
	 * @param annoValue
	 *            ע��ֵ
	 * @param valueFunction
	 *            ע��ȡֵ����
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
			if (anno == null) {// û��ע��
				// �����ֶ�����ƥ��� ������
				if ((anno == null) && withNameMatch && f.getName().equals(annoValue)) {
					return true;
				} else {// ���򶼲�ƥ��
					return false;
				}
			} else {// ��ע��
				if (valueFunction == null) {// δָ��ȡֵ�����ģ�ע��ƥ��Ϳ���
					return true;
				} else {
					// ����ȡֵ������ȡע��ֵ
					Object value = valueFunction.apply(anno);
					if (value instanceof Object[]) {// ���ע��ֵ������ģ���һԪ��ƥ��Ϳ�����
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
	 *            Ŀ����
	 * @param annoClass
	 *            ע����
	 * @param annoValue
	 *            ע��ֵ
	 * @param valueFunction
	 *            ע��ȡֵ����
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
			if (anno == null) {// û��ע��
				// �����ֶ�����ƥ��� ������
				if ((anno == null) && withNameMatch && f.getName().equals(annoValue)) {
					return true;
				} else {// ���򶼲�ƥ��
					return false;
				}
			} else {// ��ע��
				if (valueFunction == null) {// δָ��ȡֵ�����ģ�ע��ƥ��Ϳ���
					return true;
				} else {
					// ����ȡֵ������ȡע��ֵ
					Object value = valueFunction.apply(anno);
					if (value instanceof Object[]) {// ���ע��ֵ������ģ���һԪ��ƥ��Ϳ�����
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
	 * ����ע��ƥ�䣬��ʽ��:</br>
	 * ע��({����1 # �ֶ�1 , ����2 # �ֶ�2})</br>
	 * ע��({�ֶ�1 , ����2 # �ֶ�2})</br>
	 * ע��(�ֶ�)</br>
	 * ע�⣨��ʱƥ��fieldOrMethodName��fName��ͬ��</br>
	 * ���ע���в�������������ע�����ʾ�ڸ���������ͨ��
	 * 
	 * @param ann
	 *            ע��
	 * @param fieldOrMethodName
	 *            ��ע����ֶ����򷽷���,���Կ�
	 * @param cName
	 *            ע��������,����null
	 * @param fName
	 *            ע���ֶ���,����Ϊnull
	 * @param value
	 *            ע��ȡֵ���� ����Ϊnull ȡֵ�������뷵�� String[]
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
	 * ���ƶ��������,ֻ�����ֶ�ֵ
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
	 * ��������,�����������ƶ�ȡJson�ַ��� ��֧���������ͣ�����
	 * 
	 * @param <R>
	 * @param <T>
	 * 
	 * @param clazz,
	 *            ����ȡ������ࣨ����ע��ģ�������ɼ̳��ڸ���
	 * @param element,����ȡ�Ķ���
	 * @param cName
	 *            ��������
	 * @param ignoreEmptyCName
	 *            �Ƿ�������������������������������ReadValue ��ע��Ķ�������ȡ
	 * @param ignoreEmptyFName
	 *            �Ƿ�����ֶ�������������ֶ���������������ֶ����ƶ�ȡ
	 * @param valueConvertor
	 *            ����ת������
	 * @return
	 */
	public static JsonObject readJsonFrom(Class<?> clazz, Object element, String cName, boolean ignoreEmptyCName,
			boolean ignoreEmptyFName, boolean ignoreNull, BiFunction<String, Object, Object> valueConvertor) {

		JsonObject result = new JsonObject();
		// ���
		if (Util.isEmptyOrNull(cName))
			throw new IllegalArgumentException("��������Ϊ��");
		if (element == null)
			throw new IllegalArgumentException("Ŀ�����Ϊ��");
		if (!clazz.isAssignableFrom(element.getClass()))
			throw new IllegalArgumentException("Ŀ�������ָ���ࣨ��̳У���ʵ��");
		// �����ֶ�
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
		// ������
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
		if (v.length == 1 && v[0].equals("")) {// û��дע�����ݣ�ʹ�õ�Ĭ��ֵ
			if (ignoreEmptyCName && ignoreEmptyFName) {// ����������������ֶ���
				return nName;
			} else {
				return null;
			}
		} else {
			for (int i = 0; i < v.length; i++) {
				String[] loc = ((String[]) v)[i].split("#");
				if (loc.length == 1 && ignoreEmptyCName) {// ע�����fName// �������������
					return loc[0].trim();
				} else if (loc.length > 1 && cName.equals(loc[0].trim())) {// ��������ƥ��
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
			throw new IllegalArgumentException("��֧�ֵ�����");
		}
	}

}
