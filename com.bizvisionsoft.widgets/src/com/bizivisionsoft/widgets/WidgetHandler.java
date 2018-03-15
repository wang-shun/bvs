package com.bizivisionsoft.widgets;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.SingletonManager;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.bizivisionsoft.widgets.gesture.GestureAction;
import com.bizivisionsoft.widgets.gesture.GestureEvent;
import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.google.gson.GsonBuilder;

public class WidgetHandler {

	public static final String EVENT_SLIDING = "sliding";

	public static final String EVENT_RENDERHTML = "renderHtml";

	private static final String REMOTE_TYPE = "bizvision.widgethandler";

	private final RemoteObject remoteObject;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		public void handleCall(String method, JsonObject parameters) {
			if (GestureEvent.event.equalsIgnoreCase(method)) {
				String eventCode = parameters.get("event").asString();
				String data = parameters.get("data").asString();
				fireEventListener(eventCode, data);
			} else if (EVENT_SLIDING.equalsIgnoreCase(method)) {
				String data = parameters.get("data").asString();
				fireEventListener(EVENT_SLIDING, data);
			} else if (EVENT_RENDERHTML.equalsIgnoreCase(method)) {
				int height = parameters.get("height").asInt();
				fireEventListener(EVENT_RENDERHTML, height);
			}
		}
	};

	private Map<String, ListenerList<Listener>> listenersMap;

	private String name;

	WidgetHandler(Widget widget) {
		this();
		name = getName(widget);
		WidgetToolkit.setAttribute(widget, "name", name);
		remoteObject.set("name", name);
	}

	public static WidgetHandler getHandler(Widget widget) {
		if(widget==null) {
			UISession uiSession = RWT.getUISession();
			return SingletonManager.getInstance(uiSession).getSingleton(WidgetHandler.class);
		}
		
		Object wh = widget.getData("widgetHandler");
		if (wh == null) {
			wh = new WidgetHandler(widget);
			widget.setData("widgetHandler", wh);
		}
		return (WidgetHandler) wh;
	}
	
	public static WidgetHandler getHandler() {
		return getHandler(null);
	}

	public static String getName(Widget widget) {
		WidgetHandler handler = (WidgetHandler) widget.getData("widgetHandler");
		if (handler != null) {
			return handler.getName();
		}
		String name = (String) widget.getData("widgetHandlerName");
		if (name != null) {
			return name;
		}
		name = widget.getClass().getSimpleName().toLowerCase() + "_" + WidgetToolkit.getRandomString(4, true);
		widget.setData("widgetHandlerName", name);
		return name;
	}

	public void fireEventListener(String eventCode, Object data) {
		if (listenersMap == null) {
			return;
		}
		ListenerList<Listener> listenerList = listenersMap.get(eventCode);
		if (listenerList == null || listenerList.isEmpty()) {
			return;
		}
		for (int i = 0; i < listenerList.size(); i++) {
			Listener listener = (Listener) listenerList.getListeners()[i];
			Event event = new Event();
			event.data = data;
			listener.handleEvent(event);
		}
	}

	/**
	 * create a globe widget handler
	 */
	public WidgetHandler() {
		WidgetToolkit.requireWidgetsJs("widgethandler");
		Connection connection = RWT.getUISession().getConnection();
		remoteObject = connection.createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
	}

	public void setClass(String className) {
		remoteObject.set("className", className);
	}

	public void setDisabledClass(String className) {
		remoteObject.set("disabledClassName", className);
	}

	public void setHtmlContent(String htmlContent) {
		remoteObject.set("htmlContent", htmlContent);
	}

	/**
	 * 改变action目标的状态 state: show,hide
	 * 
	 * @param state
	 */
	public void setTargetState(String state) {
		remoteObject.set("targetState", state);
	}

	public void addEventListener(String event, Listener listener) {
		if (listenersMap == null) {
			listenersMap = new HashMap<String, ListenerList<Listener>>();
		}
		ListenerList<Listener> listeners = listenersMap.get(event);
		if (listeners == null) {
			listeners = new ListenerList<Listener>();
			listenersMap.put(event, listeners);
			remoteObject.call("addEventListener", new JsonObject().set("event", event));
		}
		listeners.add(listener);
	}

	public void removeEventListener(String event, Listener listener) {
		if (listenersMap == null) {
			return;
		}
		ListenerList<Listener> listeners = listenersMap.get(event);
		if (listeners == null) {
			return;
		}
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			remoteObject.call("removeEventListener", new JsonObject().set("event", event));
		}
	}

	public void removeGestureAction(GestureAction gestureAction) {
		Assert.isNotNull(gestureAction);
		String json = new GsonBuilder().create().toJson(gestureAction);
		JsonObject data = JsonObject.readFrom(json);
		remoteObject.call("removeGestureAction", data);
	}

	public void addGestureAction(GestureAction gestureAction) {
		Assert.isNotNull(gestureAction);
		String json = new GsonBuilder().create().toJson(gestureAction);
		JsonObject data = JsonObject.readFrom(json);
		remoteObject.call("addGestureAction", data);
	}

	public String getName() {
		return name;
	}

	public void show() {
		remoteObject.call("animateElement", new JsonObject().set("width", "show"));
	}

	public void hide() {
		remoteObject.call("animateElement", new JsonObject().set("width", "hide"));
	}

	/**
	 * 改变Left
	 */
	public void slideLeft(int xOffset) {
		remoteObject.call("animate", new JsonObject().set("left", "" + xOffset + "px"));
	}

	/**
	 * 与另一个控件一起移动进入
	 * 
	 * @param other
	 * @param width
	 */
	public void sliding(String previous, String width) {
		remoteObject.call(EVENT_SLIDING, new JsonObject().set("previous", previous).set("width", width));
	}

}
