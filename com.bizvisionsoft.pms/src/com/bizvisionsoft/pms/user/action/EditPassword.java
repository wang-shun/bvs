package com.bizvisionsoft.pms.user.action;

import java.util.Optional;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.UserInfo;
import com.bizvisionsoft.service.model.UserPassword;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditPassword {

	@Inject
	private IBruiService bruiService;

	@Execute
	private void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		open(bruiService, context, "�޸��˻�����");
	}

	protected void open(IBruiService bruiService, IBruiContext context, String editorName) {
		context.selected(elem -> {
			UserService service = Services.get(UserService.class);
			Optional.ofNullable(service.get(((UserInfo) elem).getUserId())).ifPresent(user -> {
				
				bruiService.createEditorByName(editorName, new UserPassword(), true, false, context).open((r, t) -> {
					FilterAndUpdate filterAndUpdate = new FilterAndUpdate()
							.filter(new BasicDBObject("userId", user.getUserId())).set(r);
					service.update(filterAndUpdate.bson());
				});
			});
		});
	}

}
