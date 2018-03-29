package com.bizvisionsoft.pms.action;

import java.util.Optional;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
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
				bruiService.createEditorByName("�û��༭��", user, true, false, context).open((r, t) -> {
					FilterAndUpdate filterAndUpdate = new FilterAndUpdate()
							.filter(new BasicDBObject("userId", user.getUserId())).set(r);
					if (service.update(filterAndUpdate.bson()) == 1) {
						UserInfo info = service.info(user.getUserId());
						GridPart grid = (GridPart) context.getContent();
						grid.replaceItem(elem, info);
					}
				});
			});

		});
	}

}
