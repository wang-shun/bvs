package com.bizvisionsoft.bruiengine.app.sysman;

import com.bizivisionsoft.widgets.datetime.DateTimeSetting;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.DateTimeInputDialog;

public class LogoutUserLaterACT {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		context.selected(t -> {
			DateTimeInputDialog dt = new DateTimeInputDialog(brui.getCurrentShell(), "��ʱ�ǳ�", "��ѡ���û��ǳ�ϵͳ��ʱ��", null,
					d -> d == null ? "����ѡ��ʱ��" : null).setDateSetting(DateTimeSetting.dateTime().setRange(false));
			if (dt.open() == DateTimeInputDialog.OK) {
				((UserSession) t).logout(dt.getValue());
			}
		});
	}

}
