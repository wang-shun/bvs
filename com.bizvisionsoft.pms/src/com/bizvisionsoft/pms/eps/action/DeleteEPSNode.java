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

		context.selected(elem -> {
			if (elem instanceof EPS) {
				if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "ɾ��", "��ȷ��Ҫɾ��ѡ�е�EPS�ڵ���")) {
					try {
						Services.get(EPSService.class).delete(((EPS) elem).get_id());
						GridPart grid = (GridPart) context.getChildContextByAssemblyName("EPS���").getContent();
						grid.remove(elem);
					} catch (Exception e) {
						MessageDialog.openError(bruiService.getCurrentShell(), "ɾ��", e.getMessage());
					}
				}
			}
		});
	}

}
