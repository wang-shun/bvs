package com.bizvisionsoft.bruiengine.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.session.UserSession;

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

	public static View create(Page page, Object input) {
		return new View(page, input);
	}

	public View(Page page, Object input) {
		super(UserSession.current().getShell());
		previous = new ArrayList<>();
		this.page = page;
		this.context = new BruiAssemblyContext();
		this.context.setInput(input);

		sidebarWidget = new SidebarWidget(page.getSidebar(), service, context);

	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setFullScreen(true);
		super.configureShell(newShell);
	}

	public int open() {
		if (page.isForceCheckLogin() || (page.isCheckLogin() && service.getCurrentUserInfo() == null))
			Optional.ofNullable(Brui.site.getLoginAssembly())
					.map(a -> new Popup(a, new BruiAssemblyContext()).setTitle("请验证您的身份").open());

		int result = super.open();

		return result;
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

		Assembly assembly = Brui.site.getAssembly(page.getContentArea().getAssemblyLinks().stream()
				.filter(al -> al.isDefaultAssembly()).findFirst().orElseThrow(NoSuchElementException::new).getId());
		createContentArea(assembly, null);

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

	private Composite createContentArea(Assembly assembly, Object input) {
		contentWidget = new ContentWidget(assembly, service, context);
		Composite contentArea = contentWidget.createUI(parent, input).getControl();
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

	public void openAssemblyInContentArea(Assembly assembly, Object input) {
		previous.add(contentWidget);
		Composite contentArea = createContentArea(assembly, input);
		contentArea.moveAbove(null);
		parent.layout();
	}

	public void closeCurrentContent() {
		contentWidget.getControl().dispose();
		contentWidget = previous.get(previous.size()-1);
		previous.remove(previous.size()-1);
		contentWidget.getControl().moveAbove(null);
		parent.layout();
	}

}
