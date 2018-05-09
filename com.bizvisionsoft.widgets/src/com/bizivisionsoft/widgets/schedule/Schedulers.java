package com.bizivisionsoft.widgets.schedule;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class Schedulers extends Composite {

	private static final String REMOTE_TYPE = "bizvision.dhtmlxscheduler";

	private final String widgetName = "dhtmlxscheduler";

	private RemoteObject remoteObject;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String eventCode, JsonObject jo) {

			Display.getCurrent().asyncExec(() -> {

			});
		}

	};

	List<Object> tasks;

	List<Object> links;

	public Schedulers(Composite parent) {
		super(parent, SWT.NONE);

		loadJsLibAndCSS();
		WidgetToolkit.requireWidgetHandlerJs("dhtmlxscheduler");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
		remoteObject.set("parent", getId(this));
	}

	private void loadJsLibAndCSS() {
		WidgetToolkit.requireWidgetJs(widgetName, "codebase/dhtmlxscheduler.js");
		WidgetToolkit.requireWidgetCss(widgetName, "codebase/dhtmlxscheduler_flat.css");

		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 加载插件

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


}
