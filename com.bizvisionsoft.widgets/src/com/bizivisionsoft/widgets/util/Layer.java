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
		WidgetToolkit.execJS("layer.msg(\"" + esc(msg) + "\")");
	}

	public static void message(String msg, int icon) {
		WidgetToolkit.execJS(
				"layer.msg(\"" + esc(msg) + "\", {shade:0.2,skin: 'layui-layer-lan',icon: " + icon + ",anim: 0})");
	}

	public static void message(String title, String msg) {
		WidgetToolkit.execJS("layer.msg(\"" + esc(msg) + "\", {title:\"" + esc(title)
				+ "\", shade:0.2,skin: 'layui-layer-lan',anim: 0})");
	}

	public static void message(String title, String msg, int animate) {
		WidgetToolkit.execJS("layer.msg(\"" + esc(msg) + "\", {title:\"" + esc(title)
				+ "\", shade:0.2,skin: 'layui-layer-lan',anim: " + animate + "})");
	}

	public static void message(String title, String msg, int icon, int animate) {
		WidgetToolkit.execJS("layer.msg(\"" + esc(msg) + "\", {title:\"" + esc(title)
				+ "\", shade:0.2,skin: 'layui-layer-lan',icon: " + icon + ",anim: " + animate + "})");
	}

	public static void open(String title, String content, int width, int height) {
		open(title, 1, content, width, height);
	}
	
	public static void open(String title,int type, String content, int width, int height) {
		String html = "<div style='margin:16px;'>" + esc(content) + "</div>";
		WidgetToolkit
				.execJS("layer.open({\"type\": "+type+",\"title\": \"" + title + "\",\"skin\": \"layui-layer-lan\", area: [\""
						+ width + "px\", \"" + height + "px\"], \"maxmin\":true,content: \"" + esc(html) + "\"})");
	}

	public static void alert(String title, String html, int width, int height) {
		WidgetToolkit.execJS("layer.alert(\"" + esc(html) + "\",{\"closeBtn\": 0,\"title\": \"" + esc(title)
				+ "\",\"skin\": \"layui-layer-lan\", area: [\"" + width + "px\", \"" + height + "px\"]})");
	}

	public static String esc(String input) {
		if (input == null) {
			return "";
		}
		String output = input.replaceAll("'", "&#x27;");
		output = output.replaceAll("\"", "&quot;");
		output = output.replaceAll("\\n", "");
		return output;
	}

	public static String onClick(String text, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div onclick='layer.tips(\"" + //
				message + "\", this, {tips: [1, \"#3595CC\"],time:3000,area:\"300px\"})'>");//
		sb.append(text);
		sb.append("</div>");
		return sb.toString();
	}

	public static String onMouseOver(String text, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div onMouseOver='layer.tips(\"" + //
				message + "\", this, {tips: [1, \"#3595CC\"],time:3000,area:\"300px\"})'>");//
		sb.append(text);
		sb.append("</div>");
		return sb.toString();
	}
}
