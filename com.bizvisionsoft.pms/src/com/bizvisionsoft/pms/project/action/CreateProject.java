package com.bizvisionsoft.pms.project.action;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateProject {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		new Editor<Project>(bruiService.getEditor("创建项目编辑器"), context)

				.setInput(new Project())

				.open((r, proj) -> {
					Project pj = Services.get(ProjectService.class).insert(proj);
					if (pj != null) {
						if (MessageDialog.openQuestion(bruiService.getCurrentShell(), "项目创建成功", "您是否打开项目主页？")) {
							// TODO 跳转到项目主页
						}

					}
				});

	}

}
