package com.bizvisionsoft.bruiengine.ui;

import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;

public class ActionListMenu extends Part {

	private List<Action> actions;
	public final static int width = 168;
	private final static int idWidth = 16;
	private BruiAssemblyContext context;
	private IBruiService serive;

	public ActionListMenu(List<Action> actions) {
		super(UserSession.current().getShell());
		this.actions = actions;
		setShellStyle(SWT.ON_TOP);
	}

	@Override
	protected void createContents(Composite parent) {
		parent.setLayout(new FormLayout());
		Composite contentPanel = new Composite(parent, SWT.NONE);
		contentPanel.setBackground(BruiColors.getColor(BruiColor.Blue_Grey_700));
		// contentPanel.setHtmlAttribute("class", "menu");
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.type = SWT.VERTICAL;
		layout.fill = true;
		layout.spacing = 0;
		layout.marginBottom = 8;
		layout.marginLeft = 0;
		layout.marginTop = 8;
		layout.marginRight = 0;
		contentPanel.setLayout(layout);
		actions.forEach(a -> createMenuItem(contentPanel, a));

		Composite indicator = new Composite(parent, SWT.NONE);
		indicator.setData(RWT.CUSTOM_VARIANT, "left");

		FormData fd = new FormData();
		contentPanel.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.width = width - idWidth;
		fd.bottom = new FormAttachment(100);

		fd = new FormData();
		indicator.setLayoutData(fd);
		fd.top = new FormAttachment(50, -idWidth / 2);
		fd.left = new FormAttachment(contentPanel, 0);
		fd.width = 0;
		fd.height = 0;

	}

	private void createMenuItem(Composite parent, Action action) {
		Button l = new Button(parent, SWT.PUSH);
		UserSession.bruiToolkit().enableMarkup(l);
		l.setData(RWT.CUSTOM_VARIANT, "item");
		l.setText(UserSession.bruiToolkit().getActionMenuItemHtml(action));
		l.setLayoutData(new RowData(width - idWidth, 36));
		l.addListener(SWT.Selection, e -> BruiActionEngine.create(action, serive).invokeExecute(e, context));
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setData(RWT.CUSTOM_VARIANT, "menu");
		newShell.addListener(SWT.Deactivate, e -> close());
		super.configureShell(newShell);
	}

	@Override
	public Point getInitialSize() {
		return getShell().computeSize(width, SWT.DEFAULT, true);
	}

	public ActionListMenu setLocation(Point location) {
		getShell().setLocation(location);
		return this;
	}

	public ActionListMenu setContext(BruiAssemblyContext context) {
		this.context = context;
		return this;
	}

	public ActionListMenu setService(IBruiService service) {
		this.serive = service;
		return this;
	}

}
