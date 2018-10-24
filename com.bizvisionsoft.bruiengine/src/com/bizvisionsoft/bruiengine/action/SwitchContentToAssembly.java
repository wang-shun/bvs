package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class SwitchContentToAssembly {

	@Inject
	private IBruiService bruiService;

	private Assembly assembly;

	private boolean openContent;

	private String passParameters;

	public SwitchContentToAssembly(Assembly assembly, boolean openContent) {
		this.assembly = assembly;
		this.openContent = openContent;
	}

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		if (openContent) {
			bruiService.openContent(assembly, context.getFirstElement(),passParameters);
		} else {
			bruiService.switchContent(assembly, context.getFirstElement(),passParameters);
		}
	}

	public SwitchContentToAssembly passParameters(String passParametersToAssembly) {
		this.passParameters = passParametersToAssembly;
		return this;
	}

}
