package com.bizvisionsoft.pms.action;

import org.eclipse.jface.dialogs.InputDialog;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class UserSetting {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute() {
		InputDialog id = new InputDialog(bruiService.getCurrentShell(), "Demo JFace Dialog", "��ʾJface����Ի���",
				"hello world!", null);
		id.open();

	}

}
