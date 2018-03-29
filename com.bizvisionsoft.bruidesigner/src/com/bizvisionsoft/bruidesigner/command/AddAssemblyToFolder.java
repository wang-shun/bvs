package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Folder;
import com.bizvisionsoft.bruidesigner.dialog.AssemblySelectionDialog;
import com.bizvisionsoft.bruidesigner.view.FolderView;

public class AddAssemblyToFolder extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FolderView part = (FolderView) HandlerUtil.getActivePart(event);
		IStructuredSelection sel = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		Folder folder = ((Folder) sel.getFirstElement());
		AssemblySelectionDialog dialog = new AssemblySelectionDialog(HandlerUtil.getActiveShell(event), true);
		if (AssemblySelectionDialog.OK == dialog.open()) {
			part.AddAssembliesToFolder( dialog.getResult(),folder);
		}
		return null;
	}

}
