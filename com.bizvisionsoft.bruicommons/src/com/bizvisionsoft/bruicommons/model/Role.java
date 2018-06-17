package com.bizvisionsoft.bruicommons.model;

public class Role extends ModelObject {

	public String name;

	public String id;
	
	private String text;
	
	private String description;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		Object old = this.name;
		this.name = name;
		firePropertyChange("name", old, this.name);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setText(String text) {
		Object old = this.text;
		this.text = text;
		firePropertyChange("text", old, this.text);
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getText() {
		return text;
	}
	
}
