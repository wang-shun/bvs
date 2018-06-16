package com.bizvisionsoft.bruiengine.app.sysman;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.model.User;

public class OpenAuthManACT {
	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		User user = brui.getCurrentUserInfo();
		if(!user.isAdmin()) {
			Layer.message("��ǰ�û��޷�ִ��Ȩ�޹��������", Layer.ICON_LOCK);
			return;
		}
		brui.switchContent("Ȩ�޹���", null);
	}

}
