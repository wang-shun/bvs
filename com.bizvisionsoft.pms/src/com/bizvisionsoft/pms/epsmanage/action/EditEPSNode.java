package com.bizvisionsoft.pms.epsmanage.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.EPSService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.EPS;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditEPSNode {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		context.selected(elem->{
			if (elem instanceof EPS) {
				EPSService service = Services.get(EPSService.class);
				EPS eps = service.get(((EPS) elem).get_id());
				bruiService.createEditorByName("EPS±à¼­Æ÷", eps, true, false, context).open((r,t) -> {
					FilterAndUpdate filterAndUpdate = new FilterAndUpdate()
							.filter(new BasicDBObject("_id", ((EPS) elem).get_id())).set(r);
					Services.get(EPSService.class).update(filterAndUpdate.bson());
					GridPart grid = (GridPart) context.getContent();
					grid.replaceItem(elem, eps);
				});
			}
			
		});
		
		

	}

}
