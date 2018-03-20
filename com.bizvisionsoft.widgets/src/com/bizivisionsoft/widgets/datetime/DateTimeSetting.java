package com.bizivisionsoft.widgets.datetime;

import java.util.HashMap;
import java.util.Map;

public class DateTimeSetting {

	public static final String TYPE_YEAR = "year";

	public static final String FORMAT_YEAR = "yyyy";

	public static final String TYPE_MONTH = "month";

	public static final String FORMAT_MONTH = "MM";

	public static final String TYPE_DATE = "date";

	public static final String FORMAT_DATE = "yyyy-MM-dd";

	public static final String TYPE_TIME = "time";

	public static final String FORMAT_TIME = "HH:mm:ss";

	public static final String TYPE_DATETIME = "datetime";

	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	private String type;

	private Object range;

	private String format;

	private String value;

	private Object max;

	private Object min;

	private Boolean showBottom;

	private String theme;

	private Map<String, String> mark;

	private Boolean calendar;

	public String getType() {
		return type;
	}

	public DateTimeSetting setType(String type) {
		this.type = type;
		return this;
	}

	public Object getRange() {
		return range;
	}

	public DateTimeSetting setRange(boolean range) {
		this.range = range;
		return this;
	}

	public String getFormat() {
		return format;
	}

	public DateTimeSetting setFormat(String format) {
		this.format = format;
		return this;
	}

	public String getValue() {
		return value;
	}

	public DateTimeSetting setValue(String value) {
		this.value = value;
		return this;
	}

	public Object getMax() {
		return max;
	}
	
	public Object getMin() {
		return min;
	}

	public DateTimeSetting setMax(Object max) {
		this.max = max;
		return this;
	}

	public DateTimeSetting setMin(Object min) {
		this.min = min;
		return this;
	}

	public boolean isShowBottom() {
		return showBottom;
	}

	public DateTimeSetting setShowBottom(boolean showBottom) {
		this.showBottom = showBottom;
		return this;
	}

	public String getTheme() {
		return theme;
	}

	public DateTimeSetting setTheme(String theme) {
		this.theme = theme;
		return this;
	}

	public Map<String, String> getMark() {
		return mark;
	}

	public DateTimeSetting setMark(Map<String, String> mark) {
		this.mark = mark;
		return this;
	}

	public DateTimeSetting addMark(String date, String note) {
		if (this.mark == null) {
			this.mark = new HashMap<String, String>();
		}
		this.mark.put(date, note);
		return this;
	}

	public boolean isCalendar() {
		return calendar;
	}

	public DateTimeSetting setCalendar(Boolean calendar) {
		this.calendar = calendar;
		return this;
	}

	public static DateTimeSetting date() {
		return new DateTimeSetting().setType(TYPE_DATE).setTheme("#3f51b5").setFormat(FORMAT_DATE);
	}

	public static DateTimeSetting time() {
		return new DateTimeSetting().setType(TYPE_TIME).setTheme("#3f51b5").setFormat(FORMAT_TIME);
	}

	public static DateTimeSetting month() {
		return new DateTimeSetting().setType(TYPE_MONTH).setTheme("#3f51b5").setFormat(FORMAT_MONTH);
	}

	public static DateTimeSetting dateTime() {
		return new DateTimeSetting().setType(TYPE_DATETIME).setTheme("#3f51b5").setFormat(FORMAT_DATETIME);
	}

	public static DateTimeSetting year() {
		return new DateTimeSetting().setType(TYPE_YEAR).setTheme("#3f51b5").setFormat(FORMAT_YEAR);
	}

}
