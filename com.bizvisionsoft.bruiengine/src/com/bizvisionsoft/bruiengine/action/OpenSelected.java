package com.bizvisionsoft.bruiengine.action;

import java.util.Optional;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.assembly.IStructuredDataPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;

public class OpenSelected {

	@Inject
	private IBruiService br;

	public OpenSelected() {
	}

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context,
			@MethodParam(Execute.ACTION) Action action) {
		Assembly assembly = context.getAssembly();
		context.selected(em -> {
			boolean editable = action.isEditorAssemblyEditable();

			String editorId = action.getEditorAssemblyId();
			Assembly editorAsm = ModelLoader.site.getAssembly(editorId);

			if (editorAsm == null) {
				String[] paramemterNames = new String[] { MethodParam.CONTEXT_INPUT_OBJECT,
						MethodParam.CONTEXT_INPUT_OBJECT_ID, MethodParam.ROOT_CONTEXT_INPUT_OBJECT,
						MethodParam.ROOT_CONTEXT_INPUT_OBJECT_ID, MethodParam.CURRENT_USER,
						MethodParam.CURRENT_USER_ID };
				Object[] parameterValues = context.getContextParameters(paramemterNames);
				Object editorConfig = AUtil.readEditorConfig(em, assembly.getName(), action.getName(), parameterValues,
						paramemterNames);
				String editorName = null;
				if (editorConfig instanceof String) {
					editorName = (String) editorConfig;
				} else if (editorConfig instanceof String[] && ((String[]) editorConfig).length > 1) {
					editorName = editable ? ((String[]) editorConfig)[0] : ((String[]) editorConfig)[1];
				} else if (editorConfig instanceof Object[] && ((Object[]) editorConfig).length > 1) {
					editable = Boolean.TRUE.equals(((Object[]) editorConfig)[1]);
					editorName = (String) ((Object[]) editorConfig)[0];
				}
				if(editorName!=null) {
					editorAsm = br.getAssembly(editorName);
				}
			}

			if (editorAsm == null)
				throw new RuntimeException("无法确定打开对象使用的编辑器。");

			String message = Optional.ofNullable(AUtil.readTypeAndLabel(em)).orElse("");
			Editor<Object> editor = new Editor<Object>(editorAsm, context).setEditable(editable)
					// .setTitle(editable ? ("编辑 " + message) : message);
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
