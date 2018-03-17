package com.bizvisionsoft.bruiengine.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriter;

import com.bizvisionsoft.mongocodex.codec.CodexProvider;
import com.mongodb.BasicDBObject;

public class Util {

	private static char[] array = "0123456789ABCDEFGHJKMNPQRSTUVWXYZ".toCharArray();

	/**
	 * ʮ����ת�����������
	 * 
	 * @param number
	 * @param N
	 * @return
	 */
	public static String _10_to_N(long number, int N) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			stack.add(array[new Long((rest % N)).intValue()]);
			rest = rest / N;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		return result.length() == 0 ? "0" : result.toString();
	}

	private static HashMap<String, Integer> nameNumber = new HashMap<String, Integer>();

	public static String generateName(String text, String key) {
		Integer number = nameNumber.get(key);
		if (number == null) {
			number = 1;
		}
		nameNumber.put(key, number + 1);
		return text + number;
	}

	public static String generateName(String text) {
		return generateName(text, text);
	}

	public static String getFormatText(Object value, String format, Locale locale) {
		String text;
		if (value instanceof Date) {
			text = Optional.ofNullable(format)//
					.map(f -> {
						return new SimpleDateFormat(f, locale).format(value);
					})
					.orElse(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale).format(value));
		} else if (value instanceof Integer || value instanceof Long || value instanceof Short) {
			text = Optional.ofNullable(format)//
					.map(f -> {
						return new DecimalFormat(f).format(value);
					}).orElse(value.toString());
		} else if (value instanceof Float || value instanceof Double) {
			text = Optional.ofNullable(format)//
					.map(f -> {
						return new DecimalFormat(f).format(value);
					}).orElse(value.toString());
		} else if (value instanceof Boolean) {
			text = (boolean) value ? "��" : "��";
		} else if (value != null) {
			text = value.toString();
		} else {
			text = "";
		}
		return text;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BasicDBObject getBson(Object input, boolean ignoreNull, boolean wrapList) {
		Codec codec = CodexProvider.getRegistry().get(input.getClass());
		StringWriter sw = new StringWriter();
		codec.encode(new JsonWriter(sw), input, EncoderContext.builder().build());
		String json = sw.toString();
		BasicDBObject result = BasicDBObject.parse(json);

		BasicDBObject _result = new BasicDBObject();
		Iterator<String> iter = result.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			Object v = result.get(k);
			if (ignoreNull && v == null) {
				continue;
			}
			if (wrapList && v instanceof List<?>) {
				_result.append("$and", getList((List<Object>) v, i -> new BasicDBObject(k, i)));
			} else {
				_result.append(k, v);
			}
		}
		return _result;
	}

	public static <T, R> List<R> getList(List<T> source, Function<T, R> func) {
		ArrayList<R> result = new ArrayList<R>();
		source.forEach(item -> result.add(func.apply(item)));
		return result;
	}

	public static <T, R> List<R> getList(T[] source, Function<T, R> func) {
		return getList(Arrays.asList(source), func);
	}

	public static String compress(String str) {

		if (str.isEmpty()) {
			return str;
		}

		try {
			ByteArrayOutputStream bos = null;
			GZIPOutputStream os = null; // ʹ��Ĭ�ϻ�������С�����µ������
			byte[] bs = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new GZIPOutputStream(bos);
				os.write(str.getBytes()); // д�������
				os.close();
				bos.close();
				bs = bos.toByteArray();
				return new String(bs, "ISO-8859-1"); // ͨ�������ֽڽ�����������ת��Ϊ�ַ���
			} finally {
				bs = null;
				bos = null;
				os = null;
			}
		} catch (Exception ex) {
			return str;
		}
	}

	/**
	 * ��ѹ���ַ���
	 * 
	 * @param str
	 *            ��ѹ�����ַ���
	 * @return ��ѹ����ַ���
	 */
	public static String decompress(String str) {
		if (str.isEmpty()) {
			return str;
		}

		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPInputStream is = null;
		byte[] buf = null;
		try {
			bis = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			bos = new ByteArrayOutputStream();
			is = new GZIPInputStream(bis); // ʹ��Ĭ�ϻ�������С�����µ�������
			buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) { // ��δѹ�����ݶ����ֽ�����
				// ��ָ�� byte �����д�ƫ���� off ��ʼ�� len ���ֽ�д���byte���������
				bos.write(buf, 0, len);
			}
			is.close();
			bis.close();
			bos.close();
			return new String(bos.toByteArray()); // ͨ�������ֽڽ�����������ת��Ϊ�ַ���
		} catch (Exception ex) {
			return str;
		} finally {
			bis = null;
			bos = null;
			is = null;
			buf = null;
		}
	}

	public static void copyStream(InputStream inputStream, OutputStream outputStream, boolean closeOutputWhenFinish)
			throws IOException {
		try {
			byte[] buffer = new byte[8192];
			boolean finished = false;
			while (!finished) {
				int bytesRead = inputStream.read(buffer);
				if (bytesRead != -1) {
					outputStream.write(buffer, 0, bytesRead);
				} else {
					finished = true;
				}
			}
		} finally {
			if (closeOutputWhenFinish)
				outputStream.close();
		}
	}

	static final String DEFAULT_CONTENT_TYPE_FILE_NAME = "content-type.tmp";

	public static String getContentType(File uploadedFile, String defaultType) {
		String contentType = null;
		BufferedReader br = null;
		if (uploadedFile.exists()) {
			File cTypeFile = new File(uploadedFile.getParentFile(), DEFAULT_CONTENT_TYPE_FILE_NAME);
			if (cTypeFile.exists()) {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(cTypeFile)));
					contentType = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (br != null) {
							br.close();
						}
					} catch (IOException ce) {
						ce.printStackTrace();
					}
				}
			}
		}
		return contentType == null ? (defaultType == null ? "application/octet-stream" : defaultType) : contentType;
	}
}
