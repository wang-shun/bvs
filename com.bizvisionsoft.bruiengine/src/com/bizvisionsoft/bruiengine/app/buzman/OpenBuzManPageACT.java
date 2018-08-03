package com.bizvisionsoft.bruiengine.app.buzman;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.model.User;

public class OpenBuzManPageACT {
	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		User user = brui.getCurrentUserInfo();
		if(!user.isBuzAdmin()&&!user.isSU()) {
			Layer.message("您没有获得业务管理的授权", Layer.ICON_LOCK);
			return;
		}
		brui.switchPage("业务管理", null);
	}

}
