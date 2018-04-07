package com.bizvisionsoft.pms.project.dataset;

import java.util.Date;
import java.util.List;

import com.bizivisionsoft.widgets.gantt.GanttEvent;
import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.Listener;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class ProjectGanttDataSet {

	@Inject
	private BruiAssemblyContext context;

	@Inject
	private IBruiService bruiService;

	private Project project;

	@Init
	private void init() {
		project = (Project) context.getRootInput();
	}

	@DataSet("项目甘特图#data")
	public List<WorkInfo> createGanttDataSet() {
		return Services.get(WorkService.class).createGanttDataSet(new BasicDBObject("project_id", project.get_id()));
	}

	@DataSet("项目甘特图#links")
	public List<WorkLinkInfo> createGanttLinkSet() {
		return Services.get(WorkService.class).createGanttLinkSet(new BasicDBObject("project_id", project.get_id()));
	}

	@DataSet("项目甘特图#initDateRange")
	public Date[] getDateRange() {
		List<Date> result = Services.get(ProjectService.class).getPlanDateRange(project.get_id());
		return result.toArray(new Date[0]);
	}

	@Listener("项目甘特图#onAfterTaskAdd")
	public void onAfterTaskAddEvent(GanttEvent event) {
		System.out.println(event.text+event.data);
	}

	@Listener("项目甘特图#onAfterTaskUpdate")
	public void onAfterTaskUpdateEvent(GanttEvent event) {
		System.out.println(event.text+event.data);
	}

}
