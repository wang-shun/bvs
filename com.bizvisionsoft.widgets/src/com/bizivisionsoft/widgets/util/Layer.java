package com.bizivisionsoft.widgets.util;

public class Layer {
	

	
	public static int ICON_INFO = 0;
	
	public static int ICON_FINISH = 1;

	public static int ICON_CANCEL = 2;
	
	public static int ICON_QUESTION = 3;

	public static int ICON_LOCK = 4;

	public static int ICON_CRY = 5;

	public static int ICON_SMILE = 6;

	public static void message(String msg) {
		WidgetToolkit.execJS("layer.msg(\"" + msg + "\")");
	}
	
	public static void showMessage(String msg,int icon) {
		WidgetToolkit.execJS("layer.msg(\"" + msg + "\", {icon: "+icon+"})");
	}
	
}
