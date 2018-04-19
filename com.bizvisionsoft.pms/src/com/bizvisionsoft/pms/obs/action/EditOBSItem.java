package com.bizvisionsoft.pms.obs.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.action.OpenSelected;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.model.IOBSScope;
import com.bizvisionsoft.service.model.OBSItem;

public class EditOBSItem {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(em -> {
			String editName = isRootOBSItem(em, context.getRootInput()) ? "OBS¸ù±à¼­Æ÷" : "OBS½Úµã±à¼­Æ÷";
			Assembly assembly = bruiService.getAssembly(editName);
			new OpenSelected(assembly, true).execute(context);
		});

	}

	private boolean isRootOBSItem(Object em, Object input) {
		return em instanceof OBSItem && input instanceof IOBSScope
				&& ((OBSItem) em).get_id().equals(((IOBSScope) input).getObs_id());
	}

}
