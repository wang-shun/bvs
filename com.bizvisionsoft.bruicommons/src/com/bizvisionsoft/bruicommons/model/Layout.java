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
	
	private String layoutType;
	
//	private String rwtCss;

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
		this.description = description;
	}

	public int getMinimalDeviceWidth() {
		return minimalDeviceWidth;
	}

	public void setMinimalDeviceWidth(int minimalDeviceWidth) {
		this.minimalDeviceWidth = minimalDeviceWidth;
	}

	public int getMaximalDeviceWidth() {
		return maximalDeviceWidth;
	}

	public void setMaximalDeviceWidth(int maximalDeviceWidth) {
		this.maximalDeviceWidth = maximalDeviceWidth;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
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
		this.extendHorizontalSpace = extendHorizontalSpace;
	}
	
	public void setExtendVerticalSpace(boolean extendVerticalSpace) {
		this.extendVerticalSpace = extendVerticalSpace;
	}
	
	public void setMakeColumnsEqualWidth(boolean makeColumnsEqualWidth) {
		this.makeColumnsEqualWidth = makeColumnsEqualWidth;
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = horizontalSpacing;
	}

	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
	}

	public void setMarginHeight(int marginHeight) {
		this.marginHeight = marginHeight;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	public void setMarginWidth(int marginWidth) {
		this.marginWidth = marginWidth;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = verticalSpacing;
	}
	
	public String getCss() {
		return css;
	}
	
	public void setCss(String css) {
		this.css = css;
	}
	
//	public String getRwtCss() {
//		return rwtCss;
//	}
//	
//	public void setRwtCss(String rwtCss) {
//		this.rwtCss = rwtCss;
//	}
	
	public static final String TYPE_GRID = "grid";

	public static final String TYPE_LANE = "lane";
	
	public String getLayoutType() {
		return layoutType;
	}
	
	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	//Ó¾µÀ²¼¾Ö
	
	public static final String LANE_HORIZONTAL = "horizontal";

	public static final String LANE_VERTICAL = "vertical";
	
	private String laneDirection;
	
	public String getLaneDirection() {
		return laneDirection;
	}
	
	public void setLaneDirection(String laneDirection) {
		this.laneDirection = laneDirection;
	}
	
	private int laneWidth;
	
	public void setLaneWidth(int laneWidth) {
		this.laneWidth = laneWidth;
	}
	
	public int getLaneWidth() {
		return laneWidth;
	}
}
