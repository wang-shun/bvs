package com.bizivisionsoft.widgets.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ClientFileLoader;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bizvisionsoft.annotations.md.service.ReadValue;

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
		ClientFileLoader jsLoader = RWT.getClient().getService(ClientFileLoader.class);
		String uri = getSiteRoot();
		jsLoader.requireJs(uri + "bvs/widgets/" + widgetName + "/js/handler.js");
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
		if (isEmptyOrNull(cName))
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
			if (!isEmptyOrNull(targetField))
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

			if (!isEmptyOrNull(targetField))
				try {
					e.setAccessible(true);
					putJsonValue(element, ignoreNull, valueConvertor, result, targetField, e.invoke(element));
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e1) {
				}
		});
		
		result.add("$hashCode", element.hashCode());
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


	public static boolean isEmptyOrNull(String s) {
		return s == null || s.isEmpty();
	}

	public static boolean isEmptyOrNull(List<?> s) {
		return s == null || s.isEmpty();
	}

}
