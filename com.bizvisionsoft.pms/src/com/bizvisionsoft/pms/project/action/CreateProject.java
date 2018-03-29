package com.bizvisionsoft.pms.project.action;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.model.Project;

public class CreateProject {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		Project project = new Project();
		Editor editor = bruiService.createEditorByName("创建项目编辑器", project, true,false, context);
		if (Window.OK == editor.open()) {
			Object result = editor.getResult();
			System.out.println(result);
//			FilterAndUpdate filterAndUpdate = new FilterAndUpdate()
//					.filter(new BasicDBObject("userId", user.getUserId())).set(editor.getResult());
//			long cnt = service.update(filterAndUpdate.bson());
//			if (cnt == 1) {
//				UserInfo info = service.info(user.getUserId());
//				GridPart grid = (GridPart) context.getContent();
//				grid.replaceItem(elem, info);
//			}
		}
	}

}
