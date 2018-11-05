package com.bizvisionsoft.bruiengine.app.user;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.model.UserPassword;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditPasswordACT {

	@Execute
	private void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		context.selected(user -> {
			open(context, (User) user);
		});
	}

	public static void open(IBruiContext context, User user) {
		UserService service = Services.get(UserService.class);
		Editor.open("ÐÞ¸ÄÕË»§ÃÜÂë", context, new UserPassword(), true, (r, t) -> {
			FilterAndUpdate filterAndUpdate = new FilterAndUpdate().filter(new BasicDBObject("userId", ((User) user).getUserId())).set(r);
			service.update(filterAndUpdate.bson());
		});
	}

}
