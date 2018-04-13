package com.bizvisionsoft.pms.cert.action;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.model.Certificate;
import com.bizvisionsoft.serviceconsumer.Services;

public class DeleteCertificate {
	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		context.selected(elem -> {
			if (elem instanceof Certificate) {
				if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "ɾ��", "��ȷ�Ͻ�Ҫɾ��ѡ���ִҵ�ʸ�")) {
					try {
						Services.get(CommonService.class).deleteCertificate(((Certificate) elem)._id);
						GridPart grid = (GridPart) context.getContent();
						grid.remove(elem);
					} catch (Exception e) {
						MessageDialog.openError(bruiService.getCurrentShell(), "ɾ��", e.getMessage());
					}
				}
			}
		});
	}
}
