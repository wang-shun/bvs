package com.bizvisionsoft.bruiengine.action;

import java.util.Optional;

import org.eclipse.core.runtime.Platform;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;

public class CreateItem {

	@Inject
	private IBruiService bruiService;

	private Assembly assembly;

	private String bundleId;

	private String className;

	public CreateItem(Assembly assembly, String bundleId, String className) {
		this.assembly = assembly;
		this.bundleId = bundleId;
		this.className = className;
	}

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		Object parent = context.getFirstElement();
		try {
			Object input = Platform.getBundle(bundleId).loadClass(className).newInstance();
			String message = Optional.ofNullable(AUtil.readType(input)).orElse("");
			Editor<?> editor = new Editor<Object>(assembly, context).setInput(input);
			editor.setTitle("´´½¨ " + message);
			editor.ok((r, o) -> {
				GridPart grid = (GridPart) context.getContent();
				grid.doCreate(parent, o);
			});

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
