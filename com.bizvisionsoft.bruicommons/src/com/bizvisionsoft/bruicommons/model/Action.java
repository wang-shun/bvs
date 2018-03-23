package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class Action extends ModelObject {

	private String id;

	private String name;

	private String text;

	private String description;

	private String tooltips;

	private String image;

	private String imageDisabled;

	private String bundleId;

	private String className;

	private String switchContentToAssemblyId;

	private boolean propagate;

	private boolean forceText;

	private String style;

	private List<Action> children;

	private String editorAssemblyId;

	private boolean editorAssemblyEditable;

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

	public String getImage() {
		return image;
	}

	public String getImageDisabled() {
		return imageDisabled;
	}

	public String getTooltips() {
		return tooltips;
	}

	public void setTooltips(String tooltips) {
		Object old = this.tooltips;
		this.tooltips = tooltips;
		firePropertyChange("tooltips", old, this.tooltips);
	}

	public void setImage(String image) {
		Object old = this.image;
		this.image = image;
		firePropertyChange("image", old, this.image);
	}

	public void setImageDisabled(String imageDisabled) {
		Object old = this.imageDisabled;
		this.imageDisabled = imageDisabled;
		firePropertyChange("imageDisabled", old, this.imageDisabled);
	}

	public List<Action> getChildren() {
		return children;
	}

	public void setChildren(List<Action> children) {
		this.children = children;
	}

	public String getBundleId() {
		return bundleId;
	}

	public String getClassName() {
		return className;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		Object old = this.text;
		this.text = text;
		firePropertyChange("text", old, this.text);
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

	public String getSwitchContentToAssemblyId() {
		return switchContentToAssemblyId;
	}

	public void setSwitchContentToAssemblyId(String switchContentToAssemblyId) {
		Object old = this.className;
		this.switchContentToAssemblyId = switchContentToAssemblyId;
		firePropertyChange("switchContentToAssemblyId", old, this.switchContentToAssemblyId);
	}

	public boolean isRunnable() {
		if (switchContentToAssemblyId != null && !switchContentToAssemblyId.isEmpty())
			return true;
		if (className != null && !className.isEmpty() && bundleId != null && !bundleId.isEmpty())
			return true;

		return false;
	}

	public void setPropagate(boolean propagate) {
		Object old = this.propagate;
		this.propagate = propagate;
		firePropertyChange("propagate", old, this.propagate);
	}

	public boolean isPropagate() {
		return propagate;
	}

	public void setForceText(boolean forceText) {
		Object old = this.forceText;
		this.forceText = forceText;
		firePropertyChange("forceText", old, this.forceText);
	}

	public boolean isForceText() {
		return forceText;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		Object old = this.style;
		this.style = style;
		firePropertyChange("style", old, this.style);
	}

	public String getEditorAssemblyId() {
		return editorAssemblyId;
	}

	public void setEditorAssemblyId(String editorAssemblyId) {
		Object old = this.editorAssemblyId;
		this.editorAssemblyId = editorAssemblyId;
		firePropertyChange("editorAssemblyId", old, this.editorAssemblyId);
	}

	public boolean isEditorAssemblyEditable() {
		return editorAssemblyEditable;
	}

	public void setEditorAssemblyEditable(boolean editorAssemblyEditable) {
		Object old = this.editorAssemblyEditable;
		this.editorAssemblyEditable = editorAssemblyEditable;
		firePropertyChange("editorAssemblyEditable", old, this.editorAssemblyEditable);
	}

	
	@Override
	public String toString() {
		return name;
	}
}
