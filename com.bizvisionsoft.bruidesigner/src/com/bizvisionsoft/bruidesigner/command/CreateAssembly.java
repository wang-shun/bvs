package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Folder;
import com.bizvisionsoft.bruidesigner.view.FolderView;

public class CreateAssembly extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String type = event.getParameter("assemblyType");
		FolderView part = (FolderView) HandlerUtil.getActivePart(event);
		IStructuredSelection sel = ((IStructuredSelection)HandlerUtil.getCurrentSelection(event));
		Folder folder = (Folder) sel.getFirstElement();
		part.createAssembly(type,folder);
		return null;
	}

}
