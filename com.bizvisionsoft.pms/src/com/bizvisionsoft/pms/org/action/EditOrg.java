package com.bizvisionsoft.pms.org.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.OrganizationService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.Organization;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditOrg {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			if (em instanceof Organization) {
				new Editor<Organization>(bruiService.getEditor("组织编辑器"), context).setTitle("编辑组织")

						.setInput((Organization) em)

						.open((r, pjset) -> {
							FilterAndUpdate fu = new FilterAndUpdate()
									.filter(new BasicDBObject("_id", ((Organization) em).get_id())).set(r);
							if (Services.get(OrganizationService.class).update(fu.bson()) == 1) {
								GridPart grid = (GridPart) context.getContent();
								grid.replaceItem(em, pjset);
							}
						});
			}
		});
	}

}
