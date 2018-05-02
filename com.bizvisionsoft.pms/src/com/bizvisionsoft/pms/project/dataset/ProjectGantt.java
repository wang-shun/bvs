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

public class ProjectGantt {

	@Inject
	private BruiAssemblyContext context;

	@Inject
	private IBruiService brui;


	private WorkService workService;

	private ObjectId project_id;

	@Init
	private void init() {
		project_id = ((Project) context.getRootInput()).get_id();
		workService = Services.get(WorkService.class);
	}
	

	@DataSet({"��Ŀ����ͼ/data","��Ŀ����ͼ���ޱ��鿴��/data"})
	public List<WorkInfo> data() {
		return workService.createTaskDataSet(new BasicDBObject("project_id", project_id));
	}
	

	@DataSet({"��Ŀ����ͼ/links","��Ŀ����ͼ���ޱ��鿴��/links"})
	public List<WorkLinkInfo> links() {
		return workService.createLinkDataSet(new BasicDBObject("project_id", project_id));
	}

	@DataSet({"��Ŀ����ͼ/initDateRange","��Ŀ����ͼ���ޱ��鿴��/initDateRange"})
	public Date[] initDateRange() {
		return Services.get(ProjectService.class).getPlanDateRange(project_id).toArray(new Date[0]);
	}

	@Listener("��Ŀ����ͼ/onAfterTaskAdd")
	public void onAfterTaskAdd(GanttEvent e) {
		workService.insertWork((WorkInfo) e.task);
		System.out.println(e.text);
	}

	@Listener("��Ŀ����ͼ/onAfterTaskUpdate")
	public void onAfterTaskUpdate(GanttEvent e) {
		workService.updateWork(new FilterAndUpdate().filter(new BasicDBObject("_id", new ObjectId(e.id)))
				.set(Util.getBson(e.task,  "_id")).bson());
		System.out.println(e.text);
	}

	@Listener("��Ŀ����ͼ/onAfterTaskDelete")
	public void onAfterTaskDelete(GanttEvent e) {
		workService.deleteWork(new ObjectId(e.id));
		System.out.println(e.text);
	}

	@Listener("��Ŀ����ͼ/onAfterLinkAdd")
	public void onAfterLinkAdd(GanttEvent e) {
		workService.insertLink((WorkLinkInfo) e.link);
		System.out.println(e.text);
	}

	@Listener("��Ŀ����ͼ/onAfterLinkUpdate")
	public void onAfterLinkUpdate(GanttEvent e) {
		workService.updateLink(new FilterAndUpdate().filter(new BasicDBObject("_id", new ObjectId(e.id)))
				.set(Util.getBson(e.link,  "_id")).bson());
		System.out.println(e.text);
	}

	@Listener("��Ŀ����ͼ/onAfterLinkDelete")
	public void onAfterLinkDelete(GanttEvent e) {
		workService.deleteLink(new ObjectId(e.id));
		System.out.println(e.text);
	}
	

}
