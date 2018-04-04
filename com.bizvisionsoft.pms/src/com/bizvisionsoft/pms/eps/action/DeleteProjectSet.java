package com.bizvisionsoft.pms.eps.action;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.model.ProjectSet;
import com.bizvisionsoft.serviceconsumer.Services;

public class DeleteProjectSet {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			if (em instanceof ProjectSet) {
				if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "删除", "您确定要删除选中的项目集吗？"))
					try {
						if (Services.get(ProjectSetService.class).delete(((ProjectSet) em).get_id()) == 1)
							((GridPart) context.getContent()).remove(em);
					} catch (Exception e) {
						MessageDialog.openError(bruiService.getCurrentShell(), "删除", e.getMessage());
					}
			}
		});
	}

}
