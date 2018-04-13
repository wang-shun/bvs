package com.bizvisionsoft.bruiengine.action;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
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
		context.selected(em -> {
			Editor<?> editor = new Editor<Object>(assembly, context).setInput(em).setEditable(editable);
			String label = AUtil.readLabel(em, "");
			if (label != null) {
				editor.setTitle(editable?("±à¼­ "+label):label);
			}

			editor.open((r, o) -> {
				GridPart grid = (GridPart) context.getContent();
				grid.modify(em, r);
			});
		});

	}

}
