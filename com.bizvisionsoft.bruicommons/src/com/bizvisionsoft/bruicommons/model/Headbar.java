package com.bizvisionsoft.bruicommons.model;

import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;

public class Headbar extends ModelObject {
	

	@Override
	@ReadValue(ReadValue.TYPE)
	@Label
	public String toString() {
		return "¶¥À¸"+(enabled?"":" [½ûÓÃ]");
	}
	
	@Behavior("±à¼­")
	private boolean behavior = true;

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
		this.height = height;
	}

}
