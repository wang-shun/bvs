package com.bizvisionsoft.bruicommons.model;

import java.util.List;

import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

public class Column extends ModelObject {

	@ReadValue
	private String id;

	@ReadValue
	@WriteValue
	private String name;

	@ReadValue
	@WriteValue
	private String description;

	@ReadValue
	@WriteValue
	private String text;

	@ReadValue
	@WriteValue
	private int alignment;

	@ReadValue
	@WriteValue
	private int width;

	@ReadValue
	@WriteValue
	private int minimumWidth;

	@ReadValue
	@WriteValue
	private boolean moveable;

	@ReadValue
	@WriteValue
	private boolean resizeable;

	@ReadValue
	@WriteValue
	private boolean detail;

	@ReadValue
	@WriteValue
	private boolean summary;

	@ReadValue
	@WriteValue
	private boolean expanded;

	@ReadValue
	@WriteValue
	private List<Column> columns;

	@ReadValue
	@WriteValue
	private boolean markupEnabled;

	@ReadValue
	@WriteValue
	private String format;

	@ReadValue
	@WriteValue
	private int sort;
	
	@ReadValue
	@WriteValue
	private boolean forceDisplayZero;
	
	@ReadValue
	@WriteValue
	private String negativeStyle;
	
	@ReadValue
	@WriteValue
	private String postiveStyle;
	
	@ReadValue
	@WriteValue
	private String gt1Style;
	
	@ReadValue
	@WriteValue
	private String lt1Style;

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

	private String tooltipText;

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

	public boolean isForceDisplayZero() {
		return forceDisplayZero;
	}
	
	public void setForceDisplayZero(boolean forceDisplayZero) {
		this.forceDisplayZero = forceDisplayZero;
	}
	
	public void setNegativeStyle(String negativeStyle) {
		this.negativeStyle = negativeStyle;
	}
	
	public String getNegativeStyle() {
		return negativeStyle;
	}
	
	public String getGt1Style() {
		return gt1Style;
	}
	
	public void setGt1Style(String gt1Style) {
		this.gt1Style = gt1Style;
	}
	
	public String getPostiveStyle() {
		return postiveStyle;
	}
	
	public void setPostiveStyle(String postiveStyle) {
		this.postiveStyle = postiveStyle;
	}
	
	public String getLt1Style() {
		return lt1Style;
	}
	
	public void setLt1Style(String lt1Style) {
		this.lt1Style = lt1Style;
	}

	public String getTooltipText() {
		return tooltipText;
	}
	
	public void setTooltipText(String tooltipText) {
		this.tooltipText = tooltipText;
	}
}
