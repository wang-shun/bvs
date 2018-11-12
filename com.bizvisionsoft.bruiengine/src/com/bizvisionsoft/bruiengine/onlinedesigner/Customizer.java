package com.bizvisionsoft.bruiengine.onlinedesigner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.htmlparser.Htmlparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizivisionsoft.widgets.tools.WidgetHandler;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.tools.Check;
import com.bizvisionsoft.service.tools.Formatter;
import com.bizvisionsoft.service.tools.Generator;
import com.bizvisionsoft.serviceconsumer.Services;
import com.google.gson.GsonBuilder;

public class Customizer extends Dialog {

	public Logger logger = LoggerFactory.getLogger(getClass());

	public static List<Column> open(Assembly config, List<Column> stored) {
		return open(config, stored, true);
	}

	public static List<Column> open(Assembly config, List<Column> stored, boolean enableColumnGroup) {
		Customizer cust = new Customizer(Display.getCurrent().getActiveShell());
		cust.siteConfig = config;
		cust.config = (Assembly) config.clone();
		cust.configColumns = cust.config.getColumns();
		cust.storedColumns = stored == null ? ((Assembly) config.clone()).getColumns() : stored;
		cust.context = UserSession.newAssemblyContext();
		cust.enableColumnGroup = enableColumnGroup;
		return cust.open() == OK ? cust.storedColumns : null;
	}

	private static final int GRID_HEIGHT = 640;
	private static final int GRID_WIDTH = 480;
	private Assembly config;
	private Assembly siteConfig;
	private List<Column> storedColumns;
	private List<Column> configColumns;
	private GridTreeViewer left;
	private GridTreeViewer right;
	private IBruiContext context;
	private boolean enableColumnGroup;

	protected Customizer(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		String title = config.getStickerTitle();
		title = Check.isAssigned(title) ? title : config.getTitle();
		title = Check.isAssigned(title) ? title : config.getName();
		newShell.setText("设置: " + title);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.DETAILS_ID, "恢复默认", false).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_INFO);
		createButton(parent, IDialogConstants.CANCEL_ID, "关闭", false).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_WARNING);
		createButton(parent, IDialogConstants.OK_ID, "保存", true).setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_NORMAL);

		if ("su".equals(UserSession.current().getUser().getUserId())) {
			createButton(parent, IDialogConstants.CLIENT_ID, "保存到站点", false).setData(RWT.CUSTOM_VARIANT,
					BruiToolkit.CSS_SERIOUS);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 24;
		layout.marginHeight = 24;
		layout.horizontalSpacing = 24;
		layout.verticalSpacing = 12;
		panel.setLayout(layout);

		createTipsPanel(panel);
		createLeftPanel(panel);
		createRigthPanel(panel);
		return panel;
	}

	private void createTipsPanel(Composite parent) {
		String info = "提示：从左侧列表中拖拽选中项放置到右侧列表可添加显示列。从右侧列表示中拖拽选中列放置到左侧可移除显示列。<br>在右侧显示列中上下拖拽条目可改变显示顺序。在显示列中选择多项后点击{ }可新建列组。";
		Composite label = new Composite(parent, SWT.NONE);
		WidgetHandler handler = WidgetHandler.getHandler(label);
		handler.setHtmlContent("<blockquote class='layui-elem-quote'>" + info + "</blockquote>");
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		data.heightHint = 72;
		label.setLayoutData(data);
	}

	private void createRigthPanel(Composite parent) {
		right = createViewer(parent, "显示列", GRID_WIDTH + 38);
		GridViewerColumn

		col = new GridViewerColumn(right, SWT.LEFT);
		UserSession.bruiToolkit().enableMarkup(col.getColumn());
		if (enableColumnGroup) {
			col.getColumn()
					.setText("<div style='color:#808080;cursor:pointer;font-size:14px;font-weight:bolder;'>{  }</div>");
			col.getColumn().addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					doAddGroup();
				}
			});
		}
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "<a class='layui-icon layui-icon-edit' style='color:#808080;font-size:20px;' href='edit' target='_rwt'></a>";
			}
		});
		col.getColumn().setWidth(38);
		col.getColumn().setResizeable(false);

		right.setInput(storedColumns);

		Grid control = right.getGrid();

		DragSource dragSource = new DragSource(control, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragRightListener());

		DropTarget dropTarget = new DropTarget(control, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropRightListener());

		right.refresh();

		right.getGrid().addListener(SWT.Selection, e -> {
			if ("edit".equals(e.text)) {
				edit((GridItem) e.item);
			}
		});
	}

	private void edit(GridItem item) {
		Column col = (Column) item.getData();
		String editor;
		if (item.getItemCount() > 0) {
			editor = "列组编辑器";
		} else if (item.getParentItem() == null) {
			editor = "列编辑器(非列组子列)";
		} else {
			editor = "列编辑器";
		}
		Editor.open(editor, context, col.clone(), (r, t) -> {
			AUtil.simpleCopy(t, col);
			right.refresh(col, true);
		});
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.DETAILS_ID) {
			resetToDefault();
		} else if (buttonId == IDialogConstants.CLIENT_ID) {
			saveToSite();
			setReturnCode(OK);
			close();
		}
		super.buttonPressed(buttonId);
	}

	private void saveToSite() {
		boolean ok = MessageDialog.openQuestion(getShell(), "保存到站点", "保存到站点将影响所有用户，请确认将当前的修改保存到站点。");
		if (ok) {
			siteConfig.setColumns(storedColumns);
			Services.get(SystemService.class).deleteClientSetting(ModelLoader.site.getName(), "assembly@" + siteConfig.getName());
			try {
				ModelLoader.saveSite();
			} catch (IOException e) {
				logger.error("保存站点配置出错", e);
				Layer.message(e.getMessage(), Layer.ICON_CANCEL);
			}
		}
	}

	private void resetToDefault() {
		storedColumns = ((Assembly) config.clone()).getColumns();
		right.setInput(storedColumns);
	}

	@Override
	protected void okPressed() {
		UserSession.current().saveClientSetting("assembly@" + config.getName(),
				new GsonBuilder().create().toJson(storedColumns));
		super.okPressed();
	}

	private void createLeftPanel(Composite parent) {
		left = createViewer(parent, "可用列", GRID_WIDTH);
		left.setInput(getColumnNodes(configColumns));
		Grid control = left.getGrid();

		DragSource dragSource = new DragSource(control, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragLeftListener());

		DropTarget dropTarget = new DropTarget(control, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropLeftListener());
	}

	private List<Column> getColumnNodes(List<Column> columns) {
		List<Column> result = new ArrayList<>();
		columns.forEach(c -> {
			List<Column> cs = c.getColumns();
			if (Check.isNotAssigned(cs)) {
				result.add(c);
			} else {
				result.addAll(getColumnNodes(cs));
			}
		});
		return result;
	}

	private GridTreeViewer createViewer(Composite parent, String title, int width) {
		GridTreeViewer viewer = new GridTreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI);
		// viewer.getTree().setLinesVisible(true);
		viewer.setAutoExpandLevel(GridTreeViewer.ALL_LEVELS);
		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setLayoutData(new GridData(width, GRID_HEIGHT));
		UserSession.bruiToolkit().enableMarkup(viewer.getGrid());

		GridViewerColumn col = new GridViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(title);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String text;
				try {
					text = Htmlparser.parser(Formatter.getString(((Column) element).getText()));
				} catch (Exception e) {
					text = "";
				}
				if (Check.isAssigned(((Column) element).getColumns())) {
					return text;
				} else {
					return text;
				}
			}
		});
		col.getColumn().setWidth(GRID_WIDTH / 2);
		col.getColumn().setResizeable(true);

		col = new GridViewerColumn(viewer, SWT.LEFT);
		col.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				if (Check.isAssigned(((Column) element).getColumns())) {
					return "";
				}
				return Formatter.getString(((Column) element).getName());
			}
		});
		col.getColumn().setWidth(GRID_WIDTH / 2);
		col.getColumn().setResizeable(true);

		viewer.setContentProvider(new ColumnsContentProvider());
		return viewer;
	}

	private void doAddGroup() {
		List<Column> selection = right.getStructuredSelection().toList(new ArrayList<Column>());
		if (Check.isNotAssigned(selection)) {
			Layer.message("请选择合并为列组的列", Layer.ICON_CANCEL);
			return;
		}

		if (selection.stream().anyMatch(c -> Check.isAssigned(c.getColumns()))) {
			Layer.message("不能将列组合并到新的列组", Layer.ICON_CANCEL);
			return;
		}

		Column column = new Column();
		column.setId(Generator.generateId());
		String name = Generator.generateName("新列组");
		column.setName(name);
		column.setText(name);
		column.setAlignment(SWT.LEFT);
		column.setResizeable(true);
		column.setWidth(80);
		column.setColumns(selection);
		column.setExpanded(true);

		selection.forEach(c -> {
			remove(c);
			c.setSummary(true);
			c.setDetail(true);
		});

		storedColumns.add(column);
		right.refresh();
		right.expandAll();
		right.setSelection(new StructuredSelection(column), true);
	}

	private void removeIn(List<Column> columns, Column c) {
		if (columns != null && !columns.remove(c)) {
			columns.forEach(p -> removeIn(p.getColumns(), c));
		}
	}

	private void remove(Column c) {
		removeIn(storedColumns, c);
	}

	class DropRightListener extends DropTargetAdapter {
		@Override
		public void drop(DropTargetEvent event) {
			Column selected;
			if ("left".equals(event.data)) {
				selected = (Column) left.getStructuredSelection().getFirstElement();
			} else {
				selected = (Column) right.getStructuredSelection().getFirstElement();
			}

			GridItem item = (GridItem) event.item;
			if (item == null)
				return;
			GridItem parent = item.getParentItem();
			if ("right".equals(event.data)) {
				// 如果选择列组，不能放置在其他列组下
				if (Check.isAssigned(selected.getColumns()) && parent != null) {
					Layer.message("不能将列组移动到其他列组下级", Layer.ICON_CANCEL);
					return;
				}
			}

			Column targetCol = (Column) item.getData();
			if (parent == null) {
				int index = storedColumns.indexOf(targetCol);
				storedColumns.add(index, (Column) selected.clone());
			} else {
				Column parentColumn = (Column) parent.getData();
				List<Column> columns = parentColumn.getColumns();
				if (columns != null) {
					int index = columns.indexOf(targetCol);
					columns.add(index, (Column) selected.clone());
				} else {
					columns = new ArrayList<>();
					parentColumn.setColumns(columns);
					columns.add(0, (Column) selected.clone());
				}
			}
			if ("right".equals(event.data))
				remove(selected);

			right.refresh();
			right.expandAll();
		}

		@Override
		public void dragOver(DropTargetEvent event) {
			event.feedback = DND.FEEDBACK_INSERT_BEFORE | DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
		}

	}

	class DropLeftListener extends DropTargetAdapter {

		@Override
		public void drop(DropTargetEvent event) {
			right.getStructuredSelection().toList(new ArrayList<Column>()).forEach(c -> removeIn(storedColumns, c));
			right.refresh();
		}

	}

	class DragRightListener extends DragSourceAdapter {

		@Override
		public void dragSetData(DragSourceEvent event) {
			event.data = "right";
		}

	}

	class DragLeftListener extends DragSourceAdapter {

		@Override
		public void dragSetData(DragSourceEvent event) {
			event.data = "left";
		}
	}

	class ColumnsContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			return ((List<Column>) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Column) {
				List<Column> children = ((Column) parentElement).getColumns();
				if (children != null)
					return children.toArray(new Column[0]);
			}
			return new Object[0];
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

	public boolean contains(List<Column> list, Column c) {
		if (list != null && list.contains(c)) {
			return true;
		}
		for (int i = 0; i < list.size(); i++) {
			if (contains(list.get(i).getColumns(), c)) {
				return true;
			}
		}
		return false;
	}

}
