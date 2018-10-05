package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.service.tools.Check;
import com.bizvisionsoft.service.tools.Formatter;
import com.bizvisionsoft.service.tools.Generator;

public class Customizer extends Dialog {

	public static List<Column> open(Assembly config, List<Column> stored) {
		Customizer cust = new Customizer(Display.getCurrent().getActiveShell());
		cust.config = (Assembly) config.clone();
		cust.configColumns = cust.config.getColumns();
		cust.storedColumns = stored == null ? ((Assembly) config.clone()).getColumns() : stored;

		cust.open();
		Layer.message("添加显示列：从可用列中拖拽到显示列<br>移除显示列：从显示列中拖拽到可用列<br>改变列顺序：在显示列中上下拖拽条目<br>新建列分组：在显示列中选择后点击{ }");
		return null;
	}

	private static final int HEIGHT = 640;
	private static final int WIDTH = 480;
	private Assembly config;
	private List<Column> storedColumns;
	private List<Column> configColumns;
	private GridTreeViewer left;
	private GridTreeViewer right;

	protected Customizer(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(false);
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
		if ("su".equals(UserSession.current().getUser().getUserId())) {
			createButton(parent, IDialogConstants.CLIENT_ID, "保存到站点", false).setData(RWT.CUSTOM_VARIANT,
					BruiToolkit.CSS_SERIOUS);
		}

		createButton(parent, IDialogConstants.DETAILS_ID, "恢复默认", false).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_INFO);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false).setData(RWT.CUSTOM_VARIANT,
				BruiToolkit.CSS_WARNING);
		createButton(parent, IDialogConstants.OK_ID, "确定", true).setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_NORMAL);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 24;
		layout.marginHeight = 24;
		layout.horizontalSpacing = 24;
		panel.setLayout(layout);

		createLeftPanel(panel);
		createRigthPanel(panel);

		return panel;
	}

	private void createRigthPanel(Composite parent) {
		right = createViewer(parent, "显示列", WIDTH + 38);
		GridViewerColumn

		col = new GridViewerColumn(right, SWT.LEFT);
		UserSession.bruiToolkit().enableMarkup(col.getColumn());
		col.getColumn()
				.setText("<div style='color:#808080;cursor:pointer;font-size:14px;font-weight:bolder;'>{  }</div>");
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return "<a class='layui-icon layui-icon-edit' style='color:#808080;font-size:20px;' href='edit' target='_rwt'></a>";
			}
		});
		col.getColumn().setWidth(38);
		col.getColumn().setResizeable(false);
		col.getColumn().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAddGroup();
			}
		});

		right.setInput(storedColumns);

		Grid control = right.getGrid();

		DragSource dragSource = new DragSource(control, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragRightListener());

		DropTarget dropTarget = new DropTarget(control, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropRightListener());

		right.refresh();
	}

	private void createLeftPanel(Composite parent) {
		left = createViewer(parent, "可用列", WIDTH);
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
		viewer.getGrid().setLayoutData(new GridData(width, HEIGHT));
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
		col.getColumn().setWidth(WIDTH / 2);
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
		col.getColumn().setWidth(WIDTH / 2);
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
