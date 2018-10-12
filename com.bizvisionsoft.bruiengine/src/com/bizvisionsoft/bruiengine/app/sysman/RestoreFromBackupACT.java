package com.bizvisionsoft.bruiengine.app.sysman;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.model.Backup;
import com.bizvisionsoft.serviceconsumer.Services;

public class RestoreFromBackupACT {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		context.selected(t -> {
			if (brui.confirm("警告", "<span class='layui-badge'>危险操作</span>，请确认您将要使用备份恢复系统！<br>本操作将删除当前系统所有的数据。")) {
				boolean ok = Services.get(SystemService.class).restoreFromBackup(((Backup) t).getId());
				if (ok) {
					Layer.message("已完成数据恢复");
				} else {
					Layer.message("数据恢复失败");
				}
			}
		});
	}

}
