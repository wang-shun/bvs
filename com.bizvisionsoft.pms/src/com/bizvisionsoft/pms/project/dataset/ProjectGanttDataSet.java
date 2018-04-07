package com.bizvisionsoft.pms.project.dataset;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.bizivisionsoft.widgets.gantt.GanttEvent;
import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.md.service.Listener;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.WorkService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
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

	@DataSet("��Ŀ����ͼ#data")
	public List<WorkInfo> data() {
		return Services.get(WorkService.class).createGanttDataSet(new BasicDBObject("project_id", project.get_id()));
	}

	@DataSet("��Ŀ����ͼ#links")
	public List<WorkLinkInfo> links() {
		return Services.get(WorkService.class).createGanttLinkSet(new BasicDBObject("project_id", project.get_id()));
	}

	@DataSet("��Ŀ����ͼ#initDateRange")
	public Date[] initDateRange() {
		List<Date> result = Services.get(ProjectService.class).getPlanDateRange(project.get_id());
		return result.toArray(new Date[0]);
	}

	@Listener("��Ŀ����ͼ#onAfterTaskAdd")
	public void onAfterTaskAdd(GanttEvent event) {
		WorkInfo wi = (WorkInfo) event.task;
		Services.get(WorkService.class).insertWork(wi);
	}

	@Listener("��Ŀ����ͼ#onAfterTaskUpdate")
	public void onAfterTaskUpdate(GanttEvent event) {
		WorkInfo wi = (WorkInfo) event.task;
		BasicDBObject result = Util.getBson(wi, true);
		result.removeField("_id");
		Services.get(WorkService.class)
				.updateWork(new FilterAndUpdate().filter(new BasicDBObject("_id", wi.get_id())).set(result).bson());
	}

	@Listener("��Ŀ����ͼ#onAfterTaskDelete")
	public void onAfterTaskDelete(GanttEvent event) {
		Services.get(WorkService.class).deleteWork(new ObjectId(event.id));
	}

	@Listener("��Ŀ����ͼ#onAfterLinkAdd")
	public void onAfterLinkAdd(GanttEvent event) {
		Services.get(WorkService.class).insertLink((WorkLinkInfo) event.link);
	}

	@Listener("��Ŀ����ͼ#onAfterLinkUpdate")
	public void onAfterLinkUpdate(GanttEvent event) {
		WorkLinkInfo wi = (WorkLinkInfo) event.link;
		BasicDBObject result = Util.getBson(wi, true);
		result.removeField("_id");
		Services.get(WorkService.class)
				.updateLink(new FilterAndUpdate().filter(new BasicDBObject("_id", wi.get_id())).set(result).bson());
	}

	@Listener("��Ŀ����ͼ#onAfterLinkDelete")
	public void onAfterLinkDelete(GanttEvent event) {
		Services.get(WorkService.class).deleteLink(new ObjectId(event.id));
	}

}
