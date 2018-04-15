package com.bizvisionsoft.pms.equipment.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.model.Equipment;
import com.bizvisionsoft.serviceconsumer.Services;

public class CreateEquipment {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context) {
		Editor.open("设备设施编辑器", context, new Equipment(), (r, i) -> {
			Equipment item = Services.get(CommonService.class).insertEquipment(i);
			GridPart grid = (GridPart) context.getContent();
			grid.insert(item);
		});
	}

}
