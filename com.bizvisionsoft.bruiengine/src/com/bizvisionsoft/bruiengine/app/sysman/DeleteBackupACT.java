package com.bizvisionsoft.bruiengine.app.sysman;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.model.Backup;
import com.bizvisionsoft.serviceconsumer.Services;

public class DeleteBackupACT {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		context.selected(t -> {
			if (brui.confirm("删除备份", "请确定要删除本备份。")) {
				boolean ok = Services.get(SystemService.class).deleteBackup(((Backup) t).getId());
				if (ok) {
					((GridPart) context.getContent()).remove(t);
					Layer.message("已删除");
				} else {
					Layer.message("删除失败", Layer.ICON_CANCEL);
				}
			}
		});
	}

}
