package com.bizvisionsoft.pms.work.gantt.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.assembly.GanttPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.WorkInfo;

public class CreateRootTask {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		Project project = (Project) context.getRootInput();
		String title;
		Assembly editor;
		WorkInfo workInfo = WorkInfo.newInstance(project.get_id());
		if(project.isStageEnable()) {
			title = "�����׶�";
			editor = bruiService.getAssembly("����ͼ�׶ι����༭��");
			workInfo.setManageLevel("level1_task").setStage(true);
		}else {
			title = "��������";
			editor = bruiService.getAssembly("����ͼ�����༭��");
		}
		
		new Editor<WorkInfo>(editor, context).setTitle(title)
				.setInput(workInfo).ok((r, wi) -> {
					GanttPart content = (GanttPart) context.getContent();
					content.addTask(wi, wi.index());
				});
	}

}
