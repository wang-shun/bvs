package com.bizvisionsoft.pms.cbs.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.IStructuredDataPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.model.CBSItem;
import com.bizvisionsoft.service.model.CBSPeriod;
import com.bizvisionsoft.serviceconsumer.Services;

public class AddCBSPeriodItem {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(parent -> {
			Editor.create("ÆÚ¼äÔ¤Ëã±à¼­Æ÷", context, new CBSPeriod().setCBSItem_id(((CBSItem) parent).get_id()), true)
					.setTitle("±à¼­ÆÚ¼äÔ¤Ëã").ok((r, o) -> {
						Services.get(CBSService.class).insertCBSPeriod(o);
						IStructuredDataPart grid = (IStructuredDataPart) context.getContent();
						grid.refresh(parent);
					});

		});
	}

}
