package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruidesigner.view.FolderView;

public class DuplicateAssembly extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FolderView part = (FolderView) HandlerUtil.getActivePart(event);
		Assembly assy = (Assembly) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();
		part.duplicateAssembly(assy);
		return null;
	}

}
