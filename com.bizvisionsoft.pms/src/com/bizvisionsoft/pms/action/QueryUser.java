package com.bizvisionsoft.pms.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.bruicommons.annotation.Execute;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.annotation.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.DataGrid;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class QueryUser {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		//»ñµÃ
		DataGrid content = (DataGrid) context.getChildContextByName("demo").getContent();
		content.doQuery();
	}

}
