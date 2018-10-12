package com.bizvisionsoft.onlinedesigner.actions;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.onlinedesigner.DesignerlToolkit;

public class RemoveSiteElement {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		context.selected(em -> {
			if (em instanceof Page) {
				if (MessageDialog.openConfirm(bruiService.getCurrentShell(), "删除", "请确认将要选中的页面。")) {
					DesignerlToolkit.removePage((Page) em);
					GridPart grid = (GridPart) context.getContent();
					grid.remove(em);
				}
			}
		});
	}
}
