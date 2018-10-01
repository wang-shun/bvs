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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriter;
import org.eclipse.rap.rwt.RWT;
import org.htmlparser.Parser;
import org.htmlparser.visitors.TextExtractingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.mongocodex.codec.CodexProvider;
import com.bizvisionsoft.service.tools.Util;
import com.mongodb.BasicDBObject;

public class EngUtil {

	public static Logger logger = LoggerFactory.getLogger(EngUtil.class);

	private static final String MONEY_NUMBER_FORMAT = "#,##0.0";

	private static final String TEMP_DIRECTORY_PREFIX = "BVS_";

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
					logger.error(e.getMessage(), e);
				} finally {
					try {
						if (br != null) {
							br.close();
						}
					} catch (IOException ce) {
						logger.error(ce.getMessage(), ce);
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
	public static String getAlphaString(String src) {
		if (src == null) {
			return "";
		}
		String result = ""; //$NON-NLS-1$
		int i;
		try {
			for (i = 0; i < src.length(); i++) {
				result += char2Alpha(src.charAt(i));
			}
		} catch (Exception e) {
			result = ""; //$NON-NLS-1$
		}
		return result;
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
			return ' ';
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

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * 
	 * @param handleResponse
	 *            处理返回结果
	 * @return URL所代表远程资源的响应结果
	 * @throws IOException
	 */
	public static void httpGet(String url, String param,
			BiConsumer<Map<String, List<String>>, InputStream> handleResponse) throws IOException {
		String urlNameString = url + "?" + param;
		// 打开和URL之间的连接
		URLConnection connection = new URL(urlNameString).openConnection();
		// 设置通用的请求属性
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 建立实际的连接
		connection.connect();

		if (handleResponse != null) {
			Map<String, List<String>> map = connection.getHeaderFields();
			InputStream is = connection.getInputStream();
			handleResponse.accept(map, is);
			if (is != null)
				is.close();
		}
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL所代表远程资源的响应结果
	 * @throws IOException
	 */
	public static void httpPost(String url, String param,
			BiConsumer<Map<String, List<String>>, InputStream> handleResponse) throws IOException {
		PrintWriter out = null;
		// 打开和URL之间的连接
		URLConnection conn = new URL(url).openConnection();
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(param);
		// flush输出流的缓冲
		out.flush();

		if (handleResponse != null) {
			Map<String, List<String>> map = conn.getHeaderFields();
			InputStream is = conn.getInputStream();
			handleResponse.accept(map, is);
			if (is != null)
				is.close();
		}
	}

	public static File createTempDirectory() throws IOException {
		File result = File.createTempFile(TEMP_DIRECTORY_PREFIX,
				"_" + RWT.getRequest().getSession().getId().toUpperCase());
		result.delete();
		if (result.mkdir()) {
			result.deleteOnExit();
		} else {
			throw new IOException("无法创建临时文件夹 " + result.getAbsolutePath());
		}
		return result;
	}

	/**
	 * HTML解析,使用HtmlParser解析HTML
	 * 
	 * @param html
	 *            需解析的文本
	 * @return
	 * @throws IOException
	 */
	public static String parserHtml2Text(String html) throws Exception {
		// 构建HtmlParser解析器，传入的String不是以HTML标记开头时，Parser认为是从文件夹中的文件中获取。
		Parser parser = new Parser("<div>" + html + "</div>");
		// 构建Text遍历器,TextExtractingVisitor将遍历html中所有的标记，并获取html标记中
		TextExtractingVisitor textVisitor = new TextExtractingVisitor();
		parser.visitAllNodesWith(textVisitor);
		return textVisitor.getExtractedText();
	}
}
