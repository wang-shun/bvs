package com.bizvisionsoft.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadOptions;
import com.bizvisionsoft.annotations.md.service.ReadValidation;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.Structure;
import com.bizvisionsoft.annotations.md.service.WriteValue;
import com.google.gson.GsonBuilder;

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
				throw new RuntimeException(
						"������" + cName + "���ֶΣ�" + fName + "��ע�⣺" + annoClass.getSimpleName() + "���ĵ��ô���",
						e.getTargetException());
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
				throw new RuntimeException(e.getTargetException().getMessage());
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
			throw new RuntimeException("ע��Ϊ" + annoClass + "����Ŀ�����", e2);
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

}
