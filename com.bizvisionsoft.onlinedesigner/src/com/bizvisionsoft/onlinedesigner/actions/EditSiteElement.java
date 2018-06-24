package com.bizvisionsoft.onlinedesigner.actions;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruicommons.model.Site;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;

public class EditSiteElement {
	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		context.selected(em -> {
			if (em instanceof Site) {
				Editor.open("վ��༭��", context, (Site) em, (o, t) -> {
					((GridPart) context.getContent()).replaceItem(em, t);
				});
			} else if (em instanceof Page) {
				Editor.open("ҳ��༭��", context, (Page) em, (o, t) -> {
					((GridPart) context.getContent()).replaceItem(em, t);
				});
			}
		});
	}

}
