package com.bizivisionsoft.widgets.diagram;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.bizvisionsoft.annotations.AUtil;

public class Diagram extends Composite {

	private static final String REMOTE_TYPE = "bizvision.diagram";

	private final String widgetName = "diagram";

	private RemoteObject remoteObject;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String eventCode, JsonObject jo) {
			Display.getCurrent().asyncExec(() -> {
				if (eventCode.equals("click")) {
					select(jo);
				}
			});
		}

	};

	List<Object> data;

	private String containerName;

	private float scale = 1;

	public Diagram(Composite parent) {
		super(parent, SWT.NONE);

		loadJsLibAndCSS();
		WidgetToolkit.requireWidgetHandlerJs(widgetName);
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
		remoteObject.set("parent", getId(this));
	}

	private void loadJsLibAndCSS() {
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/diagram.js");
		WidgetToolkit.requireWidgetCss(widgetName, "codebase/diagram.css");
	}

	public Diagram setContainer(String containerName) {
		this.containerName = containerName;
		return this;
	}

	public void setInputData(List<?> data) {
		this.data = new ArrayList<>();
		this.data.addAll(data);

		JsonArray inputData = transformToJsonInput(containerName, this.data);
		// 根节点具有parent的情况
		Set<String> parentCode = new HashSet<String>();
		inputData.forEach(jv -> parentCode.add(jv.asObject().get("id").asString()));
		inputData.forEach(jv -> {
			JsonValue _parent = jv.asObject().get("parent");
			if (_parent != null && _parent.isString() && !parentCode.contains(_parent.asString())) {
				jv.asObject().remove("parent");
			}
		});

		remoteObject.set("inputData", inputData);
	}

	private JsonArray transformToJsonInput(String cName, List<?> data) {
		// 处理模型
		JsonArray _data = new JsonArray();

		if (data != null)
			data.forEach(o -> {
				JsonObject jo = WidgetToolkit.read(o.getClass(), o, cName, true, true, true, null);
				_data.add(jo);
			});

		return _data;
	}

	public void zoomIn() {
		if (scale > 0.5) {
			scale -= 0.1;
			remoteObject.set("scale", scale);
		}
	}

	public void zoomOut() {
		if (scale < 1.5) {
			scale += 0.1;
			remoteObject.set("scale", scale);
		}
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

	public void addItem(Object item) {
		this.data.add(item);
		JsonObject jo = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, null);
		remoteObject.call("addItem", jo);
	}

	public void addItems(List<?> childs) {
		this.data.addAll(childs);
		// 将childs转换为Json
		JsonArray ja = transformToJsonInput(containerName, childs);
		// remoteObject.call只允许传入JsonObject，因此将childs放入一个JsonObject中传入给前台js
		remoteObject.call("addItems", new JsonObject().add("childs", ja));
	}

	public void updateItem(Object oldItem, Object item) {
		int idx = this.data.indexOf(oldItem);
		if (idx != -1) {
			this.data.remove(idx);
			this.data.add(idx, item);
		}
		JsonObject jo = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, null);
		remoteObject.call("updateItem", jo);
	}

	public void deleteItem(Object item) {
		this.data.remove(item);
		JsonObject jo = WidgetToolkit.read(item.getClass(), item, containerName, true, true, true, null);
		remoteObject.call("removeItem", jo);

	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			remoteObject.destroy();
		}
		super.dispose();
	}

}
