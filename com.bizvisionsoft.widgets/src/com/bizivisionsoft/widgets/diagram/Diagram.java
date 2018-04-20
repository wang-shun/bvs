package com.bizivisionsoft.widgets.diagram;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class Diagram extends Composite {

	private static final String REMOTE_TYPE = "bizvision.diagram";

	private final String widgetName = "diagram";

	private JsonArray inputData;

	private RemoteObject remoteObject;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String eventCode, JsonObject jo) {

			Display.getCurrent().asyncExec(() -> {

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

		inputData = transformToJsonInput(containerName, this.data);
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

	public void addEventListener(String eventCode, Listener listener) {
		// TODO Auto-generated method stub

	}

	public void zoomOut() {
		if (scale > 0) {
			scale -= 0.1;
			remoteObject.set("scale", scale);
		}
	}

	public void zoomIn() {
		if (scale < 2) {
			scale += 0.1;
			remoteObject.set("scale", scale);
		}
	}

}
