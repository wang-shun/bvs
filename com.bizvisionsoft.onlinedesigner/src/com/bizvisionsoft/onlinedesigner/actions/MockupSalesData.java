package com.bizvisionsoft.onlinedesigner.actions;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.serviceconsumer.Services;

public class MockupSalesData {
	
	@Execute
	public void execute() {
		Services.get(CommonService.class).mockupSalesData();
	}

}
