package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Template;
import com.bizvisionsoft.bruidesigner.view.TemplateView;

public class DeleteTemplate extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TemplateView part = (TemplateView) HandlerUtil.getActivePart(event);
		Template template = (Template) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();
		part.removeTemplate(template);
		return null;
	}

}
