package com.bizvisionsoft.bruiengine.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.BruiService;
import com.bizvisionsoft.bruiengine.service.PermissionUtil;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Check;

public class View extends Part {

	private Page page;

	private SidebarWidget sidebarWidget;

	private BruiAssemblyContext context;

	private ContentWidget contentWidget;

	private List<ContentWidget> previous;

	private Composite parent;

	private Composite headbar;

	private Composite sidebar;

	private Composite footbar;

	private BruiService service;

	public static View create(Page page, Object input) {
		return new View(page, input);
	}

	public View(Page page, Object input) {
		super(Display.getCurrent().getActiveShell());
		service = new BruiService(this);

		previous = new ArrayList<>();
		this.page = page;
		this.context = UserSession.newAssemblyContext();
		this.context.setInput(input);

		sidebarWidget = new SidebarWidget(this, page.getSidebar(), service, context);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setFullScreen(true);
		super.configureShell(newShell);
	}

	public int open() {
		Assembly la = ModelLoader.site.getLoginAssembly();
		BruiAssemblyContext c = UserSession.newAssemblyContext();

		/*
		 * 需要再次验证的，提示不同
		 */
		if (page.isForceCheckLogin()) {
			new Popup(la, c).setFullscreen(true).setTitle("请再次验证身份").open();
			checkPSWChangeRequest();
			return super.open();
		}

		/*
		 * 不需要验证的，直接跳过
		 */
		if (!page.isCheckLogin()) {
			service.loginUser();
			checkPSWChangeRequest();
			return super.open();
		}

		/*
		 * 当前进程保存了用户信息
		 */
		if (service.getCurrentUserInfo() != null) {
			service.loginUser();
			checkPSWChangeRequest();
			return super.open();
		}

		/*
		 * 浏览器Cookie保存了用户登录信息的，验证通过后打开页面
		 */
		String[] lc = service.loadClientLogin();
		try {
			service.checkLogin(lc[0], lc[1]);
			checkPSWChangeRequest();
			return super.open();
		} catch (Exception e) {
		}

		try {
			String welcome = Check.isAssignedThen(ModelLoader.site.getWelcome(), s -> s).orElse("登录WisPlanner 5");
			new Popup(la, c).setFullscreen(true).setTitle(welcome).open();
			checkPSWChangeRequest();
			return super.open();
		} catch (Exception e) {
			/*
			 * 出错不允许进入
			 */
			Layer.error(e);
			return 0;
		}
	}

	private void checkPSWChangeRequest() {
		User user = service.getCurrentUserInfo();
		if (user.isChangePSW()) {
			new Popup(ModelLoader.site.getChangePSWAssembly(), UserSession.newAssemblyContext()).setFullscreen(true).setTitle("请更改您的登录密码")
					.open();
		}
	}

	@Override
	public boolean close() {
		boolean result = super.close();
		return result;
	}

	@Override
	protected void createContents(Composite parent) {
		this.parent = parent;
		parent.setLayout(new FormLayout());
		// 创建顶栏
		if (page.getHeadbar().isEnabled()) {
			headbar = createHeadbar(parent);
		}
		if (page.getSidebar().isEnabled()) {
			sidebar = createSidebar(parent);
		}
		if (page.getFootbar().isEnabled()) {
			footbar = createFootbar(parent);
		}

		List<String> assembiesId = PermissionUtil.getRolebasedPageContent(service.getCurrentUserInfo(), page, context.getRootInput());
		String id = Brui.sessionManager.getDefaultPageAssembly(page.getId());
		if (id == null || !assembiesId.contains(id)) {
			id = assembiesId.get(0);
		}

		createContentArea(ModelLoader.site.getAssembly(id), null, null, false, null);

		FormData fd;
		if (headbar != null) {
			fd = new FormData();
			headbar.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = page.getHeadbar().getHeight();
		}

		if (sidebar != null) {
			fd = new FormData();
			sidebar.setLayoutData(fd);
			fd.top = headbar != null ? new FormAttachment(headbar) : new FormAttachment();
			fd.left = new FormAttachment();
			fd.width = page.getSidebar().getWidth();
			fd.bottom = footbar != null ? new FormAttachment(footbar) : new FormAttachment(100);
		}

		if (footbar != null) {
			fd = new FormData();
			footbar.setLayoutData(fd);
			fd.bottom = new FormAttachment(100);
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = page.getHeadbar().getHeight();
		}

	}

	private Composite createContentArea(Assembly assembly, Object input, String parameter, boolean closeable,
			Consumer<BruiAssemblyContext> callback) {
		contentWidget = new ContentWidget(assembly, service, context);
		contentWidget.setCloseCallback(callback);
		Composite contentArea = contentWidget.createUI(parent, input, parameter, closeable).getControl();
		FormData fd = new FormData();
		contentArea.setLayoutData(fd);
		fd.top = headbar != null ? new FormAttachment(headbar) : new FormAttachment();
		fd.left = sidebar != null ? new FormAttachment(sidebar) : new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = footbar != null ? new FormAttachment(footbar) : new FormAttachment(100);
		return contentArea;
	}

	private Composite createHeadbar(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}

	private Composite createFootbar(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}

	private Composite createSidebar(Composite parent) {
		return sidebarWidget.createUI(parent).getControl();
	}

	public void openAssemblyInContentArea(Assembly assembly, Object input, String parameter, Consumer<BruiAssemblyContext> callback) {
		previous.add(contentWidget);
		Composite contentArea = createContentArea(assembly, input, parameter, true, callback);
		contentArea.moveAbove(null);
		parent.layout();
	}

	public void closeCurrentContent() {
		contentWidget.getControl().dispose();
		contentWidget = previous.get(previous.size() - 1);
		previous.remove(previous.size() - 1);
		contentWidget.getControl().moveAbove(null);
		parent.layout();
	}

	public void switchAssemblyInContentArea(Assembly assembly, Object input, String parameter) {
		contentWidget.switchAssembly(assembly, input, parameter, false);
	}

	public Page getPage() {
		return page;
	}

	public void packSidebar(boolean packed) {
		FormData fd = new FormData();
		if (packed) {
			sidebar.setLayoutData(fd);
			fd.top = headbar != null ? new FormAttachment(headbar) : new FormAttachment();
			fd.left = new FormAttachment();
			fd.width = 48;
			fd.bottom = footbar != null ? new FormAttachment(footbar) : new FormAttachment(100);
		} else {
			fd = new FormData();
			sidebar.setLayoutData(fd);
			fd.top = headbar != null ? new FormAttachment(headbar) : new FormAttachment();
			fd.left = new FormAttachment();
			fd.width = page.getSidebar().getWidth();
			fd.bottom = footbar != null ? new FormAttachment(footbar) : new FormAttachment(100);
		}
		parent.layout(true);
	}

	public void updateSidebarActionBudget(String actionName) {
		if (sidebarWidget != null) {
			sidebarWidget.updateSidebarActionBudget(actionName);
		}
	}

	public void saveDefaultHomePageAssm(Assembly assm) {
		Brui.sessionManager.saveDefaultPageAssembly(page.getId(), assm.getId());
	}

}
