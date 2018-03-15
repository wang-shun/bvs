package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruidesigner.view.AssyLibView;

public class CreateAssembly extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AssyLibView part = (AssyLibView) HandlerUtil.getActivePart(event);
		part.createAssembly(null);
		return null;
	}

}
