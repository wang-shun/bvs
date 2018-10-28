package com.bizvisionsoft.bruiengine;

import static org.eclipse.rap.rwt.RWT.getClient;
import static org.eclipse.rap.rwt.internal.service.ContextProvider.getApplicationContext;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.client.service.BrowserNavigation;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.rap.rwt.internal.lifecycle.RWTLifeCycle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.service.tools.Check;

public class BruiEntryPoint implements EntryPoint, StartupParameters {

	public Logger logger = LoggerFactory.getLogger(getClass());

	private Display display;

	private Shell shell;

	private View currentView;

	private Map<String, Set<Listener>> keybindingMap = new HashMap<>();

	@Override
	public int createUI() {
		if (ModelLoader.reloadSiteForSession)
			try {
				ModelLoader.loadSite();
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
			}

		display = createDisplay();
		shell = new Shell(display, SWT.NO_TRIM);
		// setBackground();
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

	private Display createDisplay() {
		Display display = new Display();
		display.setData(RWT.CANCEL_KEYS, new String[] { "ESC" });
		display.addFilter(SWT.KeyDown, this::handleKeyEvent);
		return display;
	}

	private void handleKeyEvent(Event e) {
		String key = "" + e.stateMask + "+" + e.character;
		Set<Listener> listeners = keybindingMap.get(key.toUpperCase());
		if (listeners != null) {
			listeners.parallelStream().forEach(a -> a.handleEvent(e));
		}
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

	// private void setBackground() {
	// shell.setLayout(new FormLayout());
	// shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
	// Label title = new Label(shell, SWT.NONE);
	// title.setData(RWT.MARKUP_ENABLED, true);
	// title.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
	// String logo = Optional.ofNullable(ModelLoader.site.getHeadLogo()).map(t ->
	// getResourceURL(t))
	// .orElse("resource/image/logo_w.svg");
	// title.setStyleAttribute("backgroundImage", logo);
	// title.setStyleAttribute("background-repeat", "no-repeat");
	// title.setStyleAttribute("background-size", "cover");
	//
	// Integer h = ModelLoader.site.getHeadLogoHeight();
	// Integer w = ModelLoader.site.getHeadLogoWidth();
	// FormData fd;
	// if (w != null && h != null) {
	// fd = new FormData(w, h);
	// } else {
	// fd = new FormData(150, 60);
	// }
	// fd.left = new FormAttachment(0, 16);
	// fd.top = new FormAttachment(0, 16);
	// title.setLayoutData(fd);
	//
	// Label footRight = new Label(shell, SWT.NONE);
	// footRight.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	// footRight.setText("系统状态 | 使用条款 | 许可协议 | 关于我们");
	// // TODO 链接和文本显示
	// fd = new FormData();
	// fd.right = new FormAttachment(100, -16);
	// fd.bottom = new FormAttachment(100, -16);
	// // fd.left = new FormAttachment(0,16);
	// fd.height = 24;
	// footRight.setLayoutData(fd);
	//
	// Label footLeft = new Label(shell, SWT.NONE);
	// footLeft.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	// String text =
	// Optional.ofNullable(ModelLoader.site.getFootLeftText()).orElse("武汉曜正科技有限公司
	// 版权所有");
	// footLeft.setText(text);
	// fd = new FormData();
	// fd.left = new FormAttachment(0, 16);
	// fd.bottom = new FormAttachment(100, -16);
	// // fd.left = new FormAttachment(0,16);
	// fd.height = 24;
	// footLeft.setLayoutData(fd);
	//
	// Composite bgimg = new Composite(shell, SWT.NONE);
	// fd = new FormData();
	// fd.right = new FormAttachment(100, 20);
	// fd.bottom = new FormAttachment(100, 20);
	// fd.left = new FormAttachment(0, -20);
	// fd.top = new FormAttachment(0, -20);
	// bgimg.setLayoutData(fd);
	//
	// bgimg.moveBelow(null);
	// bgimg.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	// String img =
	// Optional.ofNullable(ModelLoader.site.getPageBackgroundImage()).map(t ->
	// getResourceURL(t))
	// .orElse("resource/image/bg/bg0" + (new Random().nextInt(3) + 1) + ".jpg");
	//
	// bgimg.setStyleAttribute("backgroundImage", img);
	// bgimg.setStyleAttribute("background-size", "cover");
	// bgimg.setStyleAttribute("background-repeat", "no-repeat");
	// bgimg.setStyleAttribute("background-position", "center");
	// Check.isAssigned(ModelLoader.site.getPageBackgroundImageCSS(),css->bgimg.setHtmlAttribute("class",
	// css));
	// }

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
				switchPage(getHomePage(), null, false);
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
		switchPage(getHomePage(), null, true);
	}

	public void home() {
		switchPage(getHomePage(), null, true);
	}

	public void switchPage(Page page, String inputUid, boolean addHistory) {
		if (currentView != null && !currentView.isDisposed()) {
			currentView.dispose();
		}

		String name = page.getTitle();
		if (Check.isNotAssigned(name)) {
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

	private Page getHomePage() {
		return ModelLoader.site.getHomePage();
	}

	private String getKeyStr(String kseq) {
		String[] ks = kseq.split("\\+");
		char ch = ks[ks.length - 1].toCharArray()[0];
		int keysMask = 0;
		for (int i = 0; i < ks.length - 1; i++) {
			if (ks[i].equalsIgnoreCase("ctrl"))
				keysMask = keysMask | SWT.CTRL;
			else if (ks[i].equalsIgnoreCase("alt"))
				keysMask = keysMask | SWT.ALT;
			else if (ks[i].equalsIgnoreCase("shift"))
				keysMask = keysMask | SWT.SHIFT;
		}
		if (keysMask == 0)
			return null;
		return "" + keysMask + "+" + ch;

	}

	public void keybinding(String kseq, Listener listener) {
		Set<String> keys = new HashSet<String>();
		Check.instanceOf(display.getData(RWT.ACTIVE_KEYS), String[].class).map(Arrays::asList).ifPresent(l -> keys.addAll(l));
		keys.add(kseq);
		String[] keyArr = keys.toArray(new String[0]);
		display.setData(RWT.ACTIVE_KEYS, keyArr);
		display.setData(RWT.CANCEL_KEYS, keyArr);

		String k = getKeyStr(kseq);
		Set<Listener> lis = keybindingMap.get(k);
		if (lis == null) {
			lis = new HashSet<>();
			keybindingMap.put(k, lis);
		}
		lis.add(listener);
	}

	public void keyunbinding(String kseq, Object listener) {
		Optional.ofNullable(keybindingMap.get(getKeyStr(kseq))).ifPresent(l -> l.remove(listener));
	}

}
