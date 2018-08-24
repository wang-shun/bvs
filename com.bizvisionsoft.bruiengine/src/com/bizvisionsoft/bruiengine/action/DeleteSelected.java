package com.bizvisionsoft.bruiengine.action;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.IStructuredDataPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.EngUtil;

public class DeleteSelected {

	@Inject
	private IBruiService br;

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		context.selected(elem -> {
			deleteElementInGrid(br, context, elem);
		});
	}

	public static void deleteElementInGrid(IBruiService bruiService, IBruiContext context, Object elem) {
		String label = AUtil.readTypeAndLabel(elem);
		String message = Optional.ofNullable(label).map(m -> "��ȷ�Ͻ�Ҫɾ�� " + m).orElse("��ȷ�Ͻ�Ҫɾ��ѡ��ļ�¼��");
		if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "ɾ��", message)) {
			EngUtil.ifInstanceThen(context.getContent(), IStructuredDataPart.class, c->c.doDelete(elem));
		}
	}

}
