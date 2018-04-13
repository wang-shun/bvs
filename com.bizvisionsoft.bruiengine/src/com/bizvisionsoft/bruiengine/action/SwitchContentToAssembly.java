package com.bizvisionsoft.bruiengine.action;

import org.eclipse.swt.widgets.Event;

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

	public SwitchContentToAssembly(Assembly assembly, boolean openContent) {
		this.assembly = assembly;
		this.openContent = openContent;
	}

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		if(openContent) {
			bruiService.openContent(assembly, context.getFristElement());
		}else {
			bruiService.switchContent(assembly, context.getFristElement());
		}
	}
	
}
