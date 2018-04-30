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

	@Listener("��Ŀ����ͼ/onTaskLinkBefore")
	public void onTaskLinkBefore(GanttEvent event) {
		WorkLinkInfo input = WorkLinkInfo.newInstance(project.get_id()).setSource((WorkInfo) event.linkSource)
				.setTarget((WorkInfo) event.linkTarget).setType(event.linkType);

		Editor.open("������ӹ�ϵ�༭����1��1��", context, input, (r, wi) -> {
			GanttPart content = (GanttPart) context.getContent();
			content.addLink(wi);
		});
	}

	@Listener("��Ŀ����ͼ/onLinkDblClick")
	public void onLinkDblClick(GanttEvent event) {
		Editor.open("������ӹ�ϵ�༭����1��1��", context, event.link, (r, wi) -> {
			GanttPart content = (GanttPart) context.getContent();
			content.updateLink(wi);
		});
	}

	@Listener("��Ŀ����ͼ/onTaskDblClick")
	public void onTaskDblClick(GanttEvent event) {
		String editor ;
		if(((WorkInfo)event.task).isStage()) {
			editor = "����ͼ�׶ι����༭��";
		}else if(((WorkInfo)event.task).isSummary()) {
			editor = "����ͼ�ܳɹ����༭��";
		}else {
			editor = "����ͼ�����༭��";
		}
		Editor.create(editor, context, event.task,false).setTitle(((WorkInfo)event.task).toString()).ok((r, wi) -> {
			GanttPart content = (GanttPart) context.getContent();
			content.updateTask(wi);
		});
	}

}
