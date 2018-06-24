package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class OpenPage {

	@Inject
	private IBruiService bruiService;
	
	private String pageName;

	public OpenPage(String pageName) {
		this.pageName = pageName;
	}

	@Execute
	public void execute() {
		bruiService.switchPage(pageName, null);
	}
	
}
