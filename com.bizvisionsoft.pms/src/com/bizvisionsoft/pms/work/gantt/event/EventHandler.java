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

	@Listener("��Ŀ����ͼ#onTaskLinkBefore")
	public void onTaskLinkBefore(GanttEvent event) {

		WorkLinkInfo input = WorkLinkInfo.newInstance(project.get_id()).setSource((WorkInfo) event.linkSource)
				.setTarget((WorkInfo) event.linkTarget).setType(event.linkType);

		// ��ʾ�༭��
		new Editor<WorkLinkInfo>(bruiService.getEditor("������ӹ�ϵ�༭����1��1��"), context).setInput(input).open((r, wi) -> {
			GanttPart content = (GanttPart) context.getContent();
			System.out.println(content);
			// content.addTask(wi, wi.index());
		});

	}

}
