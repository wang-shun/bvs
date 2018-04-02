package com.bizvisionsoft.bruicommons.model;

public class Template extends ModelObject {
	
	private String id;
	
	private String name;

	private String description;
	
	private String bundleId;
	
	private String className;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Object old = this.name;
		this.name = name;
		firePropertyChange("name", old, this.name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getBundleId() {
		return bundleId;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	
	public void setClassName(String className) {
		this.className = className;

	}

}
