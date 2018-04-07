package com.bizivisionsoft.widgets.gantt;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.bizvisionsoft.annotations.AUtil;
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
		public void handleCall(String eventCode, JsonObject jo) {

			Display.getCurrent().asyncExec(() -> {
				if ("taskUpdated".equals(eventCode)) {
					taskUpdated(jo);
					return;
				} else if ("taskDeleted".equals(eventCode)) {
					taskDeleted(jo);
					return;
				} else if ("linkUpdated".equals(eventCode)) {
					linkUpdated(jo);
					return;
				} else if ("linkDeleted".equals(eventCode)) {
					linkDeleted(jo);
					return;
				} else if ("createLink".equals(eventCode)) {
					linkDeleted(jo);
					return;
				} else {
					Event event = createEvent(eventCode, jo);
					Optional.ofNullable(listenerMap.get(eventCode))
							.ifPresent(l -> l.forEach(a -> a.handleEvent(event)));
				}
			});
		}

	};

	List<Object> tasks;

	List<Object> links;

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
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_auto_scheduling.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_critical_path.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_grouping.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_marker.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_multiselect.js");
		// WidgetToolkit.requireWidgetJs(widgetName,
		// "codebase/ext/dhtmlxgantt_smart_rendering.js");
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
		this.tasks = new ArrayList<Object>();
		this.tasks.addAll(tasks);

		this.links = new ArrayList<Object>();
		this.links.addAll(links);
		
		setInputData(transformToJsonInput(containerName, tasks, links, convertor));
	}

	Object findTask(String id) {
		return this.tasks.stream().filter(o -> {
			return id.equals(AUtil.readValue(o, containerName, "id", null));
		}).findFirst().orElse(null);
	}

	Object findLink(String id) {
		return this.links.stream().filter(o -> {
			return id.equals(AUtil.readValue(o, containerName, "id", null));
		}).findFirst().orElse(null);

	}

	public Gantt setContainer(String containerName) {
		this.containerName = containerName;
		return this;
	}

	private JsonObject transformToJsonInput(String cName, List<?> data, List<?> links,
			BiFunction<String, Object, Object> c) {
		// 处理模型
		JsonArray _data = new JsonArray();
		JsonArray _links = new JsonArray();

		if (data != null)
			data.forEach(o -> {
				JsonObject jo = WidgetToolkit.read(o.getClass(), o, cName, true, true, true, c);
				_data.add(jo);
			});

		if (links != null)
			links.forEach(o -> {
				JsonObject jo = WidgetToolkit.read(o.getClass(), o, cName, true, true, true, c);
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

	public void addTask(Object item, int index) {
		tasks.add(item);
		JsonObject task = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, convertor);
		JsonObject parameter = new JsonObject();
		parameter.add("task", task).add("index", index);
		remoteObject.call("addTask", parameter);
	}

	public void addLink(Object item) {
		links.add(item);
		JsonObject link = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, convertor);
		remoteObject.call("addLink", link);
	}

	public void updateLink(Object item) {
		JsonObject link = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, convertor);
		remoteObject.call("updateLink", link);
	}

	private Event createEvent(String eventCode, JsonObject jo) {
		GanttEvent event = new GanttEvent();
		event.text = eventCode;
		event.data = jo;

		if (GanttEventCode.onGridHeaderMenuClick.name().equals(eventCode)) {

		} else if (GanttEventCode.onTaskLinkBefore.name().equals(eventCode)) {
			event.linkSource = findTask(jo.get("source").asString());
			event.linkTarget = findTask(jo.get("target").asString());
			event.linkType = jo.get("type").asString();
		} else if (GanttEventCode.onGridRowMenuClick.name().equals(eventCode)) {
			event.taskId = jo.get("id").asString();
			event.task = findTask(event.taskId);

		} else if (GanttEventCode.onAfterAutoSchedule.name().equals(eventCode)) {
			event.taskId = jo.get("taskId").asString();
			ArrayList<String> data = new ArrayList<>();
			jo.get("e").asArray().forEach(v -> data.add(v.asString()));
			event.updatedTasks = data;

		} else if (GanttEventCode.onAfterLinkAdd.name().equals(eventCode)
				|| GanttEventCode.onAfterLinkDelete.name().equals(eventCode)
				|| GanttEventCode.onAfterLinkUpdate.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.link = findLink(event.taskId);

		} else if (GanttEventCode.onAfterTaskAdd.name().equals(eventCode)
				|| GanttEventCode.onAfterTaskDelete.name().equals(eventCode)
				|| GanttEventCode.onAfterTaskUpdate.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.task = findTask(event.id);

		} else if (GanttEventCode.onAfterTaskAutoSchedule.name().equals(eventCode)) {
			event.task = findTask(jo.get("task").asObject().get("id").asString());
			event.link = findLink(jo.get("link").asObject().get("id").asString());
			event.startDate = jo.get("start").asString();
			event.predecessor = jo.get("predecessor").toString();// TODO

		} else if (GanttEventCode.onAutoScheduleCircularLink.name().equals(eventCode)) {
			event.groups = new ArrayList<GanttGroup>();
			jo.get("groups").asArray().forEach(jv -> {
				event.groups.add(new GanttGroup(jv.asObject()));
			});
		} else if (GanttEventCode.onCircularLinkError.name().equals(eventCode)) {
			event.link = findLink(jo.get("link").asObject().get("id").asString());
			event.group = new GanttGroup(jo.get("group").asObject());

		} else if (GanttEventCode.onEmptyClick.name().equals(eventCode)
				|| GanttEventCode.onMultiSelect.name().equals(eventCode)) {

		} else if (GanttEventCode.onError.name().equals(eventCode)) {
			event.errorMessage = jo.get("errorMessage").asString();

		} else if (GanttEventCode.onLinkClick.name().equals(eventCode)
				|| GanttEventCode.onLinkDblClick.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.link = findLink(event.id);
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
			event.link = findLink(jo.get("link").asObject().get("id").asString());

		} else if (GanttEventCode.onScaleClick.name().equals(eventCode)) {
			event.date = jo.get("date").asString();

		} else if (GanttEventCode.onTaskMultiSelect.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.state = jo.get("state").asBoolean();

		} else {
		}

		return event;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/// 同步客户端的变化

	private void linkDeleted(JsonObject jo) {
		Event event = new Event();
		event.text = "linkDeleted";
		event.data = findLink(jo.get("id").asString());
		links.remove(event.data);
		Arrays.asList(getListeners(SWT.Modify)).forEach(l -> l.handleEvent(event));
	}

	private void linkUpdated(JsonObject jo) {
		Event event = new Event();
		event.text = "linkUpdated";
		Object link = findLink(jo.get("id").asString());
		if (link != null) {
			WidgetToolkit.write(link, jo.get("link").asObject(), containerName, true, true, true, convertor);
			event.data = link;
		}
		Arrays.asList(getListeners(SWT.Modify)).forEach(l -> l.handleEvent(event));
	}

	private void taskDeleted(JsonObject jo) {
		Event event = new Event();
		event.text = "taskDeleted";
		event.data = findTask(jo.get("id").asString());
		tasks.remove(event.data);
		Arrays.asList(getListeners(SWT.Modify)).forEach(l -> l.handleEvent(event));
	}

	private void taskUpdated(JsonObject jo) {
		Event event = new Event();
		event.text = "taskUpdated";
		Object task = findTask(jo.get("id").asString());
		if (task != null) {
			WidgetToolkit.write(task, jo, containerName, true, true, true, convertor);
			event.data = task;
		}
		Arrays.asList(getListeners(SWT.Modify)).forEach(l -> l.handleEvent(event));

	}

}
