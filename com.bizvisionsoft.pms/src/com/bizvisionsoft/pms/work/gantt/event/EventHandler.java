package com.bizvisionsoft.pms.work.gantt.event;

import com.bizivisionsoft.widgets.gantt.GanttEvent;
import com.bizvisionsoft.annotations.md.service.Listener;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.assembly.GanttPart;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.service.model.WorkLinkInfo;

public class EventHandler {

	@Inject
	private BruiAssemblyContext context;

	@Inject
	private IBruiService bruiService;

	private Project project;

	@Init
	private void init() {
		project = (Project) context.getRootInput();
	}

	@Listener("项目甘特图#onTaskLinkBefore")
	public void onTaskLinkBefore(GanttEvent event) {
		WorkLinkInfo input = WorkLinkInfo.newInstance(project.get_id()).setSource((WorkInfo) event.linkSource)
				.setTarget((WorkInfo) event.linkTarget).setType(event.linkType);

		Editor.open("工作搭接关系编辑器（1对1）", context, input, (r, wi) -> {
			GanttPart content = (GanttPart) context.getContent();
			content.addLink(wi);
		});
	}

	@Listener("项目甘特图#onLinkDblClick")
	public void onLinkDblClick(GanttEvent event) {
		Editor.open("工作搭接关系编辑器（1对1）", context, event.link, (r, wi) -> {
			GanttPart content = (GanttPart) context.getContent();
			content.updateLink(wi);
		});
	}

	@Listener("项目甘特图#onTaskDblClick")
	public void onTaskDblClick(GanttEvent event) {
		Editor.create("创建甘特图工作编辑器", context, event.task).setTitle("编辑工作").open((r, wi) -> {
			GanttPart content = (GanttPart) context.getContent();
			content.updateTask(wi);
		});
	}

}
