package com.bizvisionsoft.bruiengine.ui;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridColumnLabelProvider;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Sidebar;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.BruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;

public class SidebarWidget {

	class ActionContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((List<Action>) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			List<Action> children = ((Action) parentElement).getChildren();
			if (children != null)
				return children.toArray(new Action[0]);
			return new Action[0];
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}

	}

	private Sidebar sidebar;

	private BruiAssemblyEngine brui;

	private BruiService service;

	private Composite panel;

	private BruiToolkit bruiToolkit;

	private BruiAssemblyContext context;

	public SidebarWidget(Sidebar sidebar, BruiService service, BruiAssemblyContext parentContext) {
		this.sidebar = sidebar;
		this.service = service;
		parentContext.add(context = new BruiAssemblyContext().setParent(parentContext));
		bruiToolkit = UserSession.bruiToolkit();

		// 1. 创建实例,注入并初始化
		brui = BruiAssemblyEngine.create(sidebar.getHeader(), service, context);
	}

	public SidebarWidget createUI(Composite parent) {
		panel = new Composite(parent, SWT.NONE);
		panel.setLayout(new FormLayout());
		Control header = null;
		header = createHeader(panel);
		Control menu = createSidebarMenu(panel);
		Control toolbar = createToolbar(panel);

		FormData fd;
		if (header != null) {
			fd = new FormData();
			header.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = 48;
			fd.width = sidebar.getWidth();
		}

		if (toolbar != null) {
			fd = new FormData();
			toolbar.setLayoutData(fd);
			fd.bottom = new FormAttachment(100);
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = 48;
		}

		fd = new FormData();
		menu.setLayoutData(fd);
		fd.top = header != null ? new FormAttachment(header) : new FormAttachment();
		fd.bottom = toolbar != null ? new FormAttachment(toolbar) : new FormAttachment(100);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);

		return this;
	}

	public Composite getControl() {
		return panel;
	}

	private Control createToolbar(Composite parent) {
		Composite bar = new Composite(parent, SWT.NONE);
		bar.setBackgroundMode(SWT.INHERIT_DEFAULT);
		bar.setBackground(BruiColors.getColor(BruiColor.Grey_900));
		bar.setOrientation(SWT.RIGHT_TO_LEFT);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		layout.marginHeight = 12;
		layout.marginWidth = 12;
		layout.spacing = 24;
		bar.setLayout(layout);
		sidebar.getToolbarItems().forEach(a -> {
			Label btn = new Label(bar, SWT.NONE);
			btn.setToolTipText(a.getTooltips());
			bruiToolkit.enableMarkup(btn);
			btn.setText("<img alter='btn' src='" + service.getResourceURL(a.getImage())
					+ "' style='cursor:pointer;' width='24px' height='24px'></img>");
			btn.setLayoutData(new RowData(24, 24));
			btn.addListener(SWT.MouseDown, e -> {
				run(a, e);
			});
		});
		return bar;
	}

	private void run(Action action, Event e) {
		BruiActionEngine.execute(action, e, context, service);
	}

	private Control createHeader(Composite parent) {
		Composite bar = new Composite(parent, SWT.NONE);
		bar.setBackgroundMode(SWT.INHERIT_DEFAULT);
		bar.setHtmlAttribute("class", "gradient-indigo");
		// WidgetHandler.getHandler(bar).setClass("gradient-indigo");
		// bar.setBackground(BruiColors.getColor(BruiColor.Indigo_700));
		brui.createUI(bar);
		return bar;
	}

	private Control createSidebarMenu(Composite parent) {
		GridTreeViewer viewer = new GridTreeViewer(parent, SWT.FULL_SELECTION);
		viewer.getGrid().setLinesVisible(false);
		viewer.getGrid().setData(RWT.CUSTOM_VARIANT, "menu");
		viewer.getGrid().setHideIndentionImage(true);
		viewer.getGrid().setOverlayWidth(4);
		viewer.getGrid().setBackground(BruiColors.getColor(BruiColor.Grey_1000));
		bruiToolkit.enableMarkup(viewer.getGrid());
		viewer.setContentProvider(new ActionContentProvider());
		GridViewerColumn column = new GridViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setWidth(sidebar.getWidth());
		column.setLabelProvider(new GridColumnLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Action action = (Action) cell.getElement();

				GridItem item = (GridItem) cell.getViewerRow().getItem();
				GridItem parentItem = item.getParentItem();

				String html = action.getText();
				html = (html == null || html.isEmpty()) ? action.getName() : html;
				if (parentItem == null) {
					item.setHeight(42);
					html = "<div style='float:left;margin-top:4px;'>" + html + "</div>";
				} else {
					item.setHeight(36);
					cell.setBackground(BruiColors.getColor(BruiColor.Grey_900));
					html = "<div style='font-size:14px;float:left;margin-top:2px;'>" + html + "</div>";
				}
				cell.setText(html);

			}

		});
		viewer.setInput(sidebar.getSidebarItems());

		viewer.getGrid().addListener(SWT.Selection, e -> {
			Action action = (Action) ((GridItem) e.item).getData();
			if ((action == null || action.getChildren() != null) && !action.getChildren().isEmpty())
				viewer.setExpandedElements(action == null ? new Object[0] : new Object[] { action });
			if (!action.isRunnable())
				return;
			run(action, e);

		});
		return viewer.getControl();
	}

	public SidebarWidget setContext(BruiAssemblyContext context) {
		this.context = context;
		return this;
	}

}
