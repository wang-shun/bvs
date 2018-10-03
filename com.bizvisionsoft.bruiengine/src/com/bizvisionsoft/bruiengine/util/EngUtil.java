package com.bizvisionsoft.bruiengine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
		//TODO 根据文件后缀得到content-type
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
