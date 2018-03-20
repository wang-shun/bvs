package com.bizivisionsoft.widgets.util;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ClientFileLoader;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

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
		jsLoader.requireJs(uri+"bvs/widgets/"+widgetName+"/js/handler.js");
	}
	
	public static void requireWidgetJs(String widgetName,String path) {
		ClientFileLoader jsLoader = RWT.getClient().getService(ClientFileLoader.class);
		String uri = getSiteRoot();
		jsLoader.requireJs(uri+"bvs/widgets/"+widgetName+"/"+path);
	}
	
	public static void requireWidgetCss(String widgetName,String path) {
		ClientFileLoader jsLoader = RWT.getClient().getService(ClientFileLoader.class);
		String uri = getSiteRoot();
		jsLoader.requireCss(uri+"bvs/widgets/"+widgetName+"/"+path);
	}


	public static String getSiteRoot() {
		String uri = RWT.getRequest().getRequestURI();
		if(uri.endsWith("/")) {
			return uri;
		}else {
			return uri.substring(0, uri.lastIndexOf("/"))+"/";
		}
	}


	public static String getSiteHttpRoot() {
		HttpServletRequest request = RWT.getRequest();
		String root;
		if(request.getRequestURL().toString().toLowerCase().startsWith("https://")) {
			root = "https://";
		}else {
			root = "http://";
		}
		root += request.getLocalAddr();
		root +=  ":"+request.getLocalPort();
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
			randBuffer[i] = numbersAndLetters[randGen
					.nextInt(numbersAndLetters.length - 1)];
		}
		return new String(randBuffer);
	}

}
