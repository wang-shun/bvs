package com.bizvisionsoft.bruidesigner;

import org.eclipse.swt.SWT;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShellStyle(SWT.NO_TRIM);
		configurer.setShowCoolBar(true);
		configurer.setShowMenuBar(false);
		configurer.setShowStatusLine(false);
		configurer.setTitle("BrickUI Designer");
	}
	
	@Override
	public void postWindowCreate() {
		getWindowConfigurer().getWindow().getShell().setMaximized(true);
	}
}
