package com.bizvisionsoft.bruicommons.model;

public class Headbar extends ModelObject {

	private boolean enabled;
	private Integer height;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		Object old = this.enabled;
		this.enabled = enabled;
		firePropertyChange("enabled", old, this.enabled);
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		Object old = this.height;
		this.height = height;
		firePropertyChange("height", old, this.height);
	}

}
