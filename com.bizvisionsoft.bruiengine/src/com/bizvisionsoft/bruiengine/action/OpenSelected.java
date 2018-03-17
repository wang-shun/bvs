package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.bruicommons.annotation.Execute;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.annotation.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class OpenSelected {

	@Inject
	private IBruiService bruiService;

	private Assembly assembly;
	private boolean editable;

	public OpenSelected(Assembly assembly, boolean editable) {
		this.assembly = assembly;
		this.editable = editable;
	}

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		context.ifFristElementSelected(elem -> {
			bruiService.open(assembly, elem, editable, false, context);

			// TODO 默认的打开方式

		});
	}

}
