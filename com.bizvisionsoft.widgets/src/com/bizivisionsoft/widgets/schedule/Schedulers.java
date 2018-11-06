package com.bizivisionsoft.widgets.schedule;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.bizvisionsoft.annotations.AUtil;

public class Schedulers extends Composite {

	private static final String REMOTE_TYPE = "bizvision.dhtmlxscheduler";

	private final String widgetName = "dhtmlxscheduler";

	private RemoteObject remoteObject;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String eventCode, JsonObject jo) {
			Display.getCurrent().asyncExec(() -> {
				if ("onClick".equals(eventCode)) {
					select(jo);
				} else if ("onDateClick".equals(eventCode)) {
					selectDate(jo);
				}
			});
		}

	};

	List<Object> data;

	List<Object> section;

	private String containerName;

	private JsonObject inputData;

	private JsonObject sectionData;

	public Schedulers(Composite parent, String type) {
		super(parent, SWT.NONE);

		loadJsLibAndCSS();
		WidgetToolkit.requireWidgetHandlerJs("dhtmlxscheduler");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
		remoteObject.set("parent", getId(this));
		remoteObject.set("type", type);
	}

	private void selectDate(JsonObject jo) {
		String date = jo.get("date").asString();
		Event event = new Event();
		event.data = date;
		Arrays.asList(getListeners(SWT.Selection)).forEach(l -> l.handleEvent(event));
	}

	private void select(JsonObject jo) {
		String id = jo.get("id").asString();
		Event event = new Event();
		Object item = findItem(id);
		event.data = item;
		event.text = id;
		Arrays.asList(getListeners(SWT.Selection)).forEach(l -> l.handleEvent(event));
	}

	private Object findItem(String id) {
		return this.data.stream().filter(o -> {
			return id.equals(AUtil.readValue(o, containerName, "id", null));
		}).findFirst().orElse(null);
	}

	private void loadJsLibAndCSS() {
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/dhtmlxscheduler.js");
		WidgetToolkit.requireWidgetCss(widgetName, "codebase/dhtmlxscheduler.css");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 加载插件
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxscheduler_container_autoresize.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxscheduler_timeline.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxscheduler_limit.js");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 加载语言包，应根据RWT的locale
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/locale/locale_cn.js");

	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			remoteObject.destroy();
		}
		super.dispose();
	}

	public Schedulers setContainer(String cName) {
		this.containerName = cName;
		return this;
	}

	private void setInputData(JsonObject inputData) {
		if (inputData == null)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		checkWidget();
		this.inputData = inputData;
		remoteObject.set("inputData", inputData);
	}

	private void setSectionData(JsonObject sectionData) {
		if (sectionData == null)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		checkWidget();
		this.sectionData = sectionData;
		remoteObject.set("sectionData", sectionData);
	}

	public Schedulers setInput(List<?> tasks) {
		this.data = new ArrayList<Object>();
		this.data.addAll(tasks);

		setInputData(transformToJsonInput(containerName, tasks, (n, v) -> {
			if (v instanceof Date)
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
			return v;
		}));

		return this;
	}

	public Schedulers setSection(List<?> section) {
		this.section = new ArrayList<Object>();
		this.section.addAll(section);
		setSectionData(transformToJsonInput(containerName, section, null));
		return this;
	}

	private JsonObject transformToJsonInput(String cName, List<?> data, BiFunction<String, Object, Object> c) {
		// 处理模型
		JsonArray _data = new JsonArray();
		if (data != null)
			data.forEach(o -> {
				JsonObject jo = WidgetToolkit.read(o.getClass(), o, cName, true, true, true, c);
				_data.add(jo);
			});

		return new JsonObject().add("data", _data);
	}

	public JsonObject getInputData() {
		return inputData;
	}

	public JsonObject getSectionData() {
		return sectionData;
	}

}
