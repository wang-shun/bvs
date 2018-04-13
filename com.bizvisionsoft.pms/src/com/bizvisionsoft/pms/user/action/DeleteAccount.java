package com.bizvisionsoft.pms.user.action;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.UserInfo;
import com.bizvisionsoft.serviceconsumer.Services;

public class DeleteAccount {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(elem -> {
			if (elem instanceof UserInfo) {
				if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "删除", "请确认将要删除选择的账户。")) {
					try {
						Services.get(UserService.class).delete(((UserInfo) elem).get_id());
						GridPart grid = (GridPart) context.getContent();
						grid.remove(elem);
					} catch (Exception e) {
						MessageDialog.openError(bruiService.getCurrentShell(), "删除", e.getMessage());
					}
				}
			}
		});
	}

}
