package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class Column extends ModelObject {

	private String id;

	private String name;

	private String description;

	private String text;

	private int alignment;

	private int width;

	private int minimumWidth;

	private boolean moveable;

	private boolean resizeable;

	private boolean detail;

	private boolean summary;

	private boolean expanded;

	private List<Column> columns;

	private boolean markupEnabled;

	private String format;
	
	private int sort;

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

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

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		Object old = this.alignment;
		this.alignment = alignment;
		firePropertyChange("alignment", old, this.alignment);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		Object old = this.width;
		this.width = width;
		firePropertyChange("width", old, this.width);
	}

	public int getMinimumWidth() {
		return minimumWidth;
	}

	public void setMinimumWidth(int minimumWidth) {
		Object old = this.minimumWidth;
		this.minimumWidth = minimumWidth;
		firePropertyChange("minimumWidth", old, this.minimumWidth);
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		Object old = this.moveable;
		this.moveable = moveable;
		firePropertyChange("moveable", old, this.moveable);
	}

	public boolean isResizeable() {
		return resizeable;
	}

	public void setResizeable(boolean resizeable) {
		Object old = this.resizeable;
		this.resizeable = resizeable;
		firePropertyChange("resizeable", old, this.resizeable);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		Object old = this.text;
		this.text = text;
		firePropertyChange("text", old, this.text);
	}

	public boolean isDetail() {
		return detail;
	}

	public boolean isSummary() {
		return summary;
	}

	public void setDetail(boolean detail) {
		Object old = this.detail;
		this.detail = detail;
		firePropertyChange("detail", old, this.detail);
	}

	public void setSummary(boolean summary) {
		Object old = this.summary;
		this.summary = summary;
		firePropertyChange("summary", old, this.summary);
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		Object old = this.expanded;
		this.expanded = expanded;
		firePropertyChange("expanded", old, this.expanded);
	}

	public boolean isMarkupEnabled() {
		return markupEnabled;
	}

	public void setMarkupEnabled(boolean markupEnabled) {
		Object old = this.markupEnabled;
		this.markupEnabled = markupEnabled;
		firePropertyChange("markupEnabled", old, this.markupEnabled);
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		Object old = this.format;
		this.format = format;
		firePropertyChange("format", old, this.format);
	}

	/**
	 * gantt use!
	 */
	private boolean hide;

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
	
	public int getSort() {
		return sort;
	}
	
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
