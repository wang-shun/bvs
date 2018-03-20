package com.bizivisionsoft.widgets.gantt;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class Gantt extends Composite {

	private static final String REMOTE_TYPE = "bizvision.dhtmlxgantt";

	private final String widgetName = "dhtmlxgantt";

	private JsonObject config;

	private JsonObject inputData;

	private Date initFrom;

	private Date initTo;

	private RemoteObject remoteObject;

	public Gantt(Composite parent, int style) {
		super(parent, style);
		loadJsLibAndCSS();
		WidgetToolkit.requireWidgetHandlerJs("dhtmlxgantt");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.set("parent", getId(this));
	}

	private void loadJsLibAndCSS() {
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/dhtmlxgantt.js");
		WidgetToolkit.requireWidgetCss(widgetName, "codebase/dhtmlxgantt.css");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 加载插件
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_critical_path.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_grouping.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_marker.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_multiselect.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_smart_rendering.js");
//		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_tooltip.js");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 加载语言包，应根据RWT的locale
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/locale/locale_cn.js");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 皮肤
		// WidgetToolkit.requireWidgetCss(widgetName,
		// "codebase/skins/dhtmlxgantt_broadway.css");
		// WidgetToolkit.requireWidgetCss(widgetName,
		// "codebase/skins/dhtmlxgantt_contrast_black.css");
		// WidgetToolkit.requireWidgetCss(widgetName,
		// "codebase/skins/dhtmlxgantt_contrast_white.css");
		// WidgetToolkit.requireWidgetCss(widgetName,
		// "codebase/skins/dhtmlxgantt_material.css");
		// WidgetToolkit.requireWidgetCss(widgetName,
		// "codebase/skins/dhtmlxgantt_meadow.css");
		// WidgetToolkit.requireWidgetCss(widgetName,
		// "codebase/skins/dhtmlxgantt_skyblue.css");
		// WidgetToolkit.requireWidgetCss(widgetName,
		// "codebase/skins/dhtmlxgantt_terrace.css");

	}

	public JsonObject getConfig() {
		return config;
	}

	public JsonObject getInputData() {
		return inputData;
	}

	public Date getInitFrom() {
		return initFrom;
	}

	public Date getInitTo() {
		return initTo;
	}

	public void setConfig(JsonObject config) {
		if (config == null)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		checkWidget();
		this.config = config;
		remoteObject.set("config", config);
	}

	public void setInputData(JsonObject inputData) {
		if (inputData == null)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		checkWidget();
		this.inputData = inputData;
		remoteObject.set("inputData", inputData);
	}

	public void setInitDateRange(Date initFrom, Date initTo) {
		if (initTo == null || initFrom == null)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		checkWidget();
		this.initFrom = initFrom;
		this.initTo = initTo;
		remoteObject.set("initFrom", new SimpleDateFormat("yyyy/MM/dd").format(initFrom));
		remoteObject.set("initTo", new SimpleDateFormat("yyyy/MM/dd").format(initTo));
	}

}
