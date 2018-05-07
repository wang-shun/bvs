package com.bizvisionsoft.bruiengine;

import static org.eclipse.rap.rwt.RWT.getClient;
import static org.eclipse.rap.rwt.internal.service.ContextProvider.getApplicationContext;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.client.service.BrowserNavigation;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.rap.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.bruiengine.util.Util;

public class BruiEntryPoint implements EntryPoint, StartupParameters {

	private Display display;
	private Shell shell;
	private View currentView;

	@Override
	public int createUI() {
		if (ModelLoader.reloadSiteForSession)
			try {
				ModelLoader.loadSite();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		display = new Display();
		shell = new Shell(display, SWT.NO_TRIM);
		setBackground();
		UserSession.current().setEntryPoint(this).setShell(shell);

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
		BrowserNavigation service = RWT.getClient().getService(BrowserNavigation.class);
		service.addBrowserNavigationListener(event -> {
			String state = event.getState();
			if (state.isEmpty()) {
				switchPage(ModelLoader.site.getHomePage(), null, false);
			} else {
				int idx = state.indexOf("/");
				String pageId, inputUid = null;
				if (idx != -1) {
					pageId = state.substring(0, idx);
					inputUid = state.substring(idx + 1);
				} else {
					pageId = state;
				}
				Page toPage = ModelLoader.site.getPageById(pageId);
				switchPage(toPage, inputUid, true);
			}
		});
		switchPage(ModelLoader.site.getHomePage(), null, true);
	}

	public void switchPage(Page page, String inputUid, boolean addHistory) {
		if (currentView != null && !currentView.isDisposed()) {
			currentView.dispose();
		}
				
		String name = page.getTitle();
		if (Util.isEmptyOrNull(name)) {
			name = page.getName();
		}
		View view;
		String uid = "";
		if (inputUid != null) {
			Object input = BruiPageInputDataSetEngine.create(page).getInput(inputUid);
			view = View.create(page, input);
			name += " - " + AUtil.readLabel(input, null);
			uid = "/" + inputUid;
		} else {
			view = View.create(page, null);
		}

		currentView = view;
		view.open();

		if (addHistory) {
			BrowserNavigation service = RWT.getClient().getService(BrowserNavigation.class);
			service.pushState(page.getId() + uid, name);
		}
	}

}
