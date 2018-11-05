package com.bizvisionsoft.bruiengine.app.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.StructuredSelection;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;

public class RequestAccountChangePasswordACT {

	@Inject
	private IBruiService br;

	@Execute
	private void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		StructuredSelection selection = context.getSelection();
		if (selection != null && !selection.isEmpty()) {
			if (br.confirm("要求用户更改密码", "请确认选择的账户登录后需要更改密码。")) {
				List<String> userids = selection.toList(new ArrayList<User>()).stream().map(user -> user.getUserId())
						.collect(Collectors.toCollection(ArrayList::new));
				Services.get(UserService.class).requestChangePassword(userids);
				Layer.message("已设置密码更改要求");
			}
		}

	}

}
