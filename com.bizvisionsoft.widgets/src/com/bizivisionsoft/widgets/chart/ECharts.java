package com.bizivisionsoft.widgets.chart;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.google.gson.GsonBuilder;

public class ECharts extends Composite {

	private static final String REMOTE_TYPE = "bizvision.echarts";

	private RemoteObject remoteObject;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String method, JsonObject parameters) {
		}
	};

	private JsonValue option;

	/**
	 * 
	 * @param parent
	 * @param style
	 */
	public ECharts(Composite parent, int style) {
		super(parent, style);
		WidgetToolkit.requireWidgetHandlerJs("echarts");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
		remoteObject.set("parent", getId(this));
//		option = new JsonValue();
	}

	public JsonValue getOption() {
		return option;
	}

	public void setOption(JsonValue option) {
		checkWidget();
		if (option == null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.option = option;
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		System.out.println(gson.toJson(gson.fromJson(this.option.toString(), HashMap.class)));
		remoteObject.set("option", option);
	}
	
	public void setInput(Object input) {
		String json = new GsonBuilder().create().toJson(input);
		option = JsonObject.readFrom(json);
	}

	@Override
	public void dispose() {
		if (!isDisposed()) {
			remoteObject.destroy();
		}
		super.dispose();
	}

}
