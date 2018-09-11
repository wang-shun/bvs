package com.bizvisionsoft.annotations;

import java.util.HashMap;

public class UniversalCommand {

	public static final String PARAM_TARGET_CLASS = "targetClass";

	private String targetClassName;

	private HashMap<String, Object> parameters = new HashMap<>();

	public void addParameter(String name, Object value) {
		parameters.put(name, value);
	}

	public Object getParameter(String name) {
		return parameters.get(name);
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

}
