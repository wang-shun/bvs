package com.bizvisionsoft.onlinedesigner.actions;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateIndexACT {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		Services.get(CommonService.class).createIndex();
	}
}
