package com.bizivisionsoft.widgets.gantt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;

public class GanttGroup {

	GanttGroup(JsonObject jo) {
		tasks = new ArrayList<String>();
		links = new ArrayList<String>();
		jo.get("tasks").asArray().forEach(i -> tasks.add(i.asString()));
		jo.get("links").asArray().forEach(i -> links.add(i.asString()));
	}

	public List<String> tasks;

	public List<String> links;

}
