package com.bizvisionsoft.demo.rsclient;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.UserInfo;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class DemoAction {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_EVENT) Event event,
			@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		context.ifFristElementSelected(elem -> {
			String userId = ((UserInfo) elem).getUserId();
			InputDialog id = new InputDialog(bruiService.getCurrentShell(), "菜单项编辑用户", "输入用户姓名", "username", null);
			if (InputDialog.OK == id.open()) {
				String userName = id.getValue();
				System.out.println(userName);
				BasicDBObject filterAndUpdate = new BasicDBObject()
						.append("filter", new BasicDBObject("userId", userId))
						.append("update", new BasicDBObject("$set", new BasicDBObject("name", userName)));
				long cnt = Services.get(UserService.class).update(filterAndUpdate);
				if (cnt == 1) {
					GridPart grid = (GridPart) context.getChildContextByAssemblyName("用户列表").getContent();
					((UserInfo) elem).setName(userName);
					grid.update(elem);
				}
			}
		});
	}
}
