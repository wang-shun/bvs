package com.bizivisionsoft.widgets.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ClientFileLoader;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

public class WidgetToolkit {

	public static void setAttribute(Widget widget, String attr, String value) {
		if (widget == null || widget.isDisposed()) {
			return;
		}
		String $el = widget instanceof Text ? "$input" : "$el";
		String id = WidgetUtil.getId(widget);
		execJS("rap.getObject( '", id, "' ).", $el, ".attr( '", attr, "', '", value + "' );");
	}

	public static void execJS(String... strings) {
		StringBuilder builder = new StringBuilder();
		builder.append("try{");
		for (String str : strings) {
			builder.append(str);
		}
		builder.append("}catch(e){console.log(e);}");
		JavaScriptExecutor executor = RWT.getClient().getService(JavaScriptExecutor.class);
		executor.execute(builder.toString());
	}

	public static void requireWidgetHandlerJs(String widgetName) {
		requireWidgetHandlerJs(widgetName, "handler.js");
	}

	public static void requireWidgetHandlerJs(String widgetName, String jsName) {
		ClientFileLoader jsLoader = RWT.getClient().getService(ClientFileLoader.class);
		String uri = getSiteRoot();
		jsLoader.requireJs(uri + "bvs/widgets/" + widgetName + "/js/" + jsName);
	}

	public static void requireWidgetJs(String widgetName, String path) {
		ClientFileLoader jsLoader = RWT.getClient().getService(ClientFileLoader.class);
		String uri = getSiteRoot();
		jsLoader.requireJs(uri + "bvs/widgets/" + widgetName + "/" + path);
	}

	public static void requireWidgetCss(String widgetName, String path) {
		ClientFileLoader jsLoader = RWT.getClient().getService(ClientFileLoader.class);
		String uri = getSiteRoot();
		jsLoader.requireCss(uri + "bvs/widgets/" + widgetName + "/" + path);
	}

	public static String getSiteRoot() {
		String uri = RWT.getRequest().getRequestURI();
		if (uri.endsWith("/")) {
			return uri;
		} else {
			return uri.substring(0, uri.lastIndexOf("/")) + "/";
		}
	}

	public static String getSiteHttpRoot() {
		HttpServletRequest request = RWT.getRequest();
		String root;
		if (request.getRequestURL().toString().toLowerCase().startsWith("https://")) {
			root = "https://";
		} else {
			root = "http://";
		}
		root += request.getLocalAddr();
		root += ":" + request.getLocalPort();
		root += getSiteRoot();
		return root;
	}

	public static String getRandomString(int length, boolean caseIgnore) {

		Random randGen = null;
		char[] numbersAndLetters = null;
		Object initLock = new Object();

		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					if (caseIgnore) {
						numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz") //$NON-NLS-1$
								.toCharArray();
					} else {
						numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" //$NON-NLS-1$
								+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ") //$NON-NLS-1$
										.toCharArray();
					}
				}
			}
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(numbersAndLetters.length - 1)];
		}
		return new String(randBuffer);
	}

	public static <T> Map<String, Object> write(T element, JsonObject jo, String cName, String... ignoreFields) {
		final Map<String, Object> result = new HashMap<String, Object>();
		// 处理字段
		Arrays.asList(element.getClass().getDeclaredFields()).stream().forEach(e -> {
			String nName = e.getName();
			WriteValue ano = e.getAnnotation(WriteValue.class);
			String targetField = checkField(cName, true, true, nName, ano);
			if (!isEmptyOrNull(targetField) && !inArray(targetField, ignoreFields)) {
				try {
					JsonValue jv = jo.get(targetField);
					// TODO work的chargerId和assignerId，退回后是null，因此取消了原有的jv!=null的判断
					Object value = getValueFromJson(element, jv, targetField, e.getType());
					e.setAccessible(true);
					Object oldValue = e.get(element);
					if (!equals(value, oldValue)) {
						e.set(element, value);
						result.put(targetField, value);
					}
				} catch (IllegalArgumentException | IllegalAccessException e1) {
				}
			}
		});
		// 处理方法
		Arrays.asList(element.getClass().getDeclaredMethods()).stream().forEach(e -> {
			String nName = e.getName();
			WriteValue ano = e.getAnnotation(WriteValue.class);
			String targetField = checkField(cName, true, true, nName, ano);

			if (!isEmptyOrNull(targetField) && !inArray(targetField, ignoreFields)) {
				try {
					JsonValue jv = jo.get(targetField);
					if (jv != null) {
						// 简化处理，仅一个参数,根据该方法的返回判断是否更改了
						Object value = getValueFromJson(element, jv, targetField, e.getParameters()[0].getType());
						e.setAccessible(true);
						Object res = e.invoke(element, value);
						if (!Boolean.FALSE.equals(res)) {
							result.put(targetField, value);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e1) {
				}
			}
		});
		return result;
	}

	private static boolean inArray(Object elem, Object[] arr) {
		return Optional.ofNullable(arr).map(i -> Arrays.asList(i)).map(a -> a.contains(elem)).orElse(false);
	}

	private static boolean equals(Object v1, Object v2) {
		return v1 != null && v1.equals(v2) || v1 == null && v2 == null;
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
	public static JsonObject read(Class<?> clazz, Object element, String cName, boolean ignoreEmptyCName,
			boolean ignoreEmptyFName, boolean ignoreNull, BiFunction<String, Object, Object> valueConvertor) {

		JsonObject result = new JsonObject();
		// 检查
		if (isEmptyOrNull(cName))
			throw new IllegalArgumentException("容器名称为空");
		if (element == null)
			throw new IllegalArgumentException("目标对象为空");
		if (!clazz.isAssignableFrom(element.getClass()))
			throw new IllegalArgumentException("目标对象必须是指定类（或继承）的实例");
		// 处理字段
		Arrays.asList(clazz.getDeclaredFields()).stream().forEach(e -> {
			String nName = e.getName();
			ReadValue ano = e.getAnnotation(ReadValue.class);
			String targetField = checkField(cName, ignoreEmptyCName, ignoreEmptyFName, nName, ano);
			if (!isEmptyOrNull(targetField))
				try {
					e.setAccessible(true);
					putJsonValue(result, targetField, e.get(element), ignoreNull, valueConvertor);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
				}
		});
		// 处理方法
		Arrays.asList(clazz.getDeclaredMethods()).stream().forEach(e -> {
			String nName = e.getName();
			ReadValue ano = e.getAnnotation(ReadValue.class);
			String targetField = checkField(cName, ignoreEmptyCName, ignoreEmptyFName, nName, ano);

			if (!isEmptyOrNull(targetField))
				try {
					e.setAccessible(true);
					putJsonValue(result, targetField, e.invoke(element), ignoreNull, valueConvertor);
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
				String[] loc = ((String[]) v)[i].split("/");
				if (loc.length == 1 && ignoreEmptyCName) {// 注解的是fName// 如果忽略容器名
					return loc[0].trim();
				} else if (loc.length > 1 && cName.equals(loc[0].trim())) {// 容器名称匹配
					return loc[1].trim();
				}
			}
		}
		return null;
	}

	private static String checkField(String cName, boolean ignoreEmptyCName, boolean ignoreEmptyFName, String nName,
			WriteValue ano) {
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
				String[] loc = ((String[]) v)[i].split("/");
				if (loc.length == 1 && ignoreEmptyCName) {// 注解的是fName// 如果忽略容器名
					return loc[0].trim();
				} else if (loc.length > 1 && cName.equals(loc[0].trim())) {// 容器名称匹配
					return loc[1].trim();
				}
			}
		}
		return null;
	}

	public static void putJsonValue(JsonObject result, String targetField, Object value, boolean ignoreNull,
			BiFunction<String, Object, Object> valueConvertor) {
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

	private static Object getValueFromJson(Object element, JsonValue jv, String targetField, Class<?> type) {
		// 因取消了调用方法前取消的jv!=null的判断，因此在此处判断jv==null时，返回null
		if (jv == null || jv.isNull()) {
			return null;
		}

		String typeName = type.getName();

		if (("java.lang.Object".equals(typeName) || "java.lang.String".equals(typeName)) && jv.isString())
			return jv.asString();
		if (("java.lang.Object".equals(typeName) || "java.lang.Boolean".equals(typeName) || "boolean".equals(typeName))
				&& jv.isBoolean())
			return jv.asBoolean();
		if (("java.lang.Object".equals(typeName) || "java.lang.Integer".equals(typeName) || "int".equals(typeName))
				&& jv.isNumber())
			return jv.asInt();
		if (("java.lang.Object".equals(typeName) || "java.lang.Long".equals(typeName) || "long".equals(typeName))
				&& jv.isNumber())
			return jv.asLong();
		if (("java.lang.Object".equals(typeName) || "java.lang.Float".equals(typeName) || "float".equals(typeName))
				&& jv.isNumber())
			return jv.asFloat();
		if (("java.lang.Object".equals(typeName) || "java.lang.Double".equals(typeName) || "double".equals(typeName))
				&& jv.isNumber())
			return jv.asDouble();
		throw new IllegalArgumentException(targetField + "不支持的类型" + typeName + ", value:" + jv);
	}

	public static boolean isEmptyOrNull(String s) {
		return s == null || s.isEmpty();
	}

	public static boolean isEmptyOrNull(List<?> s) {
		return s == null || s.isEmpty();
	}

	public static String escapeHtml(String text) {
		return StringEscapeUtils.escapeHtml4(text);
	}

}
