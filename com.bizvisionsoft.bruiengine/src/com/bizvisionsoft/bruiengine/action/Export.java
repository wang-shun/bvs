package com.bizvisionsoft.bruiengine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.assembly.IDataSetEngineProvider;
import com.bizvisionsoft.bruiengine.service.IBruiContext;

public class Export {

	public Logger logger = LoggerFactory.getLogger(Export.class);

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		Object content = context.getContent();
		if (content instanceof IDataSetEngineProvider) {
			BruiDataSetEngine de = ((IDataSetEngineProvider) content).getDataSetEngine();
			if (de != null) {
				de.export();
			} else {
				logger.error("�����" + context.getAssembly() + ", DataSetΪ null��");
				throw new RuntimeException("��ǰ�����֧�ֵ�������");
			}
		} else {
			logger.error("�����" + context.getAssembly() + ", ��֧�ֵ������ܣ�����ʹ��ʵ��IDataSetEngineProvider�������");
			throw new RuntimeException("��ǰ�����֧�ֵ�������");
		}
	}

}
