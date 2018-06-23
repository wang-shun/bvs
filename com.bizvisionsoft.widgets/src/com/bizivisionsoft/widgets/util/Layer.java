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
	
	public static void message(String msg,int icon) {
		WidgetToolkit.execJS("layer.msg(\"" + msg + "\", {shade:0.2,skin: 'layui-layer-lan',icon: "+icon+",anim: 0})");
	}
	
	public static void message(String title,String msg) {
		WidgetToolkit.execJS("layer.msg(\"" + msg + "\", {title:\""+title+"\", shade:0.2,skin: 'layui-layer-lan',anim: 0})");
	}
	
	public static void message(String title,String msg,int animate) {
		WidgetToolkit.execJS("layer.msg(\"" + msg + "\", {title:\""+title+"\", shade:0.2,skin: 'layui-layer-lan',anim: "+animate+"})");
	}
	
	public static void message(String title,String msg,int icon,int animate) {
		WidgetToolkit.execJS("layer.msg(\"" + msg + "\", {title:\""+title+"\", shade:0.2,skin: 'layui-layer-lan',icon: "+icon+",anim: "+animate+"})");
	}
	
}
