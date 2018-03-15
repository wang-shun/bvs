package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruidesigner.view.AssyLibView;

public class DeleteAssembly extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AssyLibView part = (AssyLibView) HandlerUtil.getActivePart(event);
		Assembly assy = (Assembly) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();
		part.removeAssembly(assy);
		return null;
	}

}
