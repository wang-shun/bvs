package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.assembly.IDataSetEngineProvider;
import com.bizvisionsoft.bruiengine.assembly.IExportable;
import com.bizvisionsoft.bruiengine.service.IBruiContext;

public class Export {

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(Execute.PARAM_ACTION) Action action) {
		Object content = context.getContent();
		// �ж�content�Ƿ�̳���IExportable
		if (content instanceof IExportable) {
			((IExportable) content).export();
		} else if (content instanceof IDataSetEngineProvider) {
			BruiDataSetEngine de = ((IDataSetEngineProvider) content).getDataSetEngine();
			if (de != null) {
				de.export(action.getName(), context);
			} else {
				String msg = "�����" + context.getAssembly() + ", DataSetΪ null��";
				throw new RuntimeException(msg);
			}
		} else {
			String msg = "�����" + context.getAssembly() + ", ��֧�ֵ������ܣ�����ʹ��ʵ��IDataSetEngineProvider�������";
			throw new RuntimeException(msg);
		}
	}

}
