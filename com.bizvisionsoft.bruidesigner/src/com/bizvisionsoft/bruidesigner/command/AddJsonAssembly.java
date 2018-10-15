package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.Folder;
import com.bizvisionsoft.bruidesigner.view.FolderView;

public class AddJsonAssembly extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FolderView part = (FolderView) HandlerUtil.getActivePart(event);
		IStructuredSelection sel = ((IStructuredSelection)HandlerUtil.getCurrentSelection(event));
		Folder folder = (Folder) sel.getFirstElement();
		InputDialog id = new InputDialog(HandlerUtil.getActiveShell(event), "创建组件", "组件JSON", "", null) {
			@Override
			protected int getInputTextStyle() {
				return SWT.MULTI | SWT.WRAP | SWT.BORDER;
			}

			@Override
			protected Control createDialogArea(Composite parent) {
				Control control = super.createDialogArea(parent);
				GridData layoutData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
				layoutData.heightHint = 600;
				layoutData.widthHint = 800;
				getText().setLayoutData(layoutData);
				return control;
			}
		};
		if(InputDialog.OK==id.open()) {
			part.createJsonAssembly(id.getValue(),folder);
		}
		return null;
	}

}
