package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruidesigner.view.TemplateView;

public class CreateTemplate extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TemplateView part = (TemplateView) HandlerUtil.getActivePart(event);
		part.createTemplate();
		return null;
	}

}
