package com.bizvisionsoft.bruiengine.app.user;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.serviceconsumer.Services;

public class RequestAllAccountChangePasswordACT {

	@Inject
	private IBruiService br;

	@Execute
	private void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		if (br.confirm("Ҫ�������û���������", "��ȷ�������˻���¼����Ҫ�������롣")) {
			Services.get(UserService.class).requestAllChangePassword();
			Layer.message("�������������Ҫ��");
		}
	}

}
