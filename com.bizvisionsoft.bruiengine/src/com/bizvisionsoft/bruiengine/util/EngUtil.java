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
	 *            Ҫ�ָ������
	 * @param subSize
	 *            �ָ�Ŀ��С
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

	// i, u, v��������ĸ, ����ǰ�����ĸ

	private static char[] alphatable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',

			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	// private static char[] alphatable = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
	// 'h', 'i',
	//
	// 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
	// 'x', 'y', 'z' };

	// ��ʼ��
	private static int[] alphatable_code = { 45217, 45253, 45761, 46318, 46826, 47010, 47297, 47614, 47614, 48119,
			49062, 49324, 49896, 50371, 50614, 50622, 50906, 51387, 51446, 52218, 52218, 52218, 52698, 52980, 53689,
			54481, 55289 };

	/**
	 * ����һ���������ֵ��ַ�������һ������ƴ������ĸ���ַ���
	 * 
	 * @param String
	 *            SourceStr ����һ�����ֵ��ַ���
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
	 * ������,�����ַ�,�õ�������ĸ, Ӣ����ĸ���ض�Ӧ����ĸ �����Ǽ��庺�ַ��� '0'
	 * 
	 * @param char
	 *            ch ����ƴ������ĸ���ַ�
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
	 * �ж��ַ��Ƿ���table�����е��ַ���ƥ��
	 * 
	 * @param i
	 *            table�����е�λ��
	 * @param gb
	 *            ���ı���
	 * @return
	 */
	private static boolean alphaCodeMatch(int i, int gb) {

		if (gb < alphatable_code[i])
			return false;

		int j = i + 1;

		// ��ĸZʹ����������ǩ
		while (j < 26 && (alphatable_code[j] == alphatable_code[i]))
			++j;

		if (j == 26)
			return gb <= alphatable_code[j];
		else
			return gb < alphatable_code[j];

	}

	/**
	 * ȡ�����ֵı���
	 * 
	 * @param char
	 *            ch ����ƴ������ĸ���ַ�
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
			throw new RuntimeException("��Ҫ����Ϸ�����");
		}
		return inputAmount;
	}

	/**
	 * ��ָ��URL����GET����������
	 *
	 * @param url
	 *            ���������URL
	 * @param param
	 *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	 * 
	 * @param handleResponse
	 *            �����ؽ��
	 * @return URL������Զ����Դ����Ӧ���
	 * @throws IOException
	 */
	public static void httpGet(String url, String param,
			BiConsumer<Map<String, List<String>>, InputStream> handleResponse) throws IOException {
		String urlNameString = url + "?" + param;
		// �򿪺�URL֮�������
		URLConnection connection = new URL(urlNameString).openConnection();
		// ����ͨ�õ���������
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// ����ʵ�ʵ�����
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
	 * ��ָ�� URL ����POST����������
	 *
	 * @param url
	 *            ��������� URL
	 * @param param
	 *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	 * @return URL������Զ����Դ����Ӧ���
	 * @throws IOException
	 */
	public static void httpPost(String url, String param,
			BiConsumer<Map<String, List<String>>, InputStream> handleResponse) throws IOException {
		PrintWriter out = null;
		// �򿪺�URL֮�������
		URLConnection conn = new URL(url).openConnection();
		// ����ͨ�õ���������
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// ����POST�������������������
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// ��ȡURLConnection�����Ӧ�������
		out = new PrintWriter(conn.getOutputStream());
		// �����������
		out.print(param);
		// flush������Ļ���
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
			throw new IOException("�޷�������ʱ�ļ��� " + result.getAbsolutePath());
		}
		return result;
	}

	/**
	 * HTML����,ʹ��HtmlParser����HTML
	 * 
	 * @param html
	 *            ��������ı�
	 * @return
	 * @throws IOException
	 */
	public static String parserHtml2Text(String html) throws Exception {
		// ����HtmlParser�������������String������HTML��ǿ�ͷʱ��Parser��Ϊ�Ǵ��ļ����е��ļ��л�ȡ��
		Parser parser = new Parser("<div>" + html + "</div>");
		// ����Text������,TextExtractingVisitor������html�����еı�ǣ�����ȡhtml�����
		TextExtractingVisitor textVisitor = new TextExtractingVisitor();
		parser.visitAllNodesWith(textVisitor);
		return textVisitor.getExtractedText();
	}
}
