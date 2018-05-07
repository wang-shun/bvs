package com.bizivisionsoft.widgets.pagination;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.Arrays;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.util.WidgetToolkit;

public class Pagination extends Composite {

	private static final String REMOTE_TYPE = "bizvision.pagination";

	private RemoteObject remoteObject;

	private long count;

	private long limit;

	private int currentPage;

	private String[] functionLayout;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String method, JsonObject parameters) {
			currentPage = parameters.get("curr").asInt();
			Event event = new Event();
			event.index = currentPage;
			event.item = Pagination.this;
			event.display = Pagination.this.getDisplay();
			event.data = parameters;
			Display.getCurrent().asyncExec(() -> {
				Arrays.asList(getListeners(SWT.Selection)).forEach(l -> l.handleEvent(event));
			});
		}
	};

	private long groups;

	private Color color;

	/**
	 * 
	 * @param parent
	 * @param style
	 */
	public Pagination(Composite parent, int style) {
		super(parent, SWT.NONE);
		WidgetToolkit.requireWidgetHandlerJs("pagination");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
		String id = getId(this);
		System.out.println(id);
		remoteObject.set("parent", id);
//		remoteObject.set("theme","#3f51b5");

		if ((SWT.LONG & style) != 0) {
			setFunctionLayout(new String[] { "count", "prev", "page", "next", "skip" });
			setGroups(5);
		} else if ((SWT.MEDIUM & style) != 0) {
			setFunctionLayout(new String[] { "count","prev", "page", "next" });
			setGroups(3);
		} else if ((SWT.SHORT & style) != 0) {
			setFunctionLayout(new String[] { "prev", "next" });
			setGroups(3);
		}

	}

	public Pagination setColor(RGB themeColor) {
		checkWidget();
		if (themeColor == null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		String theme = getHtmlColor(themeColor);
		remoteObject.set("theme", theme);
		return this;
	}

	public Color getColor() {
		return color;
	}

	public static String getHtmlColor(RGB rgb) {
		return "#" + hex(rgb.red) + hex(rgb.green) + hex(rgb.blue);
	}

	private static String hex(int color) {
		String i = Integer.toHexString(color);
		if (i.length() == 1) {
			return "0" + i;
		} else {
			return i;
		}
	}

	public Pagination setCount(long count) {
		checkWidget();
		if (count < 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.count = count;
		remoteObject.set("count", count);
		return this;
	}

	public long getCount() {
		return count;
	}

	public Pagination setLimit(long limit) {
		checkWidget();
		if (limit < 1) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.limit = limit;
		remoteObject.set("limit", limit);
		return this;
	}

	public long getLimit() {
		return limit;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public Pagination setFunctionLayout(String[] functionLayout) {
		checkWidget();
		if (functionLayout == null || functionLayout.length == 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.functionLayout = functionLayout;
		JsonArray layouts = new JsonArray();
		Arrays.asList(functionLayout).forEach(l -> layouts.add(l));
		remoteObject.set("layouts", layouts);
		return this;
	}

	public String[] getFunctionLayout() {
		return functionLayout;
	}

	public Pagination setGroups(long groups) {
		checkWidget();
		if (groups < 1) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.groups = groups;
		remoteObject.set("groups", groups);
		return this;
	}

	public long getGroups() {
		return groups;
	}
	
	@Override
	public void dispose() {
	    if( !isDisposed() ) {
	        remoteObject.destroy();
	      }
	    super.dispose();
	}

}
