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
import com.bizvisionsoft.onlinedesigner.DesignerlToolkit;

public class AddSiteElement {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		context.selected(em -> {
			if (em instanceof Site) {
				Page page = DesignerlToolkit.createPage((Site) em);
				Editor.create("ҳ��༭��", context, page, false).setTitle("������ҳ��").ok((o, t) -> {
					((GridPart) context.getContent()).add(em, t);
				});
			}
		});
	}

}
