package com.bizvisionsoft.pms.action;

import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.bruicommons.annotation.Execute;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.annotation.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.DataGrid;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.model.UserInfo;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class CreateUser {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		InputDialog id = new InputDialog(bruiService.getCurrentShell(), "�����û�", "�����û�Id", "username", null);
		if (InputDialog.OK == id.open()) {
			String userId = id.getValue();
			User user = new User().setActivated(true).setEmail("zh@bizvisionsoft.com").setHeadpicURL("/asdasd/aaa.png")
					.setName("�û�" + userId).setPassword("1").setTel("1234").setUserId(userId);
			UserService service = Services.get(UserService.class);
			service.insert(user);
			List<UserInfo> ds = service.createDataSet(new BasicDBObject().append("skip", 0).append("limit", 1)
					.append("filter", new BasicDBObject("userId", userId)));
			DataGrid grid = (DataGrid) context.getContext("�û��б�").getContent();
			grid.insert(ds.get(0), 0);
		}
	}

}
