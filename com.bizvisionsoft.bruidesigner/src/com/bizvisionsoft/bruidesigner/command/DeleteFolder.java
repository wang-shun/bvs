package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Folder;
import com.bizvisionsoft.bruidesigner.view.FolderView;

public class DeleteFolder extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FolderView part = (FolderView) HandlerUtil.getActivePart(event);
		Folder folder = (Folder) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();
		part.deleteFolder(folder);
		return null;
	}

}
