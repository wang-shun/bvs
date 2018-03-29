package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class Folder extends ModelObject {

	public List<Folder> children;

	public String name;

	public String id;

	public List<Folder> getChildren() {
		return children;
	}

	public void setChildren(List<Folder> children) {
		this.children = children;
	}

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

}
