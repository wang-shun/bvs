package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class SwitchContentToAssembly {

	@Inject
	private IBruiService bruiService;
	
	private Assembly assembly;

	public SwitchContentToAssembly(Assembly assembly) {
		this.assembly = assembly;
	}

	@Execute
	public void execute() {
		bruiService.switchContent(assembly, null);
	}
	
}
