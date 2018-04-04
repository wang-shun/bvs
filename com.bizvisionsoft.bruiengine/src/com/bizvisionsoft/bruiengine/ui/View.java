package com.bizvisionsoft.bruiengine.ui;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.session.UserSession;

public class View extends Part {

	private Page page;
	
	private SidebarWidget sidebarWidget;
	
	private ContentWidget contentWidget;

	private BruiAssemblyContext context;

	public static View create(Page page,Object input) {
		return new View(page,input);
	}

	public View(Page page,Object input) {
		super(UserSession.current().getShell());
		this.page = page;
		this.context = new BruiAssemblyContext();
		this.context.setInput(input);
		
		sidebarWidget = new SidebarWidget(page.getSidebar(),service,context);
		contentWidget = new ContentWidget(page.getContentArea(),service,context);

	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setFullScreen(true);
		super.configureShell(newShell);
	}

	public int open() {
		if (page.isForceCheckLogin() || (page.isCheckLogin() && service.getCurrentUserInfo() == null))
			Optional.ofNullable(Brui.site.getLoginAssembly()).map(a -> new Popup(a,new BruiAssemblyContext()).setTitle("请验证您的身份").open());

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
		parent.setLayout(new FormLayout());
		// 创建顶栏
		Composite headbar = null;
		if (page.getHeadbar().isEnabled()) {
			headbar = createHeadbar(parent);
		}
		Composite sidebar = null;
		if (page.getSidebar().isEnabled()) {
			sidebar = createSidebar(parent);
		}
		Composite footbar = null;
		if (page.getFootbar().isEnabled()) {
			footbar = createFootbar(parent);
		}

		Composite contentArea = createContentArea(parent);

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

		fd = new FormData();
		contentArea.setLayoutData(fd);
		fd.top = headbar != null ? new FormAttachment(headbar) : new FormAttachment();
		fd.left = sidebar != null ? new FormAttachment(sidebar) : new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = footbar != null ? new FormAttachment(footbar) : new FormAttachment(100);

	}

	private Composite createContentArea(Composite parent) {
		return contentWidget.createUI(parent).getControl();
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


}
