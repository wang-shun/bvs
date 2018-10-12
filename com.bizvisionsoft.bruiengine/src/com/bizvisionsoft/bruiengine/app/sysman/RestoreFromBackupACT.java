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
			if (brui.confirm("����", "<span class='layui-badge'>Σ�ղ���</span>����ȷ������Ҫʹ�ñ��ݻָ�ϵͳ��<br>��������ɾ����ǰϵͳ���е����ݡ�")) {
				boolean ok = Services.get(SystemService.class).restoreFromBackup(((Backup) t).getId());
				if (ok) {
					Layer.message("��������ݻָ�");
				} else {
					Layer.message("���ݻָ�ʧ��");
				}
			}
		});
	}

}
