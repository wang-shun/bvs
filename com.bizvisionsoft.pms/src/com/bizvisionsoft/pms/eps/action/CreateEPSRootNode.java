package com.bizvisionsoft.pms.eps.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.model.EPS;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateEPSRootNode {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		
		bruiService.createEditorByName("EPS±à¼­Æ÷", new EPS(), true,false, context).open((r,i)->{
			EPS item = Services.get(EPSService.class).insert(i);
			GridPart grid = (GridPart) context.getChildContextByAssemblyName("EPS±í¸ñ").getContent();
			grid.insert(item);
		});
		
	}

}
