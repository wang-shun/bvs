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
		this.description = description;

	}

	public int getAlignment() {
		return alignment;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;

	}

	public int getMinimumWidth() {
		return minimumWidth;
	}

	public void setMinimumWidth(int minimumWidth) {
		this.minimumWidth = minimumWidth;

	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;

	}

	public boolean isResizeable() {
		return resizeable;
	}

	public void setResizeable(boolean resizeable) {
		this.resizeable = resizeable;

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
		this.detail = detail;

	}

	public void setSummary(boolean summary) {
		this.summary = summary;

	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;

	}

	public boolean isMarkupEnabled() {
		return markupEnabled;
	}

	public void setMarkupEnabled(boolean markupEnabled) {
		this.markupEnabled = markupEnabled;

	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;

	}

	/**
	 * gantt use!
	 */
	private boolean hide;

	private boolean element;

	private boolean noToggleGridColumnGroup;

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

	public boolean isElement() {
		return this.element;
	}
	
	public void setElement(boolean element) {
		this.element = element;
	}

	public boolean isNoToggleGridColumnGroup() {
		return noToggleGridColumnGroup;
	}
	
	public void setNoToggleGridColumnGroup(boolean noToggleGridColumnGroup) {
		this.noToggleGridColumnGroup = noToggleGridColumnGroup;
	}
}
