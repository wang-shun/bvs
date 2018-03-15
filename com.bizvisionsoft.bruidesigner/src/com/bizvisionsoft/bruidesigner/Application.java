package com.bizvisionsoft.bruidesigner;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.ExitConfirmation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * This class controls all aspects of the application's execution and is
 * contributed through the plugin.xml.
 */
public class Application implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();

		ExitConfirmation service = RWT.getClient().getService(ExitConfirmation.class);
		service.setMessage("请在退出前提交您的修改?");

		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
		return PlatformUI.createAndRunWorkbench(display, advisor);
	}

	public void stop() {
		// Do nothing
	}
}
