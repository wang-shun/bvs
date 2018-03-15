package com.bizvisionsoft.bruicommons.model;

public class Formatter extends ModelObject {

	// 时间格式
	private String dateFormat;

	// 时间格式
	private String shortDateFormat;

	// 货币格式
	private String currencyFormat;

	// 数字格式
	private String integerFormat;

	// 小数格式
	private String floatFormat;

	// 百分比格式
	private String percentageFormat;

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		Object old = this.dateFormat;
		this.dateFormat = dateFormat;
		firePropertyChange("dateFormat", old, this.dateFormat);
	}

	public String getCurrencyFormat() {
		return currencyFormat;
	}

	public void setCurrencyFormat(String currencyFormat) {
		Object old = this.currencyFormat;
		this.currencyFormat = currencyFormat;
		firePropertyChange("currencyFormat", old, this.currencyFormat);
	}

	public String getIntegerFormat() {
		return integerFormat;
	}

	public void setIntegerFormat(String integerFormat) {
		Object old = this.integerFormat;
		this.integerFormat = integerFormat;
		firePropertyChange("integerFormat", old, this.integerFormat);
	}

	public String getFloatFormat() {
		return floatFormat;
	}

	public void setFloatFormat(String floatFormat) {
		Object old = this.floatFormat;
		this.floatFormat = floatFormat;
		firePropertyChange("floatFormat", old, this.floatFormat);
	}

	public String getPercentageFormat() {
		return percentageFormat;
	}

	public void setPercentageFormat(String percentageFormat) {
		Object old = this.percentageFormat;
		this.percentageFormat = percentageFormat;
		firePropertyChange("percentageFormat", old, this.percentageFormat);
	}

	public String getShortDateFormat() {
		return shortDateFormat;
	}

	public void setShortDateFormat(String shortDateFormat) {
		Object old = this.shortDateFormat;
		this.shortDateFormat = shortDateFormat;
		firePropertyChange("shortDateFormat", old, this.shortDateFormat);
	}

}
