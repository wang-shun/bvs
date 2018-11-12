package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.onlinedesigner.IClientColumnCustomizable;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.service.tools.Check;

public class ClientSetting {

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		if (!Check.instanceThen(context.getContent(), IClientColumnCustomizable.class, IClientColumnCustomizable::customize)) {
			throw new RuntimeException("�����" + context.getAssembly() + ", ��֧�ֿͻ������ù��ܣ�����ʹ��ʵ��IClientCustomizable�������");
		}
	}

}
