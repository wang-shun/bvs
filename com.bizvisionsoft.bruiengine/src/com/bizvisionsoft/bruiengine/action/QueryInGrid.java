package com.bizvisionsoft.bruiengine.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.IQueryEnable;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class QueryInGrid {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		//���
		IQueryEnable content = (IQueryEnable) context.getContent();
		content.openQueryEditor();
	}

}
