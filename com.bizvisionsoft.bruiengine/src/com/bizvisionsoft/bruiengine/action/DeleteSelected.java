package com.bizvisionsoft.bruiengine.action;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class DeleteSelected {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(elem -> {
			String message = Optional.ofNullable(AUtil.readTypeAndLabel(elem)).map(m -> "请确认将要删除 " + m)
					.orElse("请确认将要删除选择的记录。");

			if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "删除", message)) {
				GridPart grid = (GridPart) context.getContent();
				grid.doDelete(elem);
			}
		});
	}

}
