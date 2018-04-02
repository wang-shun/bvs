package com.bizvisionsoft.bruicommons.model;

public class Formatter extends ModelObject {

	// ʱ���ʽ
	private String dateFormat;

	// ʱ���ʽ
	private String shortDateFormat;

	// ���Ҹ�ʽ
	private String currencyFormat;

	// ���ָ�ʽ
	private String integerFormat;

	// С����ʽ
	private String floatFormat;

	// �ٷֱȸ�ʽ
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
