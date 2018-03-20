package com.bizivisionsoft.widgets.timeline;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class TimeLine extends ScrolledComposite {

	private static final String REMOTE_TYPE = "bizvision.timeline";

	private RemoteObject remoteObject;

	private JsonArray renderSetting;
	
	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String method, JsonObject parameters) {
			int width = parameters.get("width").asInt();
			int height = parameters.get("height").asInt();
			getContent().setSize(width,height);
			setMinSize(width, height);
		}
	};


	public TimeLine(Composite parent, int style) {
		super(parent, SWT.V_SCROLL|SWT.BORDER);
		setHtmlAttribute("name", "timeline");
		setExpandHorizontal(true);
		setExpandVertical(true);
		Composite content = new Composite(this,SWT.NONE);
		setContent(content);
		parent.addListener(SWT.Resize, e -> {
			setMinSize(parent.getSize());
			remoteObject.set("renderSetting", renderSetting);
		});
		
		WidgetToolkit.requireWidgetHandlerJs("timeline");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.set("parent", getId(content));
		remoteObject.setHandler(operationHandler);
		renderSetting = new JsonArray();
		remoteObject.set("renderSetting", renderSetting);
	}

	public TimeLine append(String title, String content) {
		renderSetting.add(new JsonObject().add("title", title).add("content", content));
		remoteObject.set("renderSetting", renderSetting);
		return this;
	}

}
