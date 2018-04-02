package com.bizvisionsoft.bruicommons.model;

public class DataSource extends ModelObject {

	private String id;

	private String serviceName;

	private String className;

	private String name;

	private String description;
	
	private boolean list;

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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public boolean isList() {
		return list;
	}
	
	public void setList(boolean list) {
		Object old = this.list;
		this.list = list;
		firePropertyChange("list", old, this.list);
	}

}
