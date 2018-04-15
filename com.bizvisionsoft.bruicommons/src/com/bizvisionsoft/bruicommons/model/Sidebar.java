package com.bizvisionsoft.bruicommons.model;

import java.util.List;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;

public class Sidebar extends ModelObject {
	
	
	@Override
	@ReadValue(ReadValue.TYPE)
	@Label
	public String toString() {
		return "²à±ßÀ¸"+(enabled?"":" [½ûÓÃ]");
	}
	
	@Behavior("±à¼­")
	private boolean behavior = true;
	
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
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

}
