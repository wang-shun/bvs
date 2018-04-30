package com.bizvisionsoft.bruiengine.action;

import java.util.Optional;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.assembly.IStructuredDataPart;
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
			String message = Optional.ofNullable(AUtil.readTypeAndLabel(em)).orElse("");

			Editor<Object> editor = new Editor<Object>(assembly, context).setEditable(editable)
					// .setTitle(editable ? ("±à¼­ " + message) : message);
					.setTitle(message);

			if (editable) {
				IStructuredDataPart grid = (IStructuredDataPart) context.getContent();
				Object input = grid.doGetEditInput(em);
				if (input != null) {
					editor.setInput(true, input);
				} else {
					editor.setInput(false, em);
				}
				editor.ok((r, o) -> {
					grid.doModify(em, o, r);
				});
			} else {
				editor.setInput(true, em);
				editor.open();
			}

		});

	}

}
