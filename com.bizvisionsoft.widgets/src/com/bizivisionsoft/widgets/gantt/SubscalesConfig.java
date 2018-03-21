package com.bizivisionsoft.widgets.gantt;

public class SubscalesConfig {

	public String css;// (function) a function that returns a name of a CSS class that will be applied
						// to the scale units. Takes a date object as a parameter
	public String format;// (string) the format of the scale's labels
	public String step;// (string) the scale's step. By default, 1.
	// public String template;// (function) the template of the scale's labels.
	// Takes a date object as a
	// // parameter
	public String unit;// ("minute", "hour", "day", "week", "month", "year") the scale's unit. By
						// default, "day"
	
	public String date;

	public SubscalesConfig setCss(String css) {
		this.css = css;
		return this;
	}

	public SubscalesConfig setFormat(String format) {
		this.format = format;
		return this;
	}

	public SubscalesConfig setStep(String step) {
		this.step = step;
		return this;
	}

	public SubscalesConfig setUnit(String unit) {
		this.unit = unit;
		return this;
	}
	
	public SubscalesConfig setDate(String date) {
		this.date = date;
		return this;
	}

}
