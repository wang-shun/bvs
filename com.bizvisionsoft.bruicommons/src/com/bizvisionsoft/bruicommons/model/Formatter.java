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
		this.dateFormat = dateFormat;
	}

	public String getCurrencyFormat() {
		return currencyFormat;
	}

	public void setCurrencyFormat(String currencyFormat) {
		this.currencyFormat = currencyFormat;
	}

	public String getIntegerFormat() {
		return integerFormat;
	}

	public void setIntegerFormat(String integerFormat) {
		this.integerFormat = integerFormat;
	}

	public String getFloatFormat() {
		return floatFormat;
	}

	public void setFloatFormat(String floatFormat) {
		this.floatFormat = floatFormat;
	}

	public String getPercentageFormat() {
		return percentageFormat;
	}

	public void setPercentageFormat(String percentageFormat) {
		this.percentageFormat = percentageFormat;
	}

	public String getShortDateFormat() {
		return shortDateFormat;
	}

	public void setShortDateFormat(String shortDateFormat) {
		this.shortDateFormat = shortDateFormat;
	}

}
