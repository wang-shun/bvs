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
		Object old = this.description;
		this.description = description;
		firePropertyChange("description", old, this.description);
	}
	
	public String getBundleId() {
		return bundleId;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setBundleId(String bundleId) {
		Object old = this.bundleId;
		this.bundleId = bundleId;
		firePropertyChange("bundleId", old, this.bundleId);
	}
	
	public void setClassName(String className) {
		Object old = this.className;
		this.className = className;
		firePropertyChange("className", old, this.className);

	}

}
