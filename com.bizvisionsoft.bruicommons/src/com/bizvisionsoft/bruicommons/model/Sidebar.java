package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class Sidebar extends ModelObject {

	private boolean enabled;

	private Integer width;

	private String header;

	private List<Action> toolbarItems;

	private List<Action> sidebarItems;
	
	
	public List<Action> getSidebarItems() {
		return sidebarItems;
	}
	
	public void setSidebarItems(List<Action> sidebarItems) {
		this.sidebarItems = sidebarItems;
	}
	
	public List<Action> getToolbarItems() {
		return toolbarItems;
	}
	
	public void setToolbarItems(List<Action> toolbarItems) {
		this.toolbarItems = toolbarItems;
	}
	

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		Object old = this.enabled;
		this.enabled = enabled;
		firePropertyChange("enabled", old, this.enabled);
	}

	public void setHeader(String header) {
		Object old = this.header;
		this.header = header;
		firePropertyChange("header", old, this.header);
	}

	public String getHeader() {
		return header;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		Object old = this.width;
		this.width = width;
		firePropertyChange("width", old, this.width);
	}

}
