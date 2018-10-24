package com.bizvisionsoft.onlinedesigner.systemupdate;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.serviceconsumer.Services;

public class SystemUpdateV0502_accountitem {
	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("本次更新内容：<br/>");
			sb.append("1. 重新创建费用类科目关系。<br/>");
			sb.append("请确认进行本次更新。");
			if (brui.confirm("更新费用类科目关系", sb.toString())) {
				Services.get(SystemService.class).updateSystem("5.1M2", "accountitem");
				Layer.message("更新完成");
			}
		} catch (Exception e) {
			MessageDialog.openError(brui.getCurrentShell(), "更新完成错误", e.getMessage());
		}
	}
}
