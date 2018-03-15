package com.bizvisionsoft.bruidesigner.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ContentArea;
import com.bizvisionsoft.bruidesigner.dialog.AssemblySelectionDialog;
import com.bizvisionsoft.bruidesigner.view.SiteView;

public class AddAssembly extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SiteView part = (SiteView) HandlerUtil.getActivePart(event);
		AssemblySelectionDialog dialog = new AssemblySelectionDialog(HandlerUtil.getActiveShell(event), true);
		if (AssemblySelectionDialog.OK == dialog.open()) {
			Object[] result = dialog.getResult();
			if (result != null && result.length > 0) {
				List<String> assysIds = new ArrayList<String>();
				for (int i = 0; i < result.length; i++) {
					assysIds.add(((Assembly) result[i]).getId());
				}
				IStructuredSelection sel = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
				part.addAssemblies((ContentArea) sel.getFirstElement(), assysIds);
			}
		}
		return null;
	}

}
