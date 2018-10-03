package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.IClientSettable;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.service.tools.Check;

public class ClientSetting {

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		if (!Check.instanceThen(context.getContent(), IClientSettable.class, p -> p.clientSetting())) {
			throw new RuntimeException("组件：" + context.getAssembly() + ", 不支持客户端设置功能，必须使用实现IClientSettable的组件。");
		}
	}

}
