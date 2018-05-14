package com.bizvisionsoft.bruicommons.model;

import java.util.List;

import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

public class Assembly extends ModelObject {


	@ReadValue(ReadValue.TYPE)
	private String getTypeName() {
		String text = "组件";
		if (TYPE_EDITOR.equals(type)) {
			text += " - 编辑器";
		} else if (TYPE_INFOPAD.equals(type)) {
			text += " - 信息面板";
		} else if (TYPE_SELECTOR.equals(type)) {
			text += " - 弹出式选择器";
		} else if (TYPE_STICKER.equals(type)) {
			text += " - 带有标题栏容器";
		} else if (TYPE_GRID.equals(type)) {
			text += " - 表格";
		} else if (TYPE_TREE.equals(type)) {
			text += " - 树";
		} else if (TYPE_GANTT.equals(type)) {
			text += " - 甘特图";
		} else if (TYPE_SCHEDULER.equals(type)) {
			text += " - 日程表";
		} else if (TYPE_ACTION_PANEL.equals(type)) {
			text += " - 操作面板";
		} else if (TYPE_MESSENGER.equals(type)) {
			text += " - 消息收件箱";
		}
		return text;
	}

	@Override
	@Label
	public String toString() {
		return name + " [" + id + "]";
	}

	public static final String TYPE_STICKER = "sticker";
	
	public static final String TYPE_INFOPAD = "infopad";

	public static final String TYPE_GRID = "grid";
	
	public static final String TYPE_TREE = "tree";

	public static final String TYPE_EDITOR = "editor";

	public static final String TYPE_GANTT = "gantt";
	
	public static final String TYPE_SCHEDULER = "scheduler";

	public static final String TYPE_SELECTOR = "selector";

	public static final String TYPE_ACTION_PANEL = "actionpanel";
	
	public static final String TYPE_MESSENGER = "messenger";


	private String id;

	@ReadValue
	@WriteValue
	private String name;

	private String title;

	private String description;

	private String bundleId;

	private String className;

	private List<Layout> layout;

	private String type;

	private String stickerTitle;

	private String folderId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
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
		this.description = description;
	}

	public String getBundleId() {
		return bundleId;
	}

	public String getClassName() {
		return className;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public void setClassName(String className) {
		this.className = className;

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
		this.type = type;
	}

	public String getStickerTitle() {
		return stickerTitle;
	}

	public void setStickerTitle(String stickerTitle) {
		this.stickerTitle = stickerTitle;
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
		this.gridRenderBundleId = gridRenderBundleId;
	}

	public void setGridRenderClassName(String gridRenderClassName) {
		this.gridRenderClassName = gridRenderClassName;
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
		this.gridAutoExpandLevel = gridAutoExpandLevel;
	}

	public void setGridCustomItemHeight(int gridCustomItemHeight) {
		this.gridCustomItemHeight = gridCustomItemHeight;
	}

	public void setGridMarkupEnabled(boolean gridMarkupEnabled) {
		this.gridMarkupEnabled = gridMarkupEnabled;
	}

	// public void setGridUseHashlookup(boolean gridUseHashlookup) {
	// Object old = this.gridUseHashlookup;
	// this.gridUseHashlookup = gridUseHashlookup;
	// firePropertyChange("gridUseHashlookup", old, this.gridUseHashlookup);
	// }

	public void setGridLineVisiable(boolean gridLineVisiable) {
		this.gridLineVisiable = gridLineVisiable;
	}

	public void setGridMultiSelection(boolean gridMultiSelection) {
		this.gridMultiSelection = gridMultiSelection;
	}

	public void setGridHeaderVisiable(boolean gridHeaderVisiable) {
		this.gridHeaderVisiable = gridHeaderVisiable;
	}

	public void setGridFooterVisiable(boolean gridFooterVisiable) {
		this.gridFooterVisiable = gridFooterVisiable;
	}

	public void setGridHasBorder(boolean gridHasBorder) {
		this.gridHasBorder = gridHasBorder;
	}

	// public void setGridVirtual(boolean gridVirtual) {
	// Object old = this.gridVirtual;
	// this.gridVirtual = gridVirtual;
	// firePropertyChange("gridVirtual", old, this.gridVirtual);
	// }

	public void setGridFix(int gridFix) {
		this.gridFix = gridFix;
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
		this.gridPageControl = gridPageControl;
	}

	public boolean isGridHasHScroll() {
		return gridHasHScroll;
	}

	public boolean isGridHasVScroll() {
		return gridHasVScroll;
	}

	public void setGridHasHScroll(boolean gridHasHScroll) {
		this.gridHasHScroll = gridHasHScroll;
	}

	public void setGridHasVScroll(boolean gridHasVScroll) {
		this.gridHasVScroll = gridHasVScroll;
	}

	public boolean isGridHideIndentionImage() {
		return gridHideIndentionImage;
	}

	public void setGridHideIndentionImage(boolean gridHideIndentionImage) {
		this.gridHideIndentionImage = gridHideIndentionImage;
	}

	public boolean isGridAutoHeight() {
		return gridAutoHeight;
	}

	public void setGridAutoHeight(boolean gridAutoHeight) {
		this.gridAutoHeight = gridAutoHeight;
	}

	public String getGridDataSetService() {
		return gridDataSetService;
	}

	public void setGridDataSetService(String gridDataSetService) {
		this.gridDataSetService = gridDataSetService;
	}

	public String getGridDataSetBundleId() {
		return gridDataSetBundleId;
	}

	public String getGridDataSetClassName() {
		return gridDataSetClassName;
	}

	public void setGridDataSetBundleId(String gridDataSetBundleId) {
		this.gridDataSetBundleId = gridDataSetBundleId;
	}

	public void setGridDataSetClassName(String gridDataSetClassName) {
		this.gridDataSetClassName = gridDataSetClassName;
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

	private List<Action> rowActions;

	public List<Action> getRowActions() {
		return rowActions;
	}

	public void setRowActions(List<Action> rowActions) {
		this.rowActions = rowActions;
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

	/**
	 * now, only gantt used！！！
	 */
	private boolean readonly;
	
	private boolean enableGanttCompare;

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	
	public boolean isEnableGanttCompare() {
		return enableGanttCompare;
	}
	
	public void setEnableGanttCompare(boolean enableGanttCompare) {
		this.enableGanttCompare = enableGanttCompare;
	}
	
	private String ganttTimeScaleType;
	
	public void setGanttTimeScaleType(String ganttTimeScaleType) {
		this.ganttTimeScaleType = ganttTimeScaleType;
	}
	
	public String getGanttTimeScaleType() {
		return ganttTimeScaleType;
	}

	private boolean ganttGridWidthCalculate;
	
	public boolean isGanttGridWidthCalculate() {
		return ganttGridWidthCalculate;
	}

	public void setGanttGridWidthCalculate(boolean ganttGridWidthCalculate) {
		this.ganttGridWidthCalculate = ganttGridWidthCalculate;
	}

	private int ganttGridWidth;

	public void setGanttGridWidth(int ganttGridWidth) {
		this.ganttGridWidth = ganttGridWidth;
	}

	public int getGanttGridWidth() {
		return ganttGridWidth;
	}

	private String eventHandlerBundleId;

	private String eventHandlerClassName;

	public String getEventHandlerBundleId() {
		return eventHandlerBundleId;
	}

	public String getEventHandlerClassName() {
		return eventHandlerClassName;
	}

	public void setEventHandlerBundleId(String eventHandlerBundleId) {
		this.eventHandlerBundleId = eventHandlerBundleId;
	}

	public void setEventHandlerClassName(String eventHandlerClassName) {
		this.eventHandlerClassName = eventHandlerClassName;
	}

	// *******************************************************************************************
	// action
	private List<Action> headActions;

	public List<Action> getHeadActions() {
		return headActions;
	}

	public void setHeadActions(List<Action> headActions) {
		this.headActions = headActions;
	}

	// 合并sticker
	private boolean hasTitlebar;

	public boolean isHasTitlebar() {
		return hasTitlebar;
	}

	public void setHasTitlebar(boolean hasTitlebar) {
		this.hasTitlebar = hasTitlebar;
	}

	// 是否在标题栏上显示input对象的标签
	private boolean displayInputLabelInTitlebar;

	public boolean isDisplayInputLabelInTitlebar() {
		return displayInputLabelInTitlebar;
	}

	public void setDisplayInputLabelInTitlebar(boolean displayInputLabelInTitlebar) {
		this.displayInputLabelInTitlebar = displayInputLabelInTitlebar;
	}

	private boolean displayRootInputLabelInTitlebar;

	public boolean isDisplayRootInputLabelInTitlebar() {
		return displayRootInputLabelInTitlebar;
	}

	public void setDisplayRootInputLabelInTitlebar(boolean displayRootInputLabelInTitlebar) {
		this.displayRootInputLabelInTitlebar = displayRootInputLabelInTitlebar;
	}

	// 选择器组件

	private String selectorGridAssemblyId;

	public String getSelectorGridAssemblyId() {
		return selectorGridAssemblyId;
	}

	public void setSelectorGridAssemblyId(String selectorGridAssemblyId) {
		this.selectorGridAssemblyId = selectorGridAssemblyId;
	}

	private boolean selectorMultiSelection;

	public boolean isSelectorMultiSelection() {
		return selectorMultiSelection;
	}

	public void setSelectorMultiSelection(boolean selectorMultiSelection) {
		this.selectorMultiSelection = selectorMultiSelection;
	}

	private String message;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int actionPanelColumnCount;

	private String nullValueAllowedFields;
	
	public void setActionPanelColumnCount(int actionPanelColumnCount) {
		this.actionPanelColumnCount = actionPanelColumnCount;
	}
	
	public int getActionPanelColumnCount() {
		return actionPanelColumnCount;
	}

	public String getNullValueAllowedFields() {
		return nullValueAllowedFields;
	}

	public void setNullValueAllowedFields(String nullValueAllowedFields) {
		this.nullValueAllowedFields = nullValueAllowedFields;
	}
	
	private boolean smallEditor;
	
	public boolean isSmallEditor() {
		return smallEditor;
	}
	
	public void setSmallEditor(boolean smallEditor) {
		this.smallEditor = smallEditor;
	}

	//scheduler的属性, schedule, timeline两种
	
	private String schedulerType;
	
	public void setSchedulerType(String schedulerType) {
		this.schedulerType = schedulerType;
	}
	
	public String getSchedulerType() {
		return schedulerType;
	}

}
