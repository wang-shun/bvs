package com.bizvisionsoft.bruiengine.action;

import org.eclipse.jface.window.Window;

import com.bizvisionsoft.bruicommons.annotation.Execute;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.annotation.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiEngine;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;

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
			Object info = BruiEngine.deepCopy(elem);
			Editor editor = bruiService.createEditor(assembly, info, editable, false, context);
			if (Window.OK == editor.open()) {
				((GridPart) context.getContent()).replaceItem(elem, info);
			}
		});
	}

}
