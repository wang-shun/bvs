package com.bizvisionsoft.bruiengine;

import static org.eclipse.rap.rwt.RWT.getClient;
import static org.eclipse.rap.rwt.internal.service.ContextProvider.getApplicationContext;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.client.service.BrowserNavigation;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.rap.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.service.UserSession;
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
		UserSession.current().setEntryPoint(this);

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

	public String getResourceURL(String resPath) {
		if (resPath.trim().isEmpty()) {
			return null;
		}
		if (!resPath.startsWith("/")) {
			resPath = "/" + resPath;
		}
		String aliasOfResFolder = ModelLoader.site.getAliasOfResFolder();
		return "rwt-resources/" + aliasOfResFolder + resPath;
	}

	private void setBackground() {
		shell.setLayout(new FormLayout());
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		Label title = new Label(shell, SWT.NONE);
		title.setData(RWT.MARKUP_ENABLED, true);
		title.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
		String logo = Optional.ofNullable(ModelLoader.site.getHeadLogo()).map(t -> getResourceURL(t))
				.orElse("resource/image/logo_w.svg");
		title.setStyleAttribute("backgroundImage", logo);
		title.setStyleAttribute("background-repeat", "no-repeat");
		title.setStyleAttribute("background-size", "cover");

		Integer h = ModelLoader.site.getHeadLogoHeight();
		Integer w = ModelLoader.site.getHeadLogoWidth();
		FormData fd;
		if (w != null && h != null) {
			fd = new FormData(w, h);
		} else {
			fd = new FormData(150, 60);
		}
		fd.left = new FormAttachment(0, 16);
		fd.top = new FormAttachment(0, 16);
		title.setLayoutData(fd);

		Label footRight = new Label(shell, SWT.NONE);
		footRight.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		footRight.setText("ϵͳ״̬  |  ʹ������  |  ����Э��  |  ��������");
		// TODO ���Ӻ��ı���ʾ
		fd = new FormData();
		fd.right = new FormAttachment(100, -16);
		fd.bottom = new FormAttachment(100, -16);
		// fd.left = new FormAttachment(0,16);
		fd.height = 24;
		footRight.setLayoutData(fd);

		Label footLeft = new Label(shell, SWT.NONE);
		footLeft.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		String text = Optional.ofNullable(ModelLoader.site.getFootLeftText()).orElse("�人�����Ƽ����޹�˾ ��Ȩ����");
		footLeft.setText(text);
		fd = new FormData();
		fd.left = new FormAttachment(0, 16);
		fd.bottom = new FormAttachment(100, -16);
		// fd.left = new FormAttachment(0,16);
		fd.height = 24;
		footLeft.setLayoutData(fd);

		Composite bgimg = new Composite(shell, SWT.NONE);
		fd = new FormData();
		fd.right = new FormAttachment(100, 20);
		fd.bottom = new FormAttachment(100, 20);
		fd.left = new FormAttachment(0, -20);
		fd.top = new FormAttachment(0, -20);
		bgimg.setLayoutData(fd);

		bgimg.moveBelow(null);
		bgimg.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		String img = Optional.ofNullable(ModelLoader.site.getPageBackgroundImage()).map(t -> getResourceURL(t))
				.orElse("resource/image/bg/bg0" + (new Random().nextInt(3) + 1) + ".jpg");

		bgimg.setStyleAttribute("backgroundImage", img);
		bgimg.setStyleAttribute("background-size", "cover");
		bgimg.setStyleAttribute("background-repeat", "no-repeat");
		bgimg.setStyleAttribute("background-position", "center");
		bgimg.setHtmlAttribute("class", "brui_blur");
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
		RWT.getClient().getService(BrowserNavigation.class).addBrowserNavigationListener(event -> {
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
