package com.bizvisionsoft.bruiengine.action;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.IStructuredDataPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class DeleteSelected {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(elem -> {
			deleteElementInGrid(bruiService, context, elem);
		});
	}

	public static void deleteElementInGrid(IBruiService bruiService, IBruiContext context, Object elem) {
		String label = AUtil.readTypeAndLabel(elem);
		String message = Optional.ofNullable(label).map(m -> "��ȷ�Ͻ�Ҫɾ�� " + m).orElse("��ȷ�Ͻ�Ҫɾ��ѡ��ļ�¼��");

		if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "ɾ��", message)) {
			Object content = context.getContent();
			if (content instanceof IStructuredDataPart) {
				((IStructuredDataPart) content).doDelete(elem);
				Layer.message(Optional.ofNullable(label).map(m -> "��ɾ�� "+m+" ��").orElse("��ɾ����"));
			}
		}
	}

}
