package com.bizivisionsoft.widgets.datetime;

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.OperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.google.gson.GsonBuilder;

public class DateTimeSelector extends Widget {

	private static final String REMOTE_TYPE = "bizvision.datetime";

	private RemoteObject remoteObject;

	private final OperationHandler operationHandler = new AbstractOperationHandler() {

		@Override
		public void handleCall(String method, JsonObject parameters) {
			if ("ready".equals(method)) {
				// fireReady(parameters);
			} else if ("done".equals(method)) {
				fireEvent(parameters, SWT.Modify);
			} else if ("change".equals(method)) {
				fireEvent(parameters, SWT.Selection);
			}
		}
	};

	private Date date1;

	private Date date2;

	private DateTimeSetting setting;

	private Widget parent;

	/**
	 * 
	 * @param parent
	 * @param style
	 */
	public DateTimeSelector(Widget parent) {
		super(parent, SWT.NONE);
		this.parent = parent;
		WidgetToolkit.requireWidgetHandlerJs("datetime");
		remoteObject = RWT.getUISession().getConnection().createRemoteObject(REMOTE_TYPE);
		remoteObject.setHandler(operationHandler);
		remoteObject.set("bindTo", getId(parent));
		String json = new GsonBuilder().create().toJson(setting);
		remoteObject.set("renderSetting", JsonValue.readFrom(json));
	}

	

	public Date getDate() {
		return date1;
	}

	public Date getEndDate() {
		return date2;
	}

	private Event createEvent(JsonObject parameters) {
		if (parent.isDisposed()) {
			return null;
		}
		date1 = parseDate(parameters.get("date").asObject());
		date2 = parseDate(parameters.get("endDate").asObject());
		Event event = new DateTimeEvent(date1, date2);
		event.item = parent;
		event.display = parent.getDisplay();
		return event;
	}

	private Date parseDate(JsonObject dateValue) {
		if (dateValue.isEmpty())
			return null;
		String type = setting.getType();
		if (DateTimeSetting.TYPE_DATE.equals(type)) {
			return parseDateValue(dateValue);
		} else if (DateTimeSetting.TYPE_DATETIME.equals(type)) {
			return parseDateTimeValue(dateValue);
		} else if (DateTimeSetting.TYPE_YEAR.equals(type)) {
			return parseYearValue(dateValue);
		} else if (DateTimeSetting.TYPE_MONTH.equals(type)) {
			return parseMonthValue(dateValue);
		} else if (DateTimeSetting.TYPE_TIME.equals(type)) {
			return parseTimeValue(dateValue);
		} else {
			return null;
		}

	}

	private Date parseTimeValue(JsonObject dateValue) {
		Calendar cal = Calendar.getInstance(RWT.getLocale());
		int hour = dateValue.get("hours").asInt();
		cal.set(Calendar.HOUR_OF_DAY, hour);

		int minutes = dateValue.get("minutes").asInt();
		cal.set(Calendar.MINUTE, minutes);

		int seconds = dateValue.get("seconds").asInt();
		cal.set(Calendar.SECOND, seconds);

		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	private Date parseMonthValue(JsonObject dateValue) {
		Calendar cal = Calendar.getInstance(RWT.getLocale());
		int year = dateValue.get("year").asInt();
		cal.set(Calendar.YEAR, year);

		int month = dateValue.get("month").asInt() - 1;
		cal.set(Calendar.MONTH, month);

		cal.set(Calendar.DATE, 1);

		cal.set(Calendar.HOUR_OF_DAY, 0);

		cal.set(Calendar.MINUTE, 0);

		cal.set(Calendar.SECOND, 0);

		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	private Date parseYearValue(JsonObject dateValue) {
		Calendar cal = Calendar.getInstance(RWT.getLocale());
		int year = dateValue.get("year").asInt();
		cal.set(Calendar.YEAR, year);

		cal.set(Calendar.MONTH, 0);

		cal.set(Calendar.DATE, 1);

		cal.set(Calendar.HOUR_OF_DAY, 0);

		cal.set(Calendar.MINUTE, 0);

		cal.set(Calendar.SECOND, 0);

		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	private Date parseDateTimeValue(JsonObject dateValue) {
		Calendar cal = Calendar.getInstance(RWT.getLocale());
		int year = dateValue.get("year").asInt();
		cal.set(Calendar.YEAR, year);

		int month = dateValue.get("month").asInt();
		cal.set(Calendar.MONTH, month - 1);

		int date = dateValue.get("date").asInt();
		cal.set(Calendar.DATE, date);

		int hour = dateValue.get("hours").asInt();
		cal.set(Calendar.HOUR_OF_DAY, hour);

		int minutes = dateValue.get("minutes").asInt();
		cal.set(Calendar.MINUTE, minutes);

		int seconds = dateValue.get("seconds").asInt();
		cal.set(Calendar.SECOND, seconds);

		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	private Date parseDateValue(JsonObject dateValue) {
		Calendar cal = Calendar.getInstance(RWT.getLocale());
		int year = dateValue.get("year").asInt();
		cal.set(Calendar.YEAR, year);

		int month = dateValue.get("month").asInt();
		cal.set(Calendar.MONTH, month - 1);

		int date = dateValue.get("date").asInt();
		cal.set(Calendar.DATE, date);

		cal.set(Calendar.HOUR_OF_DAY, 0);

		cal.set(Calendar.MINUTE, 0);

		cal.set(Calendar.SECOND, 0);

		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	protected void fireEvent(JsonObject parameters, int eventType) {
		Optional.ofNullable(createEvent(parameters)).ifPresent(event -> {
			Display.getCurrent().asyncExec(() -> {
				Arrays.asList(getListeners(eventType)).forEach(l -> l.handleEvent(event));
			});
		});
	}

	public void dispose() {
		if (!isDisposed()) {
			remoteObject.destroy();
			super.dispose();
		}
	}

	public void show(DateTimeSetting dateTimeSetting) {
		String json = new GsonBuilder().create().toJson(setting);
		remoteObject.set("renderSetting", JsonValue.readFrom(json));
	}


}
