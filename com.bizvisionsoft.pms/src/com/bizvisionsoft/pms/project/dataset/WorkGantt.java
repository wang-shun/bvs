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
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class WorkGantt {

	@Inject
	private BruiAssemblyContext context;

	@Inject
	private IBruiService brui;

	private WorkService workService;

	private ObjectId parent_id;

	private ObjectId project_id;

	@Init
	private void init() {
		parent_id = ((WorkInfo) context.getRootInput()).get_id();
		project_id = ((WorkInfo) context.getRootInput()).getParent_id();
		workService = Services.get(WorkService.class);
	}

	@DataSet({ "��������ͼ/data", "��������ͼ���ޱ��鿴��/data" })
	public List<WorkInfo> data() {
		return workService.createTaskDataSet(new BasicDBObject("parent_id", parent_id));
	}

	@DataSet({ "��������ͼ/links", "��������ͼ���ޱ��鿴��/links" })
	public List<WorkLinkInfo> links() {
		return workService.createLinkDataSet(new BasicDBObject("parent_id", parent_id));
	}

	@DataSet({ "��������ͼ/initDateRange", "��������ͼ���ޱ��鿴��/initDateRange" })
	public Date[] initDateRange() {
		// TODO �������޵Ŀ���
		return Services.get(ProjectService.class).getPlanDateRange(project_id).toArray(new Date[0]);
	}

	@Listener("��������ͼ/onAfterTaskAdd")
	public void onAfterTaskAdd(GanttEvent e) {
		workService.insertWork((WorkInfo) e.task);
		System.out.println(e.text);
	}

	@Listener("��������ͼ/onAfterTaskUpdate")
	public void onAfterTaskUpdate(GanttEvent e) {
		workService.updateWork(new FilterAndUpdate().filter(new BasicDBObject("_id", new ObjectId(e.id)))
				.set(Util.getBson(e.task, "_id")).bson());
		System.out.println(e.text);
	}

	@Listener("��������ͼ/onAfterTaskDelete")
	public void onAfterTaskDelete(GanttEvent e) {
		workService.deleteWork(new ObjectId(e.id));
		System.out.println(e.text);
	}

	@Listener("��������ͼ/onAfterLinkAdd")
	public void onAfterLinkAdd(GanttEvent e) {
		workService.insertLink((WorkLinkInfo) e.link);
		System.out.println(e.text);
	}

	@Listener("��������ͼ/onAfterLinkUpdate")
	public void onAfterLinkUpdate(GanttEvent e) {
		workService.updateLink(new FilterAndUpdate().filter(new BasicDBObject("_id", new ObjectId(e.id)))
				.set(Util.getBson(e.link, "_id")).bson());
		System.out.println(e.text);
	}

	@Listener("��������ͼ/onAfterLinkDelete")
	public void onAfterLinkDelete(GanttEvent e) {
		workService.deleteLink(new ObjectId(e.id));
		System.out.println(e.text);
	}

}
