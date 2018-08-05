package com.bizvisionsoft.bruiengine.ui;

import java.util.ArrayList;
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

import com.bizivisionsoft.widgets.tools.WidgetHandler;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.Sidebar;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.BruiService;
import com.bizvisionsoft.bruiengine.service.PermissionUtil;
import com.bizvisionsoft.bruiengine.service.UserSession;
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
			List<Action> children = PermissionUtil.getPermitActions(service.getCurrentUserInfo(),
					((Action) parentElement).getChildren(), context.getRootInput());
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

	private boolean packed;

	private View view;

	private Composite packButton;

	private Control menu;

	private Control toolbar;

	private Control header;

	public SidebarWidget(View view, Sidebar sidebar, BruiService service, BruiAssemblyContext parentContext) {
		this.view = view;
		this.sidebar = sidebar;
		this.service = service;
		parentContext.add(context = UserSession.newAssemblyContext().setParent(parentContext));
		bruiToolkit = UserSession.bruiToolkit();

		// 1. 创建实例,注入并初始化
		brui = BruiAssemblyEngine.create(sidebar.getHeader(), service, context);
	}

	public SidebarWidget createUI(Composite parent) {
		panel = new Composite(parent, SWT.NONE);
		panel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		panel.setLayout(new FormLayout());
		header = createHeader(panel);
		menu = createSidebarMenu(panel);
		toolbar = createToolbar(panel);
		layout();
		return this;
	}

	private void layout() {
		if (packed) {
			packedLayout();
		} else {
			extendLayout();
		}
	}

	private void packedLayout() {
		FormData fd;
		if (header != null) {
			fd = new FormData();
			header.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = 48;
			fd.width = 48;
		}

		if (toolbar != null) {
			fd = new FormData();
			toolbar.setLayoutData(fd);
			fd.bottom = new FormAttachment(100);
			fd.left = new FormAttachment();
			fd.width = 48;
			fd.height = 48;
		}

		fd = new FormData();
		menu.setLayoutData(fd);
		fd.width = 0;
		fd.height = 0;
	}

	private void extendLayout() {
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

	}

	public Composite getControl() {
		return panel;
	}

	private Control createToolbar(Composite parent) {
		Composite bar = new Composite(parent, SWT.NONE);
		bar.setBackgroundMode(SWT.INHERIT_DEFAULT);
		bar.setBackground(BruiColors.getColor(BruiColor.Grey_900));
		// bar.setOrientation(SWT.RIGHT_TO_LEFT);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginTop = 0;
		layout.marginLeft = 12;
		layout.marginBottom = 0;
		layout.marginRight = 4;
		layout.marginHeight = 12;
		layout.marginWidth = 0;
		layout.spacing = 12;
		bar.setLayout(layout);

		createPackSidebarToolitem(bar);

		createHomeToolitem(bar);

		createUserSettingToolitem(bar);

		sidebar.getToolbarItems().forEach(a -> {
			Label btn = new Label(bar, SWT.NONE);
			btn.setToolTipText(a.getTooltips());
			bruiToolkit.enableMarkup(btn);
			btn.setText("<img alter='packButton' src='" + service.getResourceURL(a.getImage())
					+ "' style='cursor:pointer;' width='20px' height='20px'></img>");
			btn.setLayoutData(new RowData(24, 24));
			btn.addListener(SWT.MouseDown, e -> {
				run(a, e);
			});
		});
		return bar;
	}

	private void createHomeToolitem(Composite bar) {
		final List<AssemblyLink> pageContents = PermissionUtil.listRolebasedPageContents(service.getCurrentUserInfo(),
				view.getPage(), context.getRootInput());
		if (pageContents.isEmpty()) {
			return;
		}

		final Composite btn = new Composite(bar, SWT.NONE);
		btn.setLayoutData(new RowData(24, 24));
		WidgetHandler.getHandler(btn).setHtmlContent(
				"<i class='layui-icon layui-icon-app' style='cursor:pointer;font-size:20px;color:#ffffff;'></i>");

		btn.addListener(SWT.MouseDown, e -> {
			if (pageContents.size() == 1) {
				service.switchContent(pageContents.get(0).getName(), null);
			} else {
				ActionMenu actionMenu = new ActionMenu(service);
				List<Action> actions = new ArrayList<>();
				pageContents.forEach(al -> {
					Action action = new Action();
					String id = al.getId();
					final Assembly assm = ModelLoader.site.getAssembly(id);
					action.setName(id);
					action.setText(al.getText());
					action.setImage(al.getImage());
					action.setStyle("normal");
					actions.add(action);
					actionMenu.handleActionExecute(id, a -> {
						service.switchContent(assm, null);
						return false;
					});
				});
				actionMenu.setActions(actions).open();
			}
		});
	}

	private void createUserSettingToolitem(Composite bar) {
		Composite btn = new Composite(bar, SWT.NONE);
		btn.setToolTipText("用户设置");
		btn.setLayoutData(new RowData(24, 24));
		WidgetHandler.getHandler(btn).setHtmlContent(
				"<i class='layui-icon layui-icon-user' style='cursor:pointer;font-size:20px;color:#ffffff;'></i>");

		btn.addListener(SWT.MouseDown, e -> {
		});
	}

	private void createPackSidebarToolitem(Composite bar) {
		packButton = new Composite(bar, SWT.NONE);
		packButton.setLayoutData(new RowData(24, 24));
		updatePackButtonLabel(packButton);

		packButton.addListener(SWT.MouseDown, e -> {
			packed = !packed;
			pack();
		});

	}

	private void pack() {
		updatePackButtonLabel(packButton);
		layout();
		view.packSidebar(packed);
	}

	private void updatePackButtonLabel(final Composite btn) {
		String text;
		String content;
		if (packed) {
			text = "展开侧边栏";
			content = "<i class='layui-icon layui-icon-spread-left' style='cursor:pointer;font-size:20px;color:#ffffff;'></i>";
		} else {
			text = "折叠侧边栏";
			content = "<i class='layui-icon layui-icon-shrink-right' style='cursor:pointer;font-size:20px;color:#ffffff;'></i>";
		}

		btn.setToolTipText(text);
		WidgetHandler.getHandler(btn).setHtmlContent(content);
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

		List<Action> actions = PermissionUtil.getPermitActions(service.getCurrentUserInfo(), sidebar.getSidebarItems(),
				context.getRootInput());
		viewer.setInput(actions);

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
