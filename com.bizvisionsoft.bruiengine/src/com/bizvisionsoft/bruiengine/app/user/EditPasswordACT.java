package com.bizvisionsoft.bruiengine.app.user;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.model.UserPassword;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditPasswordACT {

	@Inject
	private IBruiService bruiService;

	@Execute
	private void execute(@MethodParam(Execute.CONTEXT) IBruiContext context,
			@MethodParam(Execute.EVENT) Event event) {
		open(bruiService, context, "�޸��˻�����");
	}

	protected void open(IBruiService bruiService, IBruiContext context, String editorName) {
		context.selected(user -> {
			UserService service = Services.get(UserService.class);
			Editor.open(editorName, context, new UserPassword(), true, (r, t) -> {
				FilterAndUpdate filterAndUpdate = new FilterAndUpdate()
						.filter(new BasicDBObject("userId", ((User) user).getUserId())).set(r);
				service.update(filterAndUpdate.bson());
			});
		});
	}

}
