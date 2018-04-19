package com.bizvisionsoft.pms.obs.action;

import java.util.Optional;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.model.OBSItem;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateOBSItem {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			OBSItem input = new OBSItem().setParent_id(((OBSItem) em).get_id())
					.setScope_id(((OBSItem) em).getScope_id());

			String message = Optional.ofNullable(AUtil.readType(em)).orElse("");
			message = "创建" + message + "OBS下级节点";
			Editor.create("OBS节点编辑器", context, input, true).setTitle(message).ok((r, t) -> {
				OBSItem item = Services.get(OBSService.class).insert(t);
				((GridPart) context.getContent()).add(em, item);
			});
		});
	}

}
