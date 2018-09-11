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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriter;
import org.eclipse.rap.rwt.RWT;

import com.bizvisionsoft.mongocodex.codec.CodexProvider;
import com.bizvisionsoft.service.tools.Util;
import com.mongodb.BasicDBObject;

public class EngUtil {

	private static final String MONEY_NUMBER_FORMAT = "#,##0.0";

	private static char[] array = "0123456789ABCDEFGHJKMNPQRSTUVWXYZ".toCharArray();

	/**
	 * 十进制转其他特殊进制
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

	public static String getFormatText(Object object) {
		return Util.getFormatText(object, null, RWT.getLocale());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BasicDBObject getBson(Object input, boolean ignoreNull, String[] containFields,
			String[] ignoreFields) {
		Codec codec = CodexProvider.getRegistry().get(input.getClass());
		StringWriter sw = new StringWriter();
		codec.encode(new JsonWriter(sw), input, EncoderContext.builder().build());
		String json = sw.toString();
		BasicDBObject result = BasicDBObject.parse(json);

		BasicDBObject _result = new BasicDBObject();
		Iterator<String> iter = result.keySet().iterator();
		while (iter.hasNext()) {
			String k = iter.next();
			if (ignoreFields != null && Arrays.asList(ignoreFields).contains(k)) {
				continue;
			}
			Object v = result.get(k);
			if (v == null && ignoreNull && (containFields == null
					|| (containFields != null && !Arrays.asList(containFields).contains(k)))) {
				continue;
			}
			_result.append(k, v);
		}
		return _result;
	}

	public static BasicDBObject getBson(Object input, String... ignoreFields) {
		return getBson(input, true, null, ignoreFields);
	}
	
	public Object get_id(Object m) {
		return getBson(m).get("_id");
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
			GZIPOutputStream os = null; // 使用默认缓冲区大小创建新的输出流
			byte[] bs = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new GZIPOutputStream(bos);
				os.write(str.getBytes()); // 写入输出流
				os.close();
				bos.close();
				bs = bos.toByteArray();
				return new String(bs, "ISO-8859-1"); // 通过解码字节将缓冲区内容转换为字符串
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
	 * 解压缩字符串
	 * 
	 * @param str
	 *            解压缩的字符串
	 * @return 解压后的字符串
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
			is = new GZIPInputStream(bis); // 使用默认缓冲区大小创建新的输入流
			buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) { // 将未压缩数据读入字节数组
				// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此byte数组输出流
				bos.write(buf, 0, len);
			}
			is.close();
			bis.close();
			bos.close();
			return new String(bos.toByteArray()); // 通过解码字节将缓冲区内容转换为字符串
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

	public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";

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

	public static boolean isEmptyOrNull(String s) {
		return s == null || s.isEmpty();
	}

	public static boolean isEmptyOrNull(List<?> s) {
		return s == null || s.isEmpty();
	}

	/**
	 * 
	 * @param <T>
	 * @param source
	 *            要分割的数组
	 * @param subSize
	 *            分割的块大小
	 * @return
	 *
	 */
	public static <T> List<List<T>> splitArray(List<T> source, int subSize) {
		List<List<T>> subAryList = new ArrayList<List<T>>();
		int count = subSize == 0 ? 0
				: (source.size() % subSize == 0 ? source.size() / subSize : source.size() / subSize + 1);
		for (int i = 0; i < count; i++) {
			int index = i * subSize;
			List<T> list = new ArrayList<T>();
			int j = 0;
			while (j < subSize && index < source.size()) {
				list.add(source.get(index++));
				j++;
			}
			subAryList.add(list);
		}

		return subAryList;
	}

	@SuppressWarnings("unchecked")
	public static <T> void ifInstanceThen(Object obj, Class<T> clazz, Consumer<T> consumer) {
		if (clazz.isAssignableFrom(obj.getClass())) {
			consumer.accept((T) obj);
		}
	}

	public static String getGenericMoneyFormatText(Double budget) {
		if (budget == null || budget == 0d) {
			return "";
		}
		return Util.getFormatText(budget, MONEY_NUMBER_FORMAT, RWT.getLocale());
	}

	public static String getHTMLDarkColor(Object seed) {
		return BruiColors.getHtmlColor(BruiColors.deepColor[seed.hashCode() % BruiColors.deepColor.length].getRgb());
	}

	// i, u, v都不做声母, 跟随前面的字母

	private static char[] alphatable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',

			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	// private static char[] alphatable = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
	// 'h', 'i',
	//
	// 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
	// 'x', 'y', 'z' };

	// 初始化
	private static int[] alphatable_code = { 45217, 45253, 45761, 46318, 46826, 47010, 47297, 47614, 47614, 48119,
			49062, 49324, 49896, 50371, 50614, 50622, 50906, 51387, 51446, 52218, 52218, 52218, 52698, 52980, 53689,
			54481, 55289 };

	/**
	 * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
	 * 
	 * @param String
	 *            SourceStr 包含一个汉字的字符串
	 */
	public static String getAlphaString(String SourceStr) {

		String Result = ""; //$NON-NLS-1$
		int StrLength = SourceStr.length();
		int i;
		try {
			for (i = 0; i < StrLength; i++) {
				Result += char2Alpha(SourceStr.charAt(i));
			}
		} catch (Exception e) {
			Result = ""; //$NON-NLS-1$
		}
		return Result;
	}

	/**
	 * 主函数,输入字符,得到他的声母, 英文字母返回对应的字母 其他非简体汉字返回 '0'
	 * 
	 * @param char
	 *            ch 汉字拼音首字母的字符
	 */
	public static char char2Alpha(char ch) {

		if (ch >= 'a' && ch <= 'z')
			// return (char) (ch - 'a' + 'A');
			return ch;
		if (ch >= 'A' && ch <= 'Z')
			return ch;
		if (ch >= '0' && ch <= '9')
			return ch;

		int gb = getCodeValue(ch, "GB2312"); //$NON-NLS-1$
		if (gb < alphatable_code[0])
			return '0';

		int i;
		for (i = 0; i < 26; ++i) {
			if (alphaCodeMatch(i, gb))
				break;
		}

		if (i >= 26)
			return '0';
		else
			return alphatable[i];
	}

	/**
	 * 判断字符是否于table数组中的字符想匹配
	 * 
	 * @param i
	 *            table数组中的位置
	 * @param gb
	 *            中文编码
	 * @return
	 */
	private static boolean alphaCodeMatch(int i, int gb) {

		if (gb < alphatable_code[i])
			return false;

		int j = i + 1;

		// 字母Z使用了两个标签
		while (j < 26 && (alphatable_code[j] == alphatable_code[i]))
			++j;

		if (j == 26)
			return gb <= alphatable_code[j];
		else
			return gb < alphatable_code[j];

	}

	/**
	 * 取出汉字的编码
	 * 
	 * @param char
	 *            ch 汉字拼音首字母的字符
	 */
	private static int getCodeValue(char ch, String charsetName) {

		String str = new String();
		str += ch;
		try {
			byte[] bytes = str.getBytes(charsetName);
			if (bytes.length < 2)
				return 0;
			return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double getDoubleInput(String input) {
		double inputAmount;
		try {
			if ("".equals(input)) {
				inputAmount = 0;
			} else {
				inputAmount = Double.parseDouble(input.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException("需要输入合法数字");
		}
		return inputAmount;
	}

}
