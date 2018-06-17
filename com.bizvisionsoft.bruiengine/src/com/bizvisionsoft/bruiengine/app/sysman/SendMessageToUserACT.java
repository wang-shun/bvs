package com.bizvisionsoft.bruiengine.app.sysman;

import org.eclipse.jface.dialogs.InputDialog;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.model.User;

public class SendMessageToUserACT {

	@Inject
	private IBruiService br;

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		context.selected(t -> {
			User user = ((UserSession) t).getLoginUser();
			InputDialog id = new InputDialog(br.getCurrentShell(), "消息", user + ":", "",
					i -> i.trim().isEmpty() ? "消息不可为空" : null).setTextMultiline(true);
			if (id.open() == InputDialog.OK)
				((UserSession) t).sendMessage("系统管理员发来消息：<br/>"+id.getValue());
		});
	}

}
