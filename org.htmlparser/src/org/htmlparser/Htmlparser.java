package org.htmlparser;

import java.io.IOException;

import org.htmlparser.visitors.TextExtractingVisitor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Htmlparser implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Htmlparser.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Htmlparser.context = null;
	}
	
	/**
	 * HTML解析,使用HtmlParser解析HTML
	 * 
	 * @param html
	 *            需解析的文本
	 * @return
	 * @throws IOException
	 */
	public static String parser(String html) throws Exception {
		// 构建HtmlParser解析器，传入的String不是以HTML标记开头时，Parser认为是从文件夹中的文件中获取。
		Parser parser = new Parser("<div>" + html + "</div>");
		// 构建Text遍历器,TextExtractingVisitor将遍历html中所有的标记，并获取html标记中
		TextExtractingVisitor textVisitor = new TextExtractingVisitor();
		parser.visitAllNodesWith(textVisitor);
		return textVisitor.getExtractedText();
	}
}
