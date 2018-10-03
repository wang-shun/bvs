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
	 * HTML����,ʹ��HtmlParser����HTML
	 * 
	 * @param html
	 *            ��������ı�
	 * @return
	 * @throws IOException
	 */
	public static String parser(String html) throws Exception {
		// ����HtmlParser�������������String������HTML��ǿ�ͷʱ��Parser��Ϊ�Ǵ��ļ����е��ļ��л�ȡ��
		Parser parser = new Parser("<div>" + html + "</div>");
		// ����Text������,TextExtractingVisitor������html�����еı�ǣ�����ȡhtml�����
		TextExtractingVisitor textVisitor = new TextExtractingVisitor();
		parser.visitAllNodesWith(textVisitor);
		return textVisitor.getExtractedText();
	}
}
