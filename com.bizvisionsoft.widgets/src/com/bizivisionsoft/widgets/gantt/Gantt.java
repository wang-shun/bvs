package com.bizivisionsoft.widgets.gantt;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.rap.json.JsonArray;
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
import org.eclipse.swt.widgets.Listener;

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

	private Map<String, ListenerList<Listener>> listenerMap;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String eventCode, JsonObject parameters) {

			//
			// if ("onGridHeaderMenuClick".equals(eventCode) && headerMenuListener != null)
			// {
			// GanttEvent event = new
			// GanttEvent(GanttEventCode.onGridHeaderMenuClick.name());
			// headerMenuListener.accept(event);
			// return;
			// }
			//
			// if ("onGridRowMenuClick".equals(eventCode) && rowMenuListener != null) {
			// int hashcode = parameters.get("$hashCode").asInt();
			// GanttEvent event = new
			// GanttEvent(GanttEventCode.onGridRowMenuClick.name()).setData(findTask(hashcode));
			// rowMenuListener.accept(event);
			// return;
			// }

			Display.getCurrent().asyncExec(() -> {
				Event event = createEvent(eventCode, parameters);
				Optional.ofNullable(listenerMap.get(eventCode)).ifPresent(l -> l.forEach(a -> a.handleEvent(event)));
			});
		}

	};

	private List<?> tasks;

	private List<?> links;

	private String containerName;

	private BiFunction<String, Object, Object> convertor = (n, v) -> {
		if (v instanceof Date)
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
		return v;
	};

	public Gantt(Composite parent, Config config) {
		super(parent, SWT.NONE);

		listenerMap = new HashMap<String, ListenerList<Listener>>();

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

	/**
	 * only for test
	 * 
	 * @param inputData
	 */
	@Deprecated
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

	public void setInputData(List<?> tasks, List<?> links) {
		this.tasks = tasks;
		this.links = links;
		setInputData(transformToJsonInput(containerName, tasks, links, convertor));
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

	public Gantt setContainer(String containerName) {
		this.containerName = containerName;
		return this;
	}

	private static JsonObject transformToJsonInput(String cName, List<?> data, List<?> links,
			BiFunction<String, Object, Object> c) {
		// 处理模型
		JsonArray _data = new JsonArray();
		JsonArray _links = new JsonArray();

		if (data != null)
			data.forEach(o -> {
				JsonObject jo = WidgetToolkit.readJsonFrom(o.getClass(), o, cName, true, true, true, c);
				_data.add(jo);
			});

		if (links != null)
			links.forEach(o -> {
				JsonObject jo = WidgetToolkit.readJsonFrom(o.getClass(), o, cName, true, true, true, c);
				_links.add(jo);
			});

		return new JsonObject().add("data", _data).add("links", _links);
	}

	public void addGanttEventListener(String eventCode, Listener listener) {
		ListenerList<Listener> lis = listenerMap.get(eventCode);
		if (lis == null) {
			lis = new ListenerList<Listener>();
			listenerMap.put(eventCode, lis);
		}
		lis.add(listener);

		// 排除系统事件
		if (GanttEventCode.onGridHeaderMenuClick.name().equals(eventCode)
				|| GanttEventCode.onGridRowMenuClick.name().equals(eventCode)) {
			return;
		}

		remoteObject.call("addListener", new JsonObject().add("name", eventCode));
	}

	public void removeGanttListener(String eventCode, Listener listener) {
		ListenerList<Listener> lis = listenerMap.get(eventCode);
		if (lis != null) {
			lis.remove(listener);
			// 排除系统事件
			if (GanttEventCode.onGridHeaderMenuClick.name().equals(eventCode)
					|| GanttEventCode.onGridRowMenuClick.name().equals(eventCode)) {
				return;
			}
			remoteObject.call("removeListener", new JsonObject().add("name", eventCode));
		}
	}

	public void addTask(Object item, String parentId, int index) {
		JsonObject task = WidgetToolkit.readJsonFrom(item.getClass(), item, containerName, true, true, true, convertor);
		JsonObject parameter = new JsonObject();
		parameter.add("task", task).add("parentId", parentId).add("index", index);
		remoteObject.call("addTask", parameter);
	}
	public void addTask(Object item, String parentId) {
		JsonObject task = WidgetToolkit.readJsonFrom(item.getClass(), item, containerName, true, true, true, convertor);
		JsonObject parameter = new JsonObject();
		parameter.add("task", task).add("parentId", parentId);
		remoteObject.call("addTask", parameter);
	}

	private Event createEvent(String eventCode, JsonObject jo) {
		GanttEvent event = new GanttEvent();
		event.text = eventCode;
		event.data = jo;

		if (GanttEventCode.onGridHeaderMenuClick.name().equals(eventCode)) {

		} else if (GanttEventCode.onGridRowMenuClick.name().equals(eventCode)) {
			event.taskId = jo.get("id").asString();
			event.task = findTask(jo.get("task").asObject().get("$hashCode").asInt());

		} else if (GanttEventCode.onAfterAutoSchedule.name().equals(eventCode)) {
			event.taskId = jo.get("taskId").asString();
			ArrayList<String> data = new ArrayList<>();
			jo.get("e").asArray().forEach(v -> data.add(v.asString()));
			event.updatedTasks = data;

		} else if (GanttEventCode.onAfterLinkAdd.name().equals(eventCode)
				|| GanttEventCode.onAfterLinkDelete.name().equals(eventCode)
				|| GanttEventCode.onAfterLinkUpdate.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.link = findLink(jo.get("item").asObject().get("$hashCode").asInt());

		} else if (GanttEventCode.onAfterTaskAdd.name().equals(eventCode)
				|| GanttEventCode.onAfterTaskDelete.name().equals(eventCode)
				|| GanttEventCode.onAfterTaskUpdate.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.task = findTask(jo.get("item").asObject().get("$hashCode").asInt());

		} else if (GanttEventCode.onAfterTaskAutoSchedule.name().equals(eventCode)) {
			event.task = findTask(jo.get("task").asObject().get("$hashCode").asInt());
			event.link = findLink(jo.get("link").asObject().get("$hashCode").asInt());
			event.startDate = jo.get("start").asString();
			event.predecessor = jo.get("predecessor").toString();// TODO

		} else if (GanttEventCode.onAutoScheduleCircularLink.name().equals(eventCode)) {
			event.groups = new ArrayList<GanttGroup>();
			jo.get("groups").asArray().forEach(jv -> {
				event.groups.add(new GanttGroup(jv.asObject()));
			});
		} else if (GanttEventCode.onCircularLinkError.name().equals(eventCode)) {
			event.link = findLink(jo.get("link").asObject().get("$hashCode").asInt());
			event.group = new GanttGroup(jo.get("group").asObject());

		} else if (GanttEventCode.onEmptyClick.name().equals(eventCode)
				|| GanttEventCode.onMultiSelect.name().equals(eventCode)) {

		} else if (GanttEventCode.onError.name().equals(eventCode)) {
			event.errorMessage = jo.get("errorMessage").asString();

		} else if (GanttEventCode.onLinkClick.name().equals(eventCode)
				|| GanttEventCode.onLinkDblClick.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.clientType = "link";
		} else if (GanttEventCode.onTaskClick.name().equals(eventCode)
				|| GanttEventCode.onTaskDblClick.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.clientType = "task";
		} else if (GanttEventCode.onTaskSelected.name().equals(eventCode)
				|| GanttEventCode.onTaskUnselected.name().equals(eventCode)
				|| GanttEventCode.onTaskRowClick.name().equals(eventCode)) {
			event.id = jo.get("id").asString();

		} else if (GanttEventCode.onLinkValidation.name().equals(eventCode)) {
			event.link = findLink(jo.get("link").asObject().get("$hashCode").asInt());

		} else if (GanttEventCode.onScaleClick.name().equals(eventCode)) {
			event.date = jo.get("date").asString();

		} else if (GanttEventCode.onTaskMultiSelect.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.state = jo.get("state").asBoolean();

		} else {
		}

		return event;
	}

}
