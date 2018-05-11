package com.bizivisionsoft.widgets.gantt;

public enum GanttEventCode {

	/**
	 * fires when autoscheduling is done autoscheduling完成后触发
	 **/
	onAfterAutoSchedule,

	/**
	 * fires after the batchUpdate method was called 批量更新多条记录后触发
	 **/
	onAfterBatchUpdate,

	/**
	 * fires after a new link is added to the Gantt chart 添加新链接后的触发将添加到甘特图中。
	 **/
	onAfterLinkAdd,

	/**
	 * fires after the user deletes a link 在用户删除链接后触发
	 **/
	onAfterLinkDelete,

	/**
	 * fires after the user updates a link 在用户更新链接后触发
	 **/
	onAfterLinkUpdate,

	/**
	 * fires after the user adds a task to the Gantt chart 用户将任务添加到甘特图后触发。
	 **/
	onAfterTaskAdd,

	/**
	 * fires for each task which has been autoscheduled 每个任务已autoscheduled触发
	 **/
	onAfterTaskAutoSchedule,

	/**
	 * fires after the user deletes a task 在用户删除任务后触发
	 **/
	onAfterTaskDelete,

	/**
	 * fires after the user updates a task 在用户更新任务后触发
	 **/
	onAfterTaskUpdate,

	/**
	 * fires if some dependency loops were found during auto scheduling
	 * 如果在自动调度过程中发现了一些依赖循环，就会触发
	 * 
	 **/
	onAutoScheduleCircularLink,

	/**
	 * fires when the circular reference has been detected and auto scheduling is
	 * not possible 当循环引用被检测到并且无法自动调度时触发
	 * 
	 **/
	onCircularLinkError,

	/**
	 * fires after all tasks were removed from the Gantt chart 从甘特图中删除所有任务后
	 **/
	onClear,

	/**
	 * fires after data has been rendered on the page 在页面上呈现数据后触发。
	 **/
	onDataRender,

	/**
	 * called after gantt has been cleared by the destructor method
	 * 甘特图中的甘特图被析构函数清除后调用。
	 * 
	 **/
	onDestroy,

	/**
	 * fires when the user clicks on an empty space in the Gantt chart (not on
	 * tasks) 当用户单击甘特图中的空空间（不是任务）时触发。
	 * 
	 **/
	onEmptyClick,

	/**
	 * fires when assert receives 'false' value, i.e. when assertion fails
	 * 当断言接收到“false”值时，即断言失败时触发。
	 * 
	 **/
	onError,

	/**
	 * fires when the user clicks on a link 当用户单击链接时触发
	 **/
	onLinkClick,

	/**
	 * fires when the user double clicks on a link 当用户双击链接时触发。
	 **/
	onLinkDblClick,

	/**
	 * fires when the user adds a new link and dhtmlxGantt checks whether the link
	 * is valid 当用户添加一个新的链接和dhtmlxgantt检查是否链接有效
	 * 
	 **/
	onLinkValidation,

	/**
	 * fires after loading data from the data source has been complete
	 * 从数据源加载数据后的触发已经完成。
	 * 
	 **/
	onLoadEnd,

	/**
	 * fires immediately before loading data from the data source has been started
	 * 在加载来自数据源的数据之前立即启动
	 * 
	 **/
	onLoadStart,

	/**				
	**/
	onMultiSelect,

	/**
	 * fires after a collection of options has been loaded from the server, but
	 * isn't parsed yet 从服务器加载了一系列选项之后的触发，但尚未解析。
	 * 
	 **/
	onOptionsLoad,

	/**
	 * fires after data was parsed (became available for API) but before it was
	 * rendered in the Gantt chart 数据解析后的触发（在API中可用），但在甘特图中呈现之前
	 * 
	 **/
	onParse,

	/**
	 * fires when the scale is re-rendered in order to display all tasks completely
	 * 当重新缩放时，为了完全显示所有任务而触发。
	 * 
	 **/
	onScaleAdjusted,

	/**
	 * fires when the user clicks on the cell in the time scale 当用户按时间刻度单击单元格时引发
	 * 
	 **/
	onScaleClick,

	/**
	 * fires when the user clicks on a task row in the grid area (including the
	 * 'expand/collapse' and 'add task' buttons) or on a task bar in the timeline
	 * area 当用户单击网格区域中的任务行（包括“扩展/崩溃”和“添加任务”按钮）或在时间轴区域的任务栏上时触发。
	 * 
	 **/
	onTaskClick,

	/**
	 * fires when the user double clicks on a task 当用户双击某个任务时触发
	 **/
	onTaskDblClick,

	/**				
	**/
	onTaskMultiSelect,

	/**
	 * fires when the user clicks on a row in the table 当用户单击表中的一行时引发
	 **/
	onTaskRowClick,

	/**
	 * fires when the user selects a task 当用户选择任务时触发
	 **/
	onTaskSelected,

	/**
	 * fires when the user unselects a task by selecting some other task
	 * 当用户取消选择一个任务选择一些其他任务
	 * 
	 **/
	onTaskUnselected,

	/**
	 **/
	onGridHeaderMenuClick,

	/**
	 * fires when the dhtmlxGantt templates are initialized 当dhtmlxgantt模板初始化
	 **/
	onGridRowMenuClick, 
	
	
	onTaskLinkBefore, 
	
	onAfterTaskProgress,
	
	

}