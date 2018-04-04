package com.bizvisionsoft.pms.eps.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.service.model.ProjectInfo;
import com.bizvisionsoft.service.model.ProjectSet;

public class OpenProjectOrProjectSet {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			if (em instanceof ProjectSet) {
				
			}else if(em instanceof ProjectInfo) {
				Page page = Brui.site.getPage("ÏîÄ¿Ê×Ò³");
				View.create(page,em).open();
			}
		});

	}

}
