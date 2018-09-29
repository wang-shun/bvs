package com.bizvisionsoft.bruiengine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.assembly.IDataSetEngineProvider;
import com.bizvisionsoft.bruiengine.assembly.IExportable;
import com.bizvisionsoft.bruiengine.service.IBruiContext;

public class Export {

	public Logger logger = LoggerFactory.getLogger(Export.class);

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(Execute.PARAM_ACTION) Action action) {
		Object content = context.getContent();
		// 判断content是否继承于IExportable
		if (content instanceof IExportable) {
			((IExportable) content).export();
		} else if (content instanceof IDataSetEngineProvider) {
			BruiDataSetEngine de = ((IDataSetEngineProvider) content).getDataSetEngine();
			if (de != null) {
				de.export(action.getName(), context);
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
