package com.bizvisionsoft.pms.resourcetype.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.model.ResourceType;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateResourceType {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		Editor.open("资源类型编辑器", context, new ResourceType(), (r, i) -> {
			ResourceType item = Services.get(CommonService.class).insertResourceType(i);
			GridPart grid = (GridPart) context.getContent();
			grid.insert(item);
		});
	}

}
