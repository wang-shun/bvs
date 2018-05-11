package com.bizivisionsoft.widgets.gantt;

public enum GanttEventCode {

	/**
	 * fires when autoscheduling is done autoscheduling��ɺ󴥷�
	 **/
	onAfterAutoSchedule,

	/**
	 * fires after the batchUpdate method was called �������¶�����¼�󴥷�
	 **/
	onAfterBatchUpdate,

	/**
	 * fires after a new link is added to the Gantt chart ��������Ӻ�Ĵ�������ӵ�����ͼ�С�
	 **/
	onAfterLinkAdd,

	/**
	 * fires after the user deletes a link ���û�ɾ�����Ӻ󴥷�
	 **/
	onAfterLinkDelete,

	/**
	 * fires after the user updates a link ���û��������Ӻ󴥷�
	 **/
	onAfterLinkUpdate,

	/**
	 * fires after the user adds a task to the Gantt chart �û���������ӵ�����ͼ�󴥷���
	 **/
	onAfterTaskAdd,

	/**
	 * fires for each task which has been autoscheduled ÿ��������autoscheduled����
	 **/
	onAfterTaskAutoSchedule,

	/**
	 * fires after the user deletes a task ���û�ɾ������󴥷�
	 **/
	onAfterTaskDelete,

	/**
	 * fires after the user updates a task ���û���������󴥷�
	 **/
	onAfterTaskUpdate,

	/**
	 * fires if some dependency loops were found during auto scheduling
	 * ������Զ����ȹ����з�����һЩ����ѭ�����ͻᴥ��
	 * 
	 **/
	onAutoScheduleCircularLink,

	/**
	 * fires when the circular reference has been detected and auto scheduling is
	 * not possible ��ѭ�����ñ���⵽�����޷��Զ�����ʱ����
	 * 
	 **/
	onCircularLinkError,

	/**
	 * fires after all tasks were removed from the Gantt chart �Ӹ���ͼ��ɾ�����������
	 **/
	onClear,

	/**
	 * fires after data has been rendered on the page ��ҳ���ϳ������ݺ󴥷���
	 **/
	onDataRender,

	/**
	 * called after gantt has been cleared by the destructor method
	 * ����ͼ�еĸ���ͼ�����������������á�
	 * 
	 **/
	onDestroy,

	/**
	 * fires when the user clicks on an empty space in the Gantt chart (not on
	 * tasks) ���û���������ͼ�еĿտռ䣨��������ʱ������
	 * 
	 **/
	onEmptyClick,

	/**
	 * fires when assert receives 'false' value, i.e. when assertion fails
	 * �����Խ��յ���false��ֵʱ��������ʧ��ʱ������
	 * 
	 **/
	onError,

	/**
	 * fires when the user clicks on a link ���û���������ʱ����
	 **/
	onLinkClick,

	/**
	 * fires when the user double clicks on a link ���û�˫������ʱ������
	 **/
	onLinkDblClick,

	/**
	 * fires when the user adds a new link and dhtmlxGantt checks whether the link
	 * is valid ���û����һ���µ����Ӻ�dhtmlxgantt����Ƿ�������Ч
	 * 
	 **/
	onLinkValidation,

	/**
	 * fires after loading data from the data source has been complete
	 * ������Դ�������ݺ�Ĵ����Ѿ���ɡ�
	 * 
	 **/
	onLoadEnd,

	/**
	 * fires immediately before loading data from the data source has been started
	 * �ڼ�����������Դ������֮ǰ��������
	 * 
	 **/
	onLoadStart,

	/**				
	**/
	onMultiSelect,

	/**
	 * fires after a collection of options has been loaded from the server, but
	 * isn't parsed yet �ӷ�����������һϵ��ѡ��֮��Ĵ���������δ������
	 * 
	 **/
	onOptionsLoad,

	/**
	 * fires after data was parsed (became available for API) but before it was
	 * rendered in the Gantt chart ���ݽ�����Ĵ�������API�п��ã������ڸ���ͼ�г���֮ǰ
	 * 
	 **/
	onParse,

	/**
	 * fires when the scale is re-rendered in order to display all tasks completely
	 * ����������ʱ��Ϊ����ȫ��ʾ���������������
	 * 
	 **/
	onScaleAdjusted,

	/**
	 * fires when the user clicks on the cell in the time scale ���û���ʱ��̶ȵ�����Ԫ��ʱ����
	 * 
	 **/
	onScaleClick,

	/**
	 * fires when the user clicks on a task row in the grid area (including the
	 * 'expand/collapse' and 'add task' buttons) or on a task bar in the timeline
	 * area ���û��������������е������У���������չ/�������͡�������񡱰�ť������ʱ�����������������ʱ������
	 * 
	 **/
	onTaskClick,

	/**
	 * fires when the user double clicks on a task ���û�˫��ĳ������ʱ����
	 **/
	onTaskDblClick,

	/**				
	**/
	onTaskMultiSelect,

	/**
	 * fires when the user clicks on a row in the table ���û��������е�һ��ʱ����
	 **/
	onTaskRowClick,

	/**
	 * fires when the user selects a task ���û�ѡ������ʱ����
	 **/
	onTaskSelected,

	/**
	 * fires when the user unselects a task by selecting some other task
	 * ���û�ȡ��ѡ��һ������ѡ��һЩ��������
	 * 
	 **/
	onTaskUnselected,

	/**
	 **/
	onGridHeaderMenuClick,

	/**
	 * fires when the dhtmlxGantt templates are initialized ��dhtmlxganttģ���ʼ��
	 **/
	onGridRowMenuClick, 
	
	
	onTaskLinkBefore, 
	
	onAfterTaskProgress,
	
	

}