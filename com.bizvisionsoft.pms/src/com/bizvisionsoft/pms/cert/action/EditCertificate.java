package com.bizvisionsoft.pms.cert.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.Certificate;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditCertificate {
	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		context.selected(em -> {
			if (em instanceof Certificate) {
				new Editor<Certificate>(bruiService.getAssembly("Ö´Òµ×Ê¸ñ±à¼­Æ÷"), context).setInput((Certificate) em)
						.open((r, pjset) -> {
							FilterAndUpdate fu = new FilterAndUpdate()
									.filter(new BasicDBObject("_id", ((Certificate) em)._id)).set(r);
							if (Services.get(CommonService.class).updateCertificate(fu.bson()) == 1) {
								GridPart grid = (GridPart) context.getContent();
								grid.replaceItem(em, pjset);
							}
						});
			}
		});
	}
}
