package com.bizvisionsoft.bruiengine.app.sysman;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.model.User;

public class OpenSysManPageACT {
	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		User user = brui.getCurrentUserInfo();
		if(!user.isAdmin()) {
			Layer.message("您没有获得系统管理的授权。", Layer.ICON_LOCK);
			return;
		}
		brui.switchPage("系统管理", null);
	}

}
