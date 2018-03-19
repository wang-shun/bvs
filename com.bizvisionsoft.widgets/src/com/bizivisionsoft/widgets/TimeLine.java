package com.bizivisionsoft.widgets;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class TimeLine extends Composite {

	private static final String REMOTE_TYPE = "bizvision.timeline";

	private RemoteObject remoteObject;

	private JsonArray renderSetting;

	public TimeLine(Composite parent, int style) {
		super(parent, style);
		WidgetToolkit.requireWidgetsJs("timeline");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.set("parent", getId(this));
		renderSetting = new JsonArray();
		remoteObject.set("renderSetting", renderSetting);
	}

	public TimeLine append(String title, String content) {
		renderSetting.add(new JsonObject().add("title", title).add("content", content));
		remoteObject.set("renderSetting", renderSetting);
		return this;
	}

}
