package com.bizvisionsoft.onlinedesigner.actions;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateIndexACT {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		try {
			Services.get(SystemService.class).createIndex();
			Layer.message("索引创建完成");
		} catch (Exception e) {
			MessageDialog.openError(brui.getCurrentShell(), "创建索引错误", e.getMessage());
		}
	}
}
