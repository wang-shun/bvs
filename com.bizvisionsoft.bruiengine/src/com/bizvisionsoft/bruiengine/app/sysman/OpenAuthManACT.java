package com.bizvisionsoft.bruiengine.app.sysman;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class OpenAuthManACT {
	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		brui.switchContent("»®œﬁ…Ë÷√", null);
	}

}
