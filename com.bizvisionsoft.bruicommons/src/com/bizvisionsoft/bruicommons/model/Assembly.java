package com.bizvisionsoft.bruicommons.model;

import java.util.List;

public class Assembly extends ModelObject {

	public static final String TYPE_STICKER = "sticker";

	public static final String TYPE_GRID = "grid";

	public static final String TYPE_EDITOR = "editor";

	private String id;

	private String name;

	private String title;

	private String description;

	private String bundleId;

	private String className;

	private List<Layout> layout;

	private String type;

	private String stickerTitle;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		Object old = this.title;
		this.title = title;
		firePropertyChange("title", old, this.title);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		Object old = this.description;
		this.description = description;
		firePropertyChange("description", old, this.description);
	}

	public String getBundleId() {
		return bundleId;
	}

	public String getClassName() {
		return className;
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

	public List<Layout> getLayout() {
		return layout;
	}

	public void setLayout(List<Layout> layout) {
		this.layout = layout;
	}

	public boolean isCustomized() {
		return type != null || (bundleId != null && !bundleId.isEmpty() && className != null && !className.isEmpty());
	}

	public boolean isEmptyContainer() {
		return !isCustomized();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		Object old = this.type;
		this.type = type;
		firePropertyChange("type", old, this.type);
	}

	public String getStickerTitle() {
		return stickerTitle;
	}

	public void setStickerTitle(String stickerTitle) {
		Object old = this.stickerTitle;
		this.stickerTitle = stickerTitle;
		firePropertyChange("stickerTitle", old, this.stickerTitle);
	}

	// *******************************************************************************************
	// 表格

	private int gridAutoExpandLevel;

	private int gridCustomItemHeight;

	private boolean gridMarkupEnabled;

	private boolean gridLineVisiable;

	private boolean gridMultiSelection;

	private boolean gridHeaderVisiable;

	private boolean gridFooterVisiable;

	private boolean gridHasBorder;

	private boolean gridHasHScroll;

	private boolean gridHasVScroll;

	private boolean gridHideIndentionImage;

	private boolean gridAutoHeight;

	private int gridFix;

	private boolean gridPageControl;

	private String gridRenderBundleId;

	private String gridRenderClassName;

	private String gridDataSetService;

	private String gridDataSetBundleId;

	private String gridDataSetClassName;

	private List<Column> columns;

	// 编辑器
	private List<FormField> fields;

	private boolean gridAutoColumnWidth;

	public String getGridRenderBundleId() {
		return gridRenderBundleId;
	}

	public String getGridRenderClassName() {
		return gridRenderClassName;
	}

	public void setGridRenderBundleId(String gridRenderBundleId) {
		Object old = this.gridRenderBundleId;
		this.gridRenderBundleId = gridRenderBundleId;
		firePropertyChange("gridRenderBundleId", old, this.gridRenderBundleId);
	}

	public void setGridRenderClassName(String gridRenderClassName) {
		Object old = this.gridRenderClassName;
		this.gridRenderClassName = gridRenderClassName;
		firePropertyChange("gridRenderClassName", old, this.gridRenderClassName);
	}

	public int getGridAutoExpandLevel() {
		return gridAutoExpandLevel;
	}

	public int getGridCustomItemHeight() {
		return gridCustomItemHeight;
	}

	public int getGridFix() {
		return gridFix;
	}
	// public int getGridPreload() {
	// return gridPreload;
	// }

	public boolean isGridHasBorder() {
		return gridHasBorder;
	}

	public boolean isGridHeaderVisiable() {
		return gridHeaderVisiable;
	}

	public boolean isGridFooterVisiable() {
		return gridFooterVisiable;
	}

	public boolean isGridLineVisiable() {
		return gridLineVisiable;
	}

	public boolean isGridMarkupEnabled() {
		return gridMarkupEnabled;
	}

	public boolean isGridMultiSelection() {
		return gridMultiSelection;
	}

	// public boolean isGridUseHashlookup() {
	// return gridUseHashlookup;
	// }
	//
	// public boolean isGridVirtual() {
	// return gridVirtual;
	// }

	public void setGridAutoExpandLevel(int gridAutoExpandLevel) {
		Object old = this.gridAutoExpandLevel;
		this.gridAutoExpandLevel = gridAutoExpandLevel;
		firePropertyChange("gridAutoExpandLevel", old, this.gridAutoExpandLevel);
	}

	public void setGridCustomItemHeight(int gridCustomItemHeight) {
		Object old = this.gridCustomItemHeight;
		this.gridCustomItemHeight = gridCustomItemHeight;
		firePropertyChange("gridCustomItemHeight", old, this.gridCustomItemHeight);
	}

	public void setGridMarkupEnabled(boolean gridMarkupEnabled) {
		Object old = this.gridMarkupEnabled;
		this.gridMarkupEnabled = gridMarkupEnabled;
		firePropertyChange("gridMarkupEnabled", old, this.gridMarkupEnabled);
	}

	// public void setGridUseHashlookup(boolean gridUseHashlookup) {
	// Object old = this.gridUseHashlookup;
	// this.gridUseHashlookup = gridUseHashlookup;
	// firePropertyChange("gridUseHashlookup", old, this.gridUseHashlookup);
	// }

	public void setGridLineVisiable(boolean gridLineVisiable) {
		Object old = this.gridLineVisiable;
		this.gridLineVisiable = gridLineVisiable;
		firePropertyChange("gridLineVisiable", old, this.gridLineVisiable);
	}

	public void setGridMultiSelection(boolean gridMultiSelection) {
		Object old = this.gridMultiSelection;
		this.gridMultiSelection = gridMultiSelection;
		firePropertyChange("gridMultiSelection", old, this.gridMultiSelection);
	}

	public void setGridHeaderVisiable(boolean gridHeaderVisiable) {
		Object old = this.gridHeaderVisiable;
		this.gridHeaderVisiable = gridHeaderVisiable;
		firePropertyChange("gridHeaderVisiable", old, this.gridHeaderVisiable);
	}

	public void setGridFooterVisiable(boolean gridFooterVisiable) {
		Object old = this.gridFooterVisiable;
		this.gridFooterVisiable = gridFooterVisiable;
		firePropertyChange("gridFooterVisiable", old, this.gridFooterVisiable);
	}

	public void setGridHasBorder(boolean gridHasBorder) {
		Object old = this.gridHasBorder;
		this.gridHasBorder = gridHasBorder;
		firePropertyChange("gridHasBorder", old, this.gridHasBorder);
	}

	// public void setGridVirtual(boolean gridVirtual) {
	// Object old = this.gridVirtual;
	// this.gridVirtual = gridVirtual;
	// firePropertyChange("gridVirtual", old, this.gridVirtual);
	// }

	public void setGridFix(int gridFix) {
		Object old = this.gridFix;
		this.gridFix = gridFix;
		firePropertyChange("gridFix", old, this.gridFix);
	}

	// public void setGridPreload(int gridPreload) {
	// Object old = this.gridPreload;
	// this.gridPreload = gridPreload;
	// firePropertyChange("gridPreload", old, this.gridPreload);
	// }

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public boolean isGridPageControl() {
		return gridPageControl;
	}

	public void setGridPageControl(boolean gridPageControl) {
		Object old = this.gridPageControl;
		this.gridPageControl = gridPageControl;
		firePropertyChange("gridPageControl", old, this.gridPageControl);
	}

	public boolean isGridHasHScroll() {
		return gridHasHScroll;
	}

	public boolean isGridHasVScroll() {
		return gridHasVScroll;
	}

	public void setGridHasHScroll(boolean gridHasHScroll) {
		Object old = this.gridHasHScroll;
		this.gridHasHScroll = gridHasHScroll;
		firePropertyChange("gridHasHScroll", old, this.gridHasHScroll);
	}

	public void setGridHasVScroll(boolean gridHasVScroll) {
		Object old = this.gridHasVScroll;
		this.gridHasVScroll = gridHasVScroll;
		firePropertyChange("gridHasVScroll", old, this.gridHasVScroll);
	}

	public boolean isGridHideIndentionImage() {
		return gridHideIndentionImage;
	}

	public void setGridHideIndentionImage(boolean gridHideIndentionImage) {
		Object old = this.gridHideIndentionImage;
		this.gridHideIndentionImage = gridHideIndentionImage;
		firePropertyChange("gridHideIndentionImage", old, this.gridHideIndentionImage);
	}

	public boolean isGridAutoHeight() {
		return gridAutoHeight;
	}

	public void setGridAutoHeight(boolean gridAutoHeight) {
		Object old = this.gridAutoHeight;
		this.gridAutoHeight = gridAutoHeight;
		firePropertyChange("gridAutoHeight", old, this.gridAutoHeight);
	}

	public String getGridDataSetService() {
		return gridDataSetService;
	}

	public void setGridDataSetService(String gridDataSetService) {
		Object old = this.gridDataSetService;
		this.gridDataSetService = gridDataSetService;
		firePropertyChange("gridDataSetService", old, this.gridDataSetService);
	}

	public String getGridDataSetBundleId() {
		return gridDataSetBundleId;
	}

	public String getGridDataSetClassName() {
		return gridDataSetClassName;
	}

	public void setGridDataSetBundleId(String gridDataSetBundleId) {
		Object old = this.gridDataSetBundleId;
		this.gridDataSetBundleId = gridDataSetBundleId;
		firePropertyChange("gridDataSetBundleId", old, this.gridDataSetBundleId);
	}

	public void setGridDataSetClassName(String gridDataSetClassName) {
		Object old = this.gridDataSetClassName;
		this.gridDataSetClassName = gridDataSetClassName;
		firePropertyChange("gridDataSetClassName", old, this.gridDataSetClassName);
	}

	public boolean isGridAutoColumnWidth() {
		return gridAutoColumnWidth;
	}

	public void setGridAutoColumnWidth(boolean gridAutoColumnWidth) {
		this.gridAutoColumnWidth = gridAutoColumnWidth;
	}

	// *******************************************************************************************
	// action
	private List<Action> actions;

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	// *******************************************************************************************
	// form field

	public List<FormField> getFields() {
		return fields;
	}

	public void setFields(List<FormField> fields) {
		this.fields = fields;
	}

	// *********************************************************************************************
	// 查询定义
	private String queryBuilderBundle;

	private String queryBuilderClass;

	public String getQueryBuilderBundle() {
		return queryBuilderBundle;
	}

	public String getQueryBuilderClass() {
		return queryBuilderClass;
	}

	public void setQueryBuilderBundle(String queryBuilderBundle) {
		this.queryBuilderBundle = queryBuilderBundle;
	}

	public void setQueryBuilderClass(String queryBuilderClass) {
		this.queryBuilderClass = queryBuilderClass;
	}

	private boolean borderTop;
	private boolean borderRight;
	private boolean borderBottom;
	private boolean borderLeft;

	public boolean isBorderBottom() {
		return borderBottom;
	}

	public boolean isBorderLeft() {
		return borderLeft;
	}

	public boolean isBorderRight() {
		return borderRight;
	}

	public boolean isBorderTop() {
		return borderTop;
	}

	public void setBorderBottom(boolean borderBottom) {
		this.borderBottom = borderBottom;
	}

	public void setBorderLeft(boolean borderLeft) {
		this.borderLeft = borderLeft;
	}

	public void setBorderRight(boolean borderRight) {
		this.borderRight = borderRight;
	}

	public void setBorderTop(boolean borderTop) {
		this.borderTop = borderTop;
	}

}
