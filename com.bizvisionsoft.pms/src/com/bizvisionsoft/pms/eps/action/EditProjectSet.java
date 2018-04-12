package com.bizvisionsoft.pms.eps.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.ProjectSetService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.ProjectSet;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditProjectSet {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			if (em instanceof ProjectSet) {
				new Editor<ProjectSet>(bruiService.getAssembly("项目集编辑器"), context).setTitle("编辑项目集")

						.setInput((ProjectSet) em)

						.open((r, pjset) -> {
							FilterAndUpdate filterAndUpdate = new FilterAndUpdate()
									.filter(new BasicDBObject("_id", ((ProjectSet) em).get_id())).set(r);
							if (Services.get(ProjectSetService.class).update(filterAndUpdate.bson()) == 1) {
								GridPart grid = (GridPart) context.getContent();
								grid.replaceItem(em, pjset);
							}
						});
			}
		});

	}

}
