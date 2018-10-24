package com.bizvisionsoft.onlinedesigner.systemupdate;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.serviceconsumer.Services;

public class SystemUpdateV0502_accountitem {
	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("���θ������ݣ�<br/>");
			sb.append("1. ���´����������Ŀ��ϵ��<br/>");
			sb.append("��ȷ�Ͻ��б��θ��¡�");
			if (brui.confirm("���·������Ŀ��ϵ", sb.toString())) {
				Services.get(SystemService.class).updateSystem("5.1M2", "accountitem");
				Layer.message("�������");
			}
		} catch (Exception e) {
			MessageDialog.openError(brui.getCurrentShell(), "������ɴ���", e.getMessage());
		}
	}
}
