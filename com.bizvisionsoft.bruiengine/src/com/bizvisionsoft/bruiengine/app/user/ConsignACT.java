package com.bizvisionsoft.bruiengine.app.user;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Selector;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;

public class ConsignACT {

	@Inject
	private IBruiService br;

	@Execute
	private void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		context.selected(em -> {
			User user = (User) em;
			if (user.getConsigner() != null) {
				if (MessageDialog.openQuestion(br.getCurrentShell(), "账户托管", "账户：" + user + "已托管到" + user.getConsigner() + "，是否取消托管？")) {
					Services.get(UserService.class).disconsign(user.getUserId());
					user.setConsigner(null);
					GridPart gird = (GridPart) context.getContent();
					gird.update(em);
					Layer.message("已取消账户托管");
				}
				return;
			}
			Selector.open("用户选择器—单选", context, null, s -> {

				String consigner = ((User) s.get(0)).getUserId();
				Services.get(UserService.class).consign(user.getUserId(), consigner);
				user.setConsigner(consigner);
				GridPart gird = (GridPart) context.getContent();
				gird.update(em);
				Layer.message("账户：" + user + "已托管到" + user.getConsigner());
			});
		});

	}

}
