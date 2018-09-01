package com.bizvisionsoft.onlinedesigner.actions;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.serviceconsumer.Services;

public class SyncOrgFullName {
	
	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		try {
			Services.get(CommonService.class).syncOrgFullName();
		} catch (Exception e) {
			MessageDialog.openError(brui.getCurrentShell(), "同步组织全名错误", e.getMessage());
		}
	}

}
