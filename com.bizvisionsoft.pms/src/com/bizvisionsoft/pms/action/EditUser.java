package com.bizvisionsoft.pms.action;

import java.util.Optional;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.bruicommons.annotation.Execute;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.annotation.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.DataGrid;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.UserInfo;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class EditUser {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.ifFristElementSelected(elem -> {
			UserService service = Services.get(UserService.class);
			Optional.ofNullable(service.get(((UserInfo) elem).getUserId())).ifPresent(user -> {
				Editor editor = bruiService.openByName("用户编辑器", user, true, context);
				if (Window.OK == editor.open()) {
					FilterAndUpdate filterAndUpdate = new FilterAndUpdate()
							.filter(new BasicDBObject("userId", user.getUserId())).set(editor.getResult());
					long cnt = service.update(filterAndUpdate.bson());
					if (cnt == 1) {
						UserInfo info = service.info(user.getUserId());
						DataGrid grid = (DataGrid) context.getChildContextByAssemblyName("用户列表").getContent();
						grid.replaceItem(elem, info);
					}
				}
			});

		});
	}

}
