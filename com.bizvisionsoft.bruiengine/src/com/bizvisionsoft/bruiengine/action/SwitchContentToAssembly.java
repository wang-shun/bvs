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

	public SwitchContentToAssembly(Assembly assembly) {
		this.assembly = assembly;
	}

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		bruiService.switchContent(assembly, context.getFristElement());
	}
	
}
