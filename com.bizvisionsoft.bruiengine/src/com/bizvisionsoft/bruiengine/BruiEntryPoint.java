package com.bizvisionsoft.bruiengine;

import static org.eclipse.rap.rwt.RWT.getClient;
import static org.eclipse.rap.rwt.internal.service.ContextProvider.getApplicationContext;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.rap.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.View;

public class BruiEntryPoint implements EntryPoint, StartupParameters {

	private Display display;
	private Shell shell;

	@Override
	public int createUI() {
		if (Brui.reloadSiteForSession)
			try {
				Brui.loadSite();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		display = new Display();
		shell = new Shell(display, SWT.NO_TRIM);
		setBackground();
		UserSession.current().setShell(shell);

		shell.setMaximized(true);
		shell.layout();
		shell.open();

		start();
		if (getApplicationContext().getLifeCycleFactory().getLifeCycle() instanceof RWTLifeCycle) {
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		}
		return 0;
	}

	private void setBackground() {
		shell.setStyleAttribute("backgroundImage", "resource/image/bg/bg0" + (new Random().nextInt(4) + 1) + ".jpg");
		shell.setStyleAttribute("background-size", "100% 100%");
	}

	@Override
	public Collection<String> getParameterNames() {
		StartupParameters service = getClient().getService(StartupParameters.class);
		return service == null ? new ArrayList<String>() : service.getParameterNames();
	}

	@Override
	public String getParameter(String name) {
		StartupParameters service = getClient().getService(StartupParameters.class);
		return service == null ? null : service.getParameter(name);
	}

	@Override
	public List<String> getParameterValues(String name) {
		StartupParameters service = getClient().getService(StartupParameters.class);
		return service == null ? null : service.getParameterValues(name);
	}

	protected Shell getShell() {
		return shell;
	}

	protected void start() {
		Page page = Brui.site.getHomePage();
		View.create(page).open();
	}

}
