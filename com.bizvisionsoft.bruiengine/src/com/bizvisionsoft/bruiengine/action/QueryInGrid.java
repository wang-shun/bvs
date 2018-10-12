package com.bizvisionsoft.bruiengine.action;

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
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		// »ñµÃ
		IQueryEnable content = (IQueryEnable) context.getContent();
		content.openQueryEditor();
	}

}
