package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruidesigner.view.SiteView;

public class RemoveAssembly extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AssemblyLink assembly = (AssemblyLink) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event)).getFirstElement();
		SiteView part = (SiteView) HandlerUtil.getActivePart(event);
		part.removeAssembly(assembly);
		return null;
	}

}
