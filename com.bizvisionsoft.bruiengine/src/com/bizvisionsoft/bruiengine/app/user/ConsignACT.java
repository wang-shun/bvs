package com.bizvisionsoft.bruiengine.app.user;

import org.eclipse.swt.widgets.Event;

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
	private void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			Selector.open("用户选择器—单选", context, null, s -> {
				String consigner = ((User) s.get(0)).getUserId();
				Services.get(UserService.class).consign(((User) em).getUserId(), consigner);
				((User) em).setConsigner(consigner);
				GridPart gird = (GridPart) context.getContent();
				gird.update(em);
			});
		});

	}

}
