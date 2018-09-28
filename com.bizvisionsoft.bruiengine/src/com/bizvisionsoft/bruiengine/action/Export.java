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
				logger.error("组件：" + context.getAssembly() + ", DataSet为 null。");
				throw new RuntimeException("当前组件不支持导出功能");
			}
		} else {
			logger.error("组件：" + context.getAssembly() + ", 不支持导出功能，必须使用实现IDataSetEngineProvider的组件。");
			throw new RuntimeException("当前组件不支持导出功能");
		}
	}

}
