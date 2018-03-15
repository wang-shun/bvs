package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.bruicommons.annotation.Execute;
import com.bizvisionsoft.bruicommons.annotation.Inject;
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
