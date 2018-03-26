package com.bizivisionsoft.widgets.gantt;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Event;

public class GanttEvent extends Event {

	public String taskId;
	
	public ArrayList<String> updatedTasks;
	
	public String id;
	
	public Object link;
	
	public Object task;
	
	public String errorMessage;
	
	public String clientType;
	
	public boolean state;
	
	public String startDate;
	
	public String predecessor;
	
	public ArrayList<GanttGroup> groups;
	
	public GanttGroup group;

	public String date;

}
