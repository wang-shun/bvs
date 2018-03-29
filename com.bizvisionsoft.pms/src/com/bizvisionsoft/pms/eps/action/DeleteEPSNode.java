package com.bizvisionsoft.pms.eps.action;

import org.eclipse.jface.dialogs.MessageDialog;
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

public class DeleteEPSNode {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {

		context.ifFristElementSelected(elem -> {
			if (elem instanceof EPS) {
				if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "删除", "您确认要删除当前的选择吗？")) {
					Services.get(EPSService.class).delete(((EPS) elem).get_id());
					GridPart grid = (GridPart) context.getChildContextByAssemblyName("EPS表格").getContent();
					grid.remove(elem);
				}
			}
		});
	}

}
