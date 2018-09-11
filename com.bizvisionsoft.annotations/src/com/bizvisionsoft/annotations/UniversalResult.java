package com.bizvisionsoft.annotations;

public class UniversalResult {

	private String result;
	
	private String targetClassName;

	private boolean list;

	private Object value;

	public void setResult(String result) {
		this.result = result;
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	public String getResult() {
		return result;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public void setList(boolean list) {
		this.list = list;
	}
	
	public boolean isList() {
		return list;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}

}
