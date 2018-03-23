package com.bizvisionsoft.pms.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class CreateProject {
	
	@Inject
	private IBruiService bruiService;
	
	@Execute
	public void execute() {
		Shell shell = new Shell(bruiService.getCurrentShell(),SWT.BORDER|SWT.RESIZE|SWT.MAX|SWT.DIALOG_TRIM);
		shell.setBounds(200, 200, 600, 400);
		shell.setText("Test ≤‚ ‘");
		shell.open();
	}

}
