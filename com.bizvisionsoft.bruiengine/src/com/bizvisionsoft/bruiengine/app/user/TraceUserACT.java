package com.bizvisionsoft.bruiengine.app.user;

import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;

public class TraceUserACT {

	@Inject
	private IBruiService br;

	@Execute
	private void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			boolean trace = !Boolean.TRUE.equals(((User) em).getTrace());
			Services.get(UserService.class).trace(((User) em).getUserId(), trace);
			((User) em).setTrace(trace);
			GridPart gird = (GridPart) context.getContent();
			gird.update(em);
			if (trace) {
				Layer.message("已开始跟踪账户：" + em);
			}else {
				Layer.message("已停止跟踪账户：" + em);
			}
		});

	}

}
