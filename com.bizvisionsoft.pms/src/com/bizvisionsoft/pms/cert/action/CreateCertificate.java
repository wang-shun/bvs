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
import com.bizvisionsoft.service.model.Certificate;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateCertificate {
	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		Editor.open("Ö´Òµ×Ê¸ñ±à¼­Æ÷", context, new Certificate(), (r, i) -> {
			Certificate item = Services.get(CommonService.class).insertCertificate(i);
			GridPart grid = (GridPart) context.getContent();
			grid.insert(item);
		});
	}
}
