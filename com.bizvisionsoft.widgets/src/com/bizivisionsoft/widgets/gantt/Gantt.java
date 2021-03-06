package com.bizivisionsoft.widgets.gantt;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
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
				Event event = createEvent(eventCode, jo);
				if (event.doit)
					Optional.ofNullable(listenerMap.get(eventCode)).ifPresent(l -> l.forEach(a -> {
						a.handleEvent(event);
						handleCallBack();
					}));

			});
		}

	};

	List<Object> tasks;

	List<Object> cacheTasks;

	List<Object> links;

	List<Object> cacheLinks;

	private boolean dirty;

	private String containerName;

	private BiFunction<String, Object, Object> convertor = (n, v) -> {
		if (v instanceof Date)
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
		return v;
	};

	private BiFunction<String, Object, Object> convertor4UpdateGantt = (n, v) -> {
		if (v instanceof Date) {
			String format = "%Y-%m-%d %H:%i:%s";
			String value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
			return new JsonObject().add("type", "Date").add("format", format).add("value", value);
		}
		return v;
	};

	private BiConsumer<List<Object>, List<Object>> callBack;

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

	protected void handleCallBack() {
		if (callBack != null) {
			callBack.accept(tasks, links);
			callBack = null;
		}
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
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_smart_rendering.js");
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/ext/dhtmlxgantt_undo.js");

		// WidgetToolkit.requireWidgetJs(widgetName,
		// "codebase/ext/dhtmlxgantt_tooltip.js");

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

	// public void setInitDateRange(Date initFrom, Date initTo) {
	// if (initTo == null || initFrom == null)
	// SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	// checkWidget();
	// this.initFrom = initFrom;
	// this.initTo = initTo;
	// remoteObject.set("initFrom", new
	// SimpleDateFormat("yyyy/MM/dd").format(initFrom));
	// remoteObject.set("initTo", new
	// SimpleDateFormat("yyyy/MM/dd").format(initTo));
	// }

	public void setInputData(List<?> tasks, List<?> links) {
		this.tasks = new ArrayList<Object>();
		this.cacheTasks = new ArrayList<Object>();
		if (tasks != null) {
			this.tasks.addAll(tasks);
			this.cacheTasks.addAll(tasks);
		}

		this.links = new ArrayList<Object>();
		this.cacheLinks = new ArrayList<Object>();
		if (links != null) {
			this.links.addAll(links);
			this.cacheLinks.addAll(tasks);
		}

		JsonObject inputDataObject = transformToJsonInput(containerName, tasks, links, convertor);

		JsonArray inputData = (JsonArray) inputDataObject.get("data");

		Set<String> parentCode = new HashSet<String>();
		inputData.forEach(jv -> parentCode.add(jv.asObject().get("id").asString()));
		inputData.forEach(jv -> {
			JsonValue _parent = jv.asObject().get("parent");
			if (_parent != null && _parent.isString() && !parentCode.contains(_parent.asString())) {
				jv.asObject().remove("parent");
			}
		});

		setInputData(inputDataObject);
	}

	Object findTask(String id) {
		return find(tasks, id);
	}

	Object findInCacheTasks(String id) {
		return find(cacheTasks, id);
	}

	Object findLink(String id) {
		return find(links, id);
	}

	Object findInCacheLinks(String id) {
		return find(cacheLinks, id);
	}

	private Object find(List<Object> arr, String id) {
		return arr.stream().filter(o -> {
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

	private GanttEvent createEvent(String eventCode, JsonObject jo) {
		GanttEvent event = new GanttEvent();
		event.gantt = this;
		event.text = eventCode;
		event.data = jo;
		JsonValue id = jo.get("id");
		if ("dirtyChanged".equals(eventCode)) {
			JsonValue jv = jo.get("dirty");
			if (jv.isBoolean()) {
				this.dirty = jv.asBoolean();
			}
		} else if ("update".equals(eventCode)) {
			event.tasks = new ArrayList<Object>();
			event.links = new ArrayList<Object>();
			jo.get("tasks").asArray().forEach(jv -> {
				JsonObject _jo = jv.asObject();
				String _id = _jo.get("id").asString();
				Object obj = findTask(_id);
				if (obj == null)
					obj = findInCacheTasks(_id);
				WidgetToolkit.write(obj, _jo, containerName, "id");
				event.tasks.add(obj);
			});

			jo.get("links").asArray().forEach(jv -> {
				JsonObject _jo = jv.asObject();
				String _id = _jo.get("id").asString();
				Object obj = findLink(_id);
				if (obj == null)
					obj = findInCacheLinks(_id);
				WidgetToolkit.write(obj, _jo, containerName, "id");
				event.links.add(obj);
			});
			tasks.clear();
			tasks.addAll(event.tasks);
			links.clear();
			links.addAll(event.links);
			// TODO 考虑新任务，删除任务的情况，这些操作有BUG
		} else if ("save".equals(eventCode)) {
			event.tasks = new ArrayList<Object>();
			event.links = new ArrayList<Object>();
			jo.get("tasks").asArray().forEach(jv -> {
				JsonObject _jo = jv.asObject();
				String _id = _jo.get("id").asString();
				Object obj = findTask(_id);
				WidgetToolkit.write(obj, _jo, containerName, "id");
				event.tasks.add(obj);
			});

			jo.get("links").asArray().forEach(jv -> {
				JsonObject _jo = jv.asObject();
				String _id = _jo.get("id").asString();
				Object obj = findLink(_id);
				WidgetToolkit.write(obj, _jo, containerName, "id");
				event.links.add(obj);
			});
			tasks.clear();
			tasks.addAll(event.tasks);
			links.clear();
			links.addAll(event.links);
		} else if (GanttEventCode.onGridHeaderMenuClick.name().equals(eventCode)) {

		} else if (GanttEventCode.onTaskLinkBefore.name().equals(eventCode)) {
			event.linkSource = findTask(jo.get("source").asString());
			event.linkTarget = findTask(jo.get("target").asString());
			event.linkType = jo.get("type").asString();
			event.doit = event.linkSource != null && event.linkTarget != null && event.linkType != null;
		} else if (GanttEventCode.onGridRowMenuClick.name().equals(eventCode)) {
			if (id.isString()) {
				event.taskId = id.asString();
				event.task = findTask(event.taskId);
				event.doit = event.task != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onAfterAutoSchedule.name().equals(eventCode)) {
			JsonValue jv = jo.get("taskId");
			if (jv != null && jv.isString()) {
				event.taskId = jv.asString();
			}
			ArrayList<String> data = new ArrayList<>();
			jo.get("updatedTasks").asArray().forEach(v -> data.add(v.asString()));
			event.updatedTasks = data;
			// TODO
		} else if (GanttEventCode.onAfterLinkAdd.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				event.link = findLink(event.id);
				event.doit = event.link != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onAfterLinkDelete.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				event.link = findLink(event.id);
				links.remove(event.link);
				event.doit = event.link != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onAfterLinkUpdate.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				Object link = findLink(event.id);
				if (link != null
						&& !WidgetToolkit.write(link, jo.get("link").asObject(), containerName, "id").isEmpty()) {
					event.link = link;
				}
				event.doit = event.link != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onAfterTaskAdd.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				event.task = findTask(event.id);
				event.doit = event.task != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onAfterTaskDelete.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				event.task = findTask(event.id);
				tasks.remove(event.task);
				event.doit = event.task != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onAfterTaskUpdate.name().equals(eventCode)
				|| GanttEventCode.onAfterTaskProgress.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				Object task = findTask(event.id);
				if (task != null
						&& !WidgetToolkit.write(task, jo.get("task").asObject(), containerName, "id").isEmpty()) {
					event.task = task;
				}
				event.doit = event.task != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onAfterTaskAutoSchedule.name().equals(eventCode)) {
			// event.task = findTask(jo.get("task").asObject().get("id").asString());
			// event.link = findLink(jo.get("link").asObject().get("id").asString());
			// event.startDate = jo.get("start").asString();
			// event.predecessor = jo.get("predecessor").toString();
			// 不必单独考虑
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
			if (id.isString()) {
				event.id = id.asString();
				event.link = findLink(event.id);
				event.clientType = "link";
				event.doit = event.link != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onTaskClick.name().equals(eventCode)
				|| GanttEventCode.onTaskDblClick.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				event.task = findTask(event.id);
				event.clientType = "task";
				event.doit = event.task != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onTaskSelected.name().equals(eventCode)
				|| GanttEventCode.onTaskUnselected.name().equals(eventCode)
				|| GanttEventCode.onTaskRowClick.name().equals(eventCode)) {
			if (id.isString()) {
				event.id = id.asString();
				event.task = findTask(event.id);
				event.clientType = "task";
				event.doit = event.task != null;
			} else {
				event.doit = false;
			}
		} else if (GanttEventCode.onLinkValidation.name().equals(eventCode)) {
			event.link = findLink(jo.get("link").asObject().get("id").asString());
			// TODO
		} else if (GanttEventCode.onScaleClick.name().equals(eventCode)) {
			event.date = jo.get("date").asString();

		} else if (GanttEventCode.onTaskMultiSelect.name().equals(eventCode)) {
			event.id = jo.get("id").asString();
			event.state = jo.get("state").asBoolean();
			// TODO

		} else {
		}

		return event;
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	/// 对Gantt的客户端操作
	/**
	 * 
	 * @param item
	 * @param index
	 */
	public void addTask(Object item) {
		tasks.add(item);
		cacheTasks.add(item);
		JsonObject task = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, convertor);

		JsonArray inputArray = (JsonArray) this.inputData.get("data");
		inputArray.add(task);
		Set<String> parentCode = new HashSet<String>();
		inputArray.forEach(jv -> parentCode.add(jv.asObject().get("id").asString()));

		JsonValue _parent = task.get("parent");
		if (_parent != null && _parent.isString() && !parentCode.contains(_parent.asString())) {
			task.remove("parent");
		}

		JsonObject parameter = new JsonObject();
		parameter.add("task", task);
		remoteObject.call("addTask", parameter);
	}

	public void addLink(Object item) {
		links.add(item);
		cacheLinks.add(item);
		JsonObject link = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, convertor);
		remoteObject.call("addLink", link);
	}

	public void deleteTask(String id) {
		remoteObject.call("deleteTask", new JsonObject().add("id", id));
	}

	public void deleteLink(String id) {
		remoteObject.call("deleteLink", new JsonObject().add("id", id));
	}

	public void updateLink(Object item) {
		links.forEach(link -> {
			if (link.equals(item)) {
				AUtil.simpleCopy(item, link);
			}
		});
		JsonObject link = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, convertor);
		remoteObject.call("updateLink", link);
	}

	public void updateTask(Object item) {
		tasks.forEach(task -> {
			if (task.equals(item)) {
				AUtil.simpleCopy(item, task);
			}
		});

		JsonObject task = WidgetToolkit.read(item.getClass(), item, containerName, true, true, false,
				convertor4UpdateGantt);

		JsonArray inputArray = (JsonArray) this.inputData.get("data");

		JsonValue _parent = task.get("parent");
		Set<String> parentCode = new HashSet<String>();
		inputArray.forEach(jv -> parentCode.add(jv.asObject().get("id").asString()));
		if ((_parent != null && _parent.isString() && !parentCode.contains(_parent.asString())) || _parent.isNull()) {
			task.remove("parent");
		}
		remoteObject.call("updateTask", task);
	}

	public void autoSchedule() {
		remoteObject.call("autoSchedule", new JsonObject());
	}

	public void save() {
		remoteObject.call("save", new JsonObject());
	}

	public void save(BiConsumer<List<Object>, List<Object>> callback) {
		remoteObject.call("save", new JsonObject());
		this.callBack = callback;
	}

	public void highlightCriticalPath(boolean display) {
		remoteObject.call("highlightCriticalPath", new JsonObject().add("display", display));
	}

	public void setScaleType(String type) {
		remoteObject.call("setScaleType", new JsonObject().add("type", type));
	}

	public void redo() {
		remoteObject.call("redo", new JsonObject());
	}

	public void undo() {
		remoteObject.call("undo", new JsonObject());
	}

	public void setDirty(boolean dirty) {
		remoteObject.call("setDirty", new JsonObject().add("dirty", dirty));
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}

}
