package com.bizvisionsoft.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.bson.Document;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.ImageURL;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadEditorConfig;
import com.bizvisionsoft.annotations.md.service.ReadOptions;
import com.bizvisionsoft.annotations.md.service.ReadValidation;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.google.gson.GsonBuilder;
import com.mongodb.DBObject;

public class AUtil {

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
	 * ע����ʽΪReadValue("������/�ֶ���")
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

	public static Object readImageUrl(Object element, String cName, String fName, Object defaultValue) {
		if (element instanceof Map<?, ?>) {
			return ((Map<?, ?>) element).get(fName + ".img");
		} else {
			return read(element.getClass(), ImageURL.class, element, cName, fName, defaultValue, a -> a.value());
		}
	}

	@SuppressWarnings("unchecked")
	public static void writeValue(Object element, String cName, String fName, Object value) {
		if (element instanceof Document) {
			((Document) element).append(fName, value);
		} else if (element instanceof DBObject) {
			((DBObject) element).put(fName, value);
		} else if (element instanceof Map<?, ?>) {
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
				throw createTargetException(e,
						"������" + cName + "���ֶΣ�" + fName + "��ע�⣺" + annoClass.getSimpleName() + "���ĵ��ô���");
			}

		return defaultValue;
	}

	public static <T extends Annotation> void write(Class<?> c, Class<T> annoClass, Object element, String cName,
			String fName, Object value, Function<T, String[]> func) {
		Field f = getContainerField(c, annoClass, cName, fName, func).orElse(null);
		if (f != null)
			try {
				f.setAccessible(true);
				value = getTypedValue(value, f.getType());
				f.set(element, value);
				return;
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
						"������" + cName + "���ֶΣ�" + fName + "��ע�⣺" + annoClass.getSimpleName() + "�����ֶ��޷����ʡ�", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(
						"������" + cName + "���ֶΣ�" + fName + "��ע�⣺" + annoClass.getSimpleName() + "�����ֶβ�������", e);
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
				throw createTargetException(e, e.getTargetException().getMessage());
			}
	}

	private static RuntimeException createTargetException(InvocationTargetException e, String message) {
		Throwable target = e.getTargetException();
		RuntimeException re = new RuntimeException(message, target);
		re.setStackTrace(target.getStackTrace());
		return re;
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
	 * ע��({����1 / �ֶ�1 , ����2 / �ֶ�2})</br>
	 * ע��({�ֶ�1 , ����2 / �ֶ�2})</br>
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
				String[] loc = ((String[]) v)[i].split("/");
				if (loc.length == 1 && fName.equals(loc[0].trim())) {
					return true;
				} else if (loc.length > 1 && loc[0].trim().equals(cName) && loc[1].trim().equals(fName)) {
					return true;
				}
			}
			return false;
		}).orElse(false);
	}

	/**
	 * ���ƶ��������,ֻ�����ֶ�ֵ
	 * 
	 * @param info
	 * @param elem
	 * @return
	 */
	public static Object simpleCopy(Object from, Object to) {
		Arrays.asList(from.getClass().getDeclaredFields()).forEach(srcField -> {
			try {
				Field tgtField = to.getClass().getDeclaredField(srcField.getName());
				tgtField.setAccessible(true);
				srcField.setAccessible(true);
				Object srcValue = srcField.get(from);
				tgtField.set(to, srcValue);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			}
		});
		return to;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(T source) {
		// TODO deep������� documentʱ������Щ���⣬����ObjectId�ֶα�ת��ΪMap,���Կ���ʹ��BsonProvider�ķ�ʽ����
		String json = new GsonBuilder().create().toJson(source);
		return (T) new GsonBuilder().create().fromJson(json, source.getClass());
	}

	public static <T extends Annotation> Object getValue(Class<?> clazz, Class<T> annoClass, Object target) {
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
			throw createTargetException(e2, "ע��Ϊ" + annoClass + "����Ŀ�����");
		}
	}

	public static String readLabel(Object target, String labelType) {
		try {
			Field field = getField(target.getClass(), Label.class, labelType == null ? "" : labelType, f -> f.value())
					.orElse(null);
			if (field != null) {
				field.setAccessible(true);
				return (String) field.get(target);
			}

			Method method = getMethod(target.getClass(), Label.class, labelType == null ? "" : labelType,
					f -> f.value()).orElse(null);
			if (method != null) {
				method.setAccessible(true);
				return (String) method.invoke(target);
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			// TODO
		}
		return null;
	}

	public static String readLabel(Object target) {
		return readLabel(target, "");
	}

	public static String readType(Object elem) {
		return Optional.ofNullable(readValue(elem, "", ReadValue.TYPE, null)).map(v -> v.toString()).orElse(null);
	}

	public static String readTypeAndLabel(Object elem) {
		String message = null;
		String type = AUtil.readType(elem);
		if (type != null) {
			message = type;
		}
		String label = AUtil.readLabel(elem);
		if (label != null) {
			if (message != null)
				message += ": " + label;
			else
				message = label;
		}
		return message;
	}

	public static <T extends Annotation> Object invokeMethodInjectParams(Object target, Method method,
			Object[] parameterValues, String[] paramemterNames, Class<T> parameterAnnotationClass,
			Function<T, String> howToGetParameterNameFromAnnotation) {
		boolean byCommand = isCommandParameter(method, parameterAnnotationClass, howToGetParameterNameFromAnnotation);
		// �ж��Ƿ�ʹ��command
		if (byCommand) {
			return invokeMethodInjectCommandParams(target, method, parameterValues, paramemterNames);
		} else {
			return invokeMethodInjectNormalParams(target, method, parameterValues, paramemterNames,
					parameterAnnotationClass, howToGetParameterNameFromAnnotation);
		}
	}

	private static <T extends Annotation> Object invokeMethodInjectNormalParams(Object target, Method method,
			Object[] parameterValues, String[] paramemterNames, Class<T> parameterAnnotationClass,
			Function<T, String> howToGetParameterNameFromAnnotation) {
		Object[] args = null;
		Parameter[] para = method.getParameters();
		if (paramemterNames == null) {
			args = parameterValues;
		} else {
			args = new Object[method.getParameterCount()];
			for (int i = 0; i < para.length; i++) {
				T emp = para[i].getAnnotation(parameterAnnotationClass);
				if (emp != null) {
					String paramName = howToGetParameterNameFromAnnotation.apply(emp);
					int idx = -1;
					for (int j = 0; j < paramemterNames.length; j++) {
						if (paramemterNames[j].equals(paramName)) {
							idx = j;
							break;
						}
					}
					if (idx != -1)
						args[i] = parameterValues[idx];
				}
			}
		}
		try {
			method.setAccessible(true);
			return method.invoke(target, args);
		} catch (IllegalAccessException | IllegalArgumentException e) {// ���ʴ��󣬲�����������û�ж���÷�����
		} catch (InvocationTargetException e1) {
			throw createTargetException(e1, e1.getTargetException().getMessage());
		}
		return null;
	}

	private static Object invokeMethodInjectCommandParams(Object target, Method method, Object[] parameterValues,
			String[] paramemterNames) {
		UniversalCommand command = new UniversalCommand();
		for (int i = 0; i < paramemterNames.length; i++) {
			if (UniversalCommand.PARAM_TARGET_CLASS.equals(paramemterNames[i])) {
				command.setTargetClassName((String) parameterValues[i]);
			} else {
				command.addParameter(paramemterNames[i], parameterValues[i]);
			}
		}
		try {
			method.setAccessible(true);
			UniversalResult ur = (UniversalResult) method.invoke(target, new Object[] { command });
			return ur.getValue();
		} catch (IllegalAccessException | IllegalArgumentException e) {// ���ʴ��󣬲�����������û�ж���÷�����
		} catch (InvocationTargetException e1) {
			throw createTargetException(e1, e1.getTargetException().getMessage());
		}
		return null;
	}

	private static <T extends Annotation> boolean isCommandParameter(Method method, Class<T> parameterAnnotationClass,
			Function<T, String> howToGetParameterNameFromAnnotation) {
		Parameter[] para = method.getParameters();
		if (para.length == 1) {
			T emp = para[0].getAnnotation(parameterAnnotationClass);
			if (emp != null && howToGetParameterNameFromAnnotation != null) {
				String paramName = howToGetParameterNameFromAnnotation.apply(emp);
				if (MethodParam.COMMAND.equals(paramName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean readBehavior(Object element, String cName, String fName, Object[] parameterValues,
			String[] paramemterNames) {
		return readBehavior(element, cName, fName, parameterValues, paramemterNames, false);
	}

	public static Boolean readBehavior(Object element, String cName, String fName, Object[] parameterValues,
			String[] paramemterNames, Boolean nullValue) {
		Object v = readValue(element, cName, fName, parameterValues, paramemterNames, Behavior.class, a -> a.value());
		if (v == null) {
			return nullValue;
		} else {
			return Boolean.TRUE.equals(v);
		}
	}

	public static Object readEditorConfig(Object element, String cName, String fName, Object[] parameterValues,
			String[] paramemterNames) {
		return readValue(element, cName, fName, parameterValues, paramemterNames, ReadEditorConfig.class,
				a -> a.value());
	}

	public static <T extends Annotation> Object readValue(Object element, String cName, String fName,
			Object[] parameterValues, String[] paramemterNames, Class<T> aclass,
			Function<T, String[]> howToGetNameFromAnnotation) {
		Field f = getContainerField(element.getClass(), aclass, cName, fName, howToGetNameFromAnnotation).orElse(null);
		if (f != null) {
			f.setAccessible(true);
			try {
				return f.get(element);
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		} else {
			Method m = AUtil.getContainerMethod(element.getClass(), aclass, cName, fName, howToGetNameFromAnnotation)
					.orElse(null);
			if (m != null) {
				return invokeMethodInjectParams(element, m, parameterValues, paramemterNames, MethodParam.class,
						f1 -> f1.value());
			}
		}
		return null;
	}

	private static Object getTypedValue(Object value, Class<?> type) {
		String tName = type.getName();
		if (value != null) {
			if (type.isAssignableFrom(value.getClass())) {
				return value;
			} else if (value.getClass().getSimpleName().equals(tName)) {
				return value;
			}
		}
		try {
			if ("int".equals(tName)) {
				value = convertValue(value, 0, Integer::parseInt);
			} else if ("Integer".equals(tName)) {
				value = convertValue(value, null, Integer::parseInt);
			} else if ("double".equals(tName)) {
				value = convertValue(value, 0d, Double::parseDouble);
			} else if ("Double".equals(tName)) {
				value = convertValue(value, null, Double::parseDouble);
			} else if ("float".equals(tName)) {
				value = convertValue(value, 0f, Float::parseFloat);
			} else if ("Float".equals(tName)) {
				value = convertValue(value, null, Float::parseFloat);
			} else if ("long".equals(tName)) {
				value = convertValue(value, 0l, Long::parseLong);
			} else if ("Long".equals(tName)) {
				value = convertValue(value, null, Long::parseLong);
			} else if ("short".equals(tName)) {
				value = convertValue(value, 0, Short::parseShort);
			} else if ("Short".equals(tName)) {
				value = convertValue(value, null, Short::parseShort);
			} else if ("boolean".equals(tName)) {
				value = convertValue(value, false, Boolean::parseBoolean);
			} else if ("Boolean".equals(tName)) {
				value = convertValue(value, null, Boolean::parseBoolean);
			}
		} catch (Exception e) {
		}
		return value;
	}

	private static Object convertValue(Object value, Object nullValue, Function<String, Object> converter) {
		if (value == null) {
			return nullValue;
		} else {
			String str = value.toString();
			if (str.isEmpty()) {
				return nullValue;
			} else {
				return converter.apply(str);
			}
		}
	}

}
