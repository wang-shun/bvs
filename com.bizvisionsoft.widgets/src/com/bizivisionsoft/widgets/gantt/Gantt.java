package com.bizivisionsoft.widgets.gantt;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.google.gson.GsonBuilder;

public class Gantt extends Composite {

	private static final String REMOTE_TYPE = "bizvision.dhtmlxgantt";

	private final String widgetName = "dhtmlxgantt";

	private Config config;

	private JsonObject inputData;

	private Date initFrom;

	private Date initTo;

	private RemoteObject remoteObject;

	public static final int EVENT_GRID_MENU = 1000;

	public static final int EVENT_ROW_MENU = 1001;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String method, JsonObject parameters) {
			Event event = new Event();
			int eventType;
			if ("gridMenuClicked".equals(method)) {
				eventType = EVENT_GRID_MENU;
			} else if ("gridRowMenuClicked".equals(method)) {
				int hashcode = parameters.get("$hashCode").asInt();
				event.data = findTask(hashcode);
				eventType = EVENT_ROW_MENU;
			} else {
				return;
			}
			Display.getCurrent().asyncExec(() -> {
				Arrays.asList(getListeners(eventType)).forEach(l -> l.handleEvent(event));
			});
		}

	};

	private List<?> tasks;

	private List<?> links;

	public Gantt(Composite parent, Config config) {
		super(parent, SWT.NONE);
		this.config = config;
		loadJsLibAndCSS();
		WidgetToolkit.requireWidgetHandlerJs("dhtmlxgantt");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
		remoteObject.set("parent", getId(this));
		String json = new GsonBuilder().create().toJson(config);
		remoteObject.set("config", JsonValue.readFrom(json));
	}

	private void loadJsLibAndCSS() {
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/dhtmlx.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/dhtmlxgantt.js");
		WidgetToolkit.requireWidgetCss(widgetName, "codebase/dhtmlxgantt.css");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 加载插件
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_critical_path.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_grouping.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_marker.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_multiselect.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_smart_rendering.js");
		// WidgetToolkit.requireWidgetJs(widgetName,
		// "codebase/ext/dhtmlxgantt_tooltip.js");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 加载语言包，应根据RWT的locale
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/locale/locale_cn.js");

	}

	public Config getConfig() {
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

	public void setInputData(String cName, List<?> tasks, List<?> links) {
		this.tasks = tasks;
		this.links = links;
		setInputData(WidgetToolkit.transformToJsonInput(cName, tasks, links));
	}

	Object findTask(int hashCode) {
		return this.tasks.stream().filter(o -> {
			return o.hashCode() == hashCode;
		}).findFirst().orElse(null);
	}

	Object findLink(int hashCode) {
		return this.links.stream().filter(o -> {
			return o.hashCode() == hashCode;
		}).findFirst().orElse(null);

	}

}
