package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class Layout extends ModelObject {

	private String id;

	private String name;

	private String description;

	private int minimalDeviceWidth;

	private int maximalDeviceWidth;

	private int columnCount;

	private int horizontalSpacing;

	private int verticalSpacing;

	private boolean makeColumnsEqualWidth;
	
	private boolean extendHorizontalSpace;

	private boolean extendVerticalSpace;

	private int marginBottom;

	private int marginHeight;

	private int marginLeft;

	private int marginRight;

	private int marginTop;

	private int marginWidth;
	
	private String css;
	
	private String rwtCss;

	private List<AssemblyLayouted> assemblys;

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

	public int getMinimalDeviceWidth() {
		return minimalDeviceWidth;
	}

	public void setMinimalDeviceWidth(int minimalDeviceWidth) {
		Object old = this.minimalDeviceWidth;
		this.minimalDeviceWidth = minimalDeviceWidth;
		firePropertyChange("minimalDeviceWidth", old, this.minimalDeviceWidth);
	}

	public int getMaximalDeviceWidth() {
		return maximalDeviceWidth;
	}

	public void setMaximalDeviceWidth(int maximalDeviceWidth) {
		Object old = this.maximalDeviceWidth;
		this.maximalDeviceWidth = maximalDeviceWidth;
		firePropertyChange("maximalDeviceWidth", old, this.maximalDeviceWidth);
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		Object old = this.columnCount;
		this.columnCount = columnCount;
		firePropertyChange("columnCount", old, this.columnCount);
	}

	public List<AssemblyLayouted> getAssemblys() {
		return assemblys;
	}

	public void setAssemblys(List<AssemblyLayouted> assemblys) {
		this.assemblys = assemblys;
	}

	public int getHorizontalSpacing() {
		return horizontalSpacing;
	}

	public boolean isMakeColumnsEqualWidth() {
		return makeColumnsEqualWidth;
	}

	public int getMarginBottom() {
		return marginBottom;
	}

	public int getMarginHeight() {
		return marginHeight;
	}

	public int getMarginLeft() {
		return marginLeft;
	}

	public int getMarginRight() {
		return marginRight;
	}

	public int getMarginTop() {
		return marginTop;
	}

	public int getMarginWidth() {
		return marginWidth;
	}
	
	public int getVerticalSpacing() {
		return verticalSpacing;
	}
	
	public boolean isExtendHorizontalSpace() {
		return extendHorizontalSpace;
	}
	
	public boolean isExtendVerticalSpace() {
		return extendVerticalSpace;
	}
	
	public void setExtendHorizontalSpace(boolean extendHorizontalSpace) {
		Object old = this.extendHorizontalSpace;
		this.extendHorizontalSpace = extendHorizontalSpace;
		firePropertyChange("extendHorizontalSpace", old, this.extendHorizontalSpace);
	}
	
	public void setExtendVerticalSpace(boolean extendVerticalSpace) {
		Object old = this.extendVerticalSpace;
		this.extendVerticalSpace = extendVerticalSpace;
		firePropertyChange("extendVerticalSpace", old, this.extendVerticalSpace);
	}
	
	public void setMakeColumnsEqualWidth(boolean makeColumnsEqualWidth) {
		Object old = this.makeColumnsEqualWidth;
		this.makeColumnsEqualWidth = makeColumnsEqualWidth;
		firePropertyChange("makeColumnsEqualWidth", old, this.makeColumnsEqualWidth);
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		Object old = this.horizontalSpacing;
		this.horizontalSpacing = horizontalSpacing;
		firePropertyChange("horizontalSpacing", old, this.horizontalSpacing);
	}

	public void setMarginBottom(int marginBottom) {
		Object old = this.marginBottom;
		this.marginBottom = marginBottom;
		firePropertyChange("marginBottom", old, this.marginBottom);
	}

	public void setMarginHeight(int marginHeight) {
		Object old = this.marginHeight;
		this.marginHeight = marginHeight;
		firePropertyChange("marginHeight", old, this.marginHeight);
	}

	public void setMarginLeft(int marginLeft) {
		Object old = this.marginLeft;
		this.marginLeft = marginLeft;
		firePropertyChange("marginLeft", old, this.marginLeft);
	}

	public void setMarginRight(int marginRight) {
		Object old = this.marginRight;
		this.marginRight = marginRight;
		firePropertyChange("marginRight", old, this.marginRight);
	}

	public void setMarginTop(int marginTop) {
		Object old = this.marginTop;
		this.marginTop = marginTop;
		firePropertyChange("marginTop", old, this.marginTop);
	}

	public void setMarginWidth(int marginWidth) {
		Object old = this.marginWidth;
		this.marginWidth = marginWidth;
		firePropertyChange("marginWidth", old, this.marginWidth);
	}

	public void setVerticalSpacing(int verticalSpacing) {
		Object old = this.verticalSpacing;
		this.verticalSpacing = verticalSpacing;
		firePropertyChange("verticalSpacing", old, this.verticalSpacing);
	}
	
	public String getCss() {
		return css;
	}
	
	public void setCss(String css) {
		Object old = this.css;
		this.css = css;
		firePropertyChange("css", old, this.css);
	}
	
	public String getRwtCss() {
		return rwtCss;
	}
	
	public void setRwtCss(String rwtCss) {
		Object old = this.rwtCss;
		this.rwtCss = rwtCss;
		firePropertyChange("rwtCss", old, this.rwtCss);
	}
}
