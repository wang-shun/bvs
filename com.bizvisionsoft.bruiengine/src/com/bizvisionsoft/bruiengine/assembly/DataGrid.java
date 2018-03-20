package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import com.bizivisionsoft.widgets.pagination.Pagination;
import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetContent;
import com.bizvisionsoft.bruicommons.annotation.Init;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.BruiEngine;
import com.bizvisionsoft.bruiengine.BruiGridDataSetEngine;
import com.bizvisionsoft.bruiengine.BruiGridRenderEngine;
import com.bizvisionsoft.bruiengine.BruiQueryEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.BruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.mongodb.BasicDBObject;

public class DataGrid {

	private static final int LIMIT = 50;

	@Inject
	private IBruiService bruiService;

	private Assembly config;

	@GetContent("viewer")
	private GridTreeViewer viewer;

	private BruiGridRenderEngine renderEngine;

	private BruiGridDataSetEngine dataSetEngine;

	private long count;

	private Integer limit;

	private Integer skip;

	private int currentPage;

	@GetContent("pagination")
	private Pagination page;

	@Inject
	private BruiAssemblyContext context;

	private int actionColWidth;

	private Composite toolbar;

	private ToolItemDescriptor itemSelector;

	private List<ToolItemDescriptor> toolitems = new ArrayList<ToolItemDescriptor>();

	private boolean checkOn;

	private boolean forceDisablePagination;

	private boolean disableDateSetEngine;

	private BasicDBObject filter;

	private boolean queryOn;

	public DataGrid(Assembly gridConfig) {
		this.config = gridConfig;
	}

	@Init
	private void init() {
		// 注册渲染器
		renderEngine = BruiGridRenderEngine.create(config, bruiService);

		// 注册数据集引擎
		if (!disableDateSetEngine)
			dataSetEngine = BruiGridDataSetEngine.create(config, bruiService);
	}

	public DataGrid addItemSelector(ToolItemDescriptor listener) {
		this.itemSelector = listener;
		return this;
	}

	public DataGrid addToolItem(ToolItemDescriptor ti) {
		toolitems.add(ti);
		return this;
	}

	public DataGrid setCheckOn(boolean checkOn) {
		this.checkOn = checkOn;
		return this;
	}

	public DataGrid setQueryOn(boolean queryOn) {
		this.queryOn = queryOn;
		return this;
	}

	/**
	 * 忽略配置，强行取消翻页
	 * 
	 * @return
	 */
	public DataGrid disablePagination() {
		forceDisablePagination = true;
		return this;
	}

	public DataGrid disableDateSetEngine() {
		disableDateSetEngine = true;
		return this;
	}

	@CreateUI
	public void createUI(Composite parent) {
		parent.setLayout(new FormLayout());
		Control queryPanel = createQueryPanel(parent);
		Control grid = createGrid(parent);
		Control pagec = createToolbar(parent);

		Label sep = null;
		if (queryPanel != null) {
			FormData fd = new FormData();
			queryPanel.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = 220;

			sep = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
			fd = new FormData();
			sep.setLayoutData(fd);
			fd.top = new FormAttachment(queryPanel);
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = 1;
		}

		FormData fd = new FormData();
		grid.setLayoutData(fd);
		fd.top = new FormAttachment(sep);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);

		if (pagec != null) {
			fd.bottom = new FormAttachment(pagec);
			fd = new FormData();
			pagec.setLayoutData(fd);
			fd.height = 48;
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
		}
		fd.bottom = new FormAttachment(100);

		setInput();
	}

	private Control createQueryPanel(Composite parent) {
		if (!queryOn) {
			return null;
		}
		Composite queryPanel = new Composite(parent, SWT.NONE);

		Assembly queryConfig = (Assembly) BruiEngine.simpleCopy(config, new Assembly());
		queryConfig.setType(Assembly.TYPE_EDITOR);
		queryConfig.setTitle("查询");

		String bundleId = config.getQueryBuilderBundle();
		String classId = config.getQueryBuilderClass();
		Object input;
		if (bundleId != null && classId != null) {
			input = BruiQueryEngine.create(bundleId, classId, bruiService, context).getTarget();
		} else {
			input = new Document();
		}

		BruiAssemblyEngine brui = BruiAssemblyEngine.newInstance(queryConfig);
		IBruiContext childContext = new BruiEditorContext().setEditable(true).setCompact(true).setInput(input)
				.setIgnoreNull(true).setParent(context).setAssembly(queryConfig).setEngine(brui);
		context.add(childContext);

		final DataEditor editor = (DataEditor) brui.getTarget();
		// 2. 设置查询
		editor.addToolItem(new ToolItemDescriptor("查询", BruiToolkit.CSS_INFO, e -> {
			try {
				doQuery((BasicDBObject) editor.save());
			} catch (Exception e1) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", e1.getMessage());
			}

		}));

		brui.init(new IServiceWithId[] { bruiService, childContext }).createUI(queryPanel);

		return queryPanel;
	}

	private Control createToolbar(Composite parent) {
		if (toolitems.isEmpty() && (!config.isGridPageControl() || forceDisablePagination)) {
			return null;
		}

		toolbar = new Composite(parent, SWT.NONE);
		toolbar.setLayout(new FormLayout());
		FormData fd = new FormData();
		new Label(toolbar, SWT.HORIZONTAL | SWT.SEPARATOR).setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);

		Control left = null;
		for (Iterator<ToolItemDescriptor> iterator = toolitems.iterator(); iterator.hasNext();) {
			ToolItemDescriptor desc = iterator.next();
			Button btn = UserSession.bruiToolkit().newStyledControl(Button.class, toolbar, SWT.PUSH, desc.style);
			btn.setText(desc.label);
			btn.addListener(SWT.Selection, desc.listener);
			fd = new FormData();
			btn.setLayoutData(fd);
			fd.top = new FormAttachment(0, 11);
			fd.left = new FormAttachment(left, 8);
			fd.width = 64;
			fd.height = 28;
			left = btn;
		}

		if (config.isGridPageControl() && !forceDisablePagination) {
			// 求出有多少记录
			createPageControl();

			fd = new FormData();
			page.setLayoutData(fd);
			fd.top = new FormAttachment(0, 1);
			fd.bottom = new FormAttachment(100);
			fd.left = new FormAttachment(left, 8);
			fd.right = new FormAttachment(100, -8);
		}
		return toolbar;
	}

	private Control createPageControl() {
		count = dataSetEngine.count(filter);
		// 获得最佳的每页记录数
		limit = LIMIT;
		// 起始
		skip = 0;
		page = new Pagination(toolbar, toolitems.isEmpty() ? SWT.LONG : SWT.MEDIUM).setCount(count).setLimit(LIMIT);
		page.addListener(SWT.Selection, e -> {
			currentPage = e.index;
			skip = (currentPage - 1) * limit;
			setInput();
		});

		return page;
	}

	private Control createGrid(Composite parent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// 创建表格
		int style = config.isGridHasBorder() ? SWT.BORDER : SWT.NONE;
		style = config.isGridHasVScroll() ? (style | SWT.V_SCROLL) : style;
		style = config.isGridHasHScroll() ? (style | SWT.H_SCROLL) : style;
		style = config.isGridMultiSelection() ? (style | SWT.MULTI) : style;

		if (checkOn)
			style |= SWT.CHECK;

		viewer = new GridTreeViewer(parent, style);
		Grid grid = viewer.getGrid();
		grid.setHeaderVisible(config.isGridHeaderVisiable());
		grid.setFooterVisible(config.isGridFooterVisiable());
		grid.setLinesVisible(config.isGridLineVisiable());
		grid.setHideIndentionImage(config.isGridHideIndentionImage());
		// 不分页时才需要考虑预加载
		if (!config.isGridPageControl())
			grid.setData(RWT.PRELOADED_ITEMS, 100);

		// grid.setAutoHeight(config.isGridAutoHeight());
		if (config.getGridCustomItemHeight() != 0)
			grid.setItemHeight(config.getGridCustomItemHeight());

		viewer.setAutoExpandLevel(config.getGridAutoExpandLevel());
		viewer.setAutoPreferredHeight(config.isGridAutoHeight());
		viewer.setUseHashlookup(true);

		if (itemSelector != null || config.isGridMarkupEnabled()
				|| (config.getActions() != null && !config.getActions().isEmpty()))
			UserSession.bruiToolkit().enableMarkup(grid);

		if (config.getGridFix() > 0)
			grid.setData(RWT.FIXED_COLUMNS, config.getGridFix());

		/////////////////////////////////////////////////////////////////////////////////////
		// 创建列
		config.getColumns().forEach(c -> {
			if (c.getColumns() == null || c.getColumns().isEmpty()) {
				createColumn(grid, c);
			} else {
				createGroup(grid, c);
			}
		});

		/////////////////////////////////////////////////////////////////////////////////////
		// 创建操作列
		//
		final List<Action> actions = config.getActions();

		// 如果作为选择器，无需创建操作
		if (itemSelector == null && actions != null && actions.size() > 0) {
			actionColWidth = BruiToolkit.actionMargin;

			for (Action action : actions) {
				String imgUrl = action.getImage();
				boolean forceText = action.isForceText();
				String text = action.getText();
				text = (text == null || text.isEmpty()) ? action.getName() : text;
				if (imgUrl == null || imgUrl.isEmpty()) {
					actionColWidth += BruiToolkit.actionTextBtnWidth;
				} else if (forceText) {
					actionColWidth += BruiToolkit.actionForceBtnTextWidth;
				} else {
					actionColWidth += BruiToolkit.actionImgBtnWidth;
				}
				actionColWidth += BruiToolkit.actionMargin;
			}

			GridColumn col = new GridColumn(grid, SWT.NONE);
			col.setWidth(actionColWidth);
			col.setMoveable(false);
			col.setResizeable(false);
			col.setData("fixedRight", true);

			GridViewerColumn vcol = new GridViewerColumn(viewer, col);
			vcol.setLabelProvider(new DataGridActionColumnLabelProvider(actions));
			grid.addListener(SWT.Selection, e -> {
				actions.stream().filter(a -> a.getId().equals(e.text)).findFirst().ifPresent(action -> {
					Object elem = e.item.getData();
					viewer.setSelection(new StructuredSelection(elem));
					invoveAction(e, action);
				});
			});
		} else if (itemSelector != null) {
			int actionColWidth = 2 * BruiToolkit.actionMargin + BruiToolkit.actionTextBtnWidth;

			GridColumn col = new GridColumn(grid, SWT.NONE);
			col.setWidth(actionColWidth);
			col.setMoveable(false);
			col.setResizeable(false);
			col.setData("fixedRight", true);

			GridViewerColumn vcol = new GridViewerColumn(viewer, col);
			Action a = new Action();
			a.setText(itemSelector.label);
			a.setId("choice");
			a.setStyle(itemSelector.style);
			vcol.setLabelProvider(new DataGridActionColumnLabelProvider(Arrays.asList(new Action[] { a })));

			grid.addListener(SWT.Selection, itemSelector.listener);
		}

		/////////////////////////////////////////////////////////////////////////////////////
		// 计算自动列宽
		if (config.isGridAutoColumnWidth()) {
			grid.getParent().addListener(SWT.Resize, e -> {
				int width = grid.getParent().getBounds().width;
				if (width == 0) {
					return;
				}
				GridColumn[] cols = grid.getColumns();
				int total = 0;
				for (int i = 0; i < cols.length; i++) {
					if (!Boolean.TRUE.equals(cols[i].getData("fixedRight")))
						total += cols[i].getWidth();
				}
				for (int i = 0; i < cols.length; i++) {
					if (!Boolean.TRUE.equals(cols[i].getData("fixedRight")))
						cols[i].setWidth(width * cols[i].getWidth() / total);
				}
			});
		}

		/////////////////////////////////////////////////////////////////////////////////////
		// 内容提供
		viewer.setContentProvider(new DataGridContentProvider());

		context.setSelectionProvider(viewer);

		return grid;
	}

	private void invoveAction(Event e, Action action) {
		if (action.getChildren() == null || action.getChildren().isEmpty()) {
			BruiActionEngine.create(action, bruiService).invokeExecute(e, context);
		} else {
			// 显示菜单
			GridItem item = ((GridItem) e.item);
			Grid grid = viewer.getGrid();
			ActionMenu menu = new ActionMenu(action.getChildren()).setContext(context).setService(bruiService);
			menu.create();
			Point size = menu.getInitialSize();
			int x = grid.getBounds().width - actionColWidth - size.x;
			int y = item.getBounds(0).y - (size.y - item.getHeight()) / 2;
			Point point = grid.toDisplay(x, y);
			menu.setLocation(point).open();
		}
	}

	private void setInput() {
		if (!disableDateSetEngine) {
			setInput((List<?>) dataSetEngine.query(skip, limit, filter));
		}
	}

	public void setInput(List<?> input) {
		renderEngine.setInput(input);
		viewer.getGrid().removeAll();
		viewer.setInput(input);
	}

	private GridColumnGroup createGroup(Grid grid, Column colConfig) {
		GridColumnGroup grp = new GridColumnGroup(grid, colConfig.getAlignment() | SWT.TOGGLE);
		if (colConfig.isMarkupEnabled()) {
			UserSession.bruiToolkit().enableMarkup(grp);
		}
		grp.setText(colConfig.getText());
		grp.setExpanded(colConfig.isExpanded());

		colConfig.getColumns().forEach(c -> {
			createColumn(grp, c);
		});
		return grp;
	}

	private GridColumn createColumn(Object parent, Column c) {

		GridColumn col;
		if (parent instanceof Grid)
			col = new GridColumn((Grid) parent, SWT.NONE);
		else
			col = new GridColumn((GridColumnGroup) parent, SWT.NONE);

		if (c.isMarkupEnabled()) {
			UserSession.bruiToolkit().enableMarkup(col);
		}

		// 列头和列脚的文本
		renderEngine.renderHeaderText(col, c);
		renderEngine.renderFoot(col, c);

		col.setAlignment(c.getAlignment());
		col.setWidth(c.getWidth());
		col.setMinimumWidth(c.getMinimumWidth());
		col.setMoveable(c.isMoveable());
		col.setResizeable(c.isResizeable());
		col.setDetail(c.isDetail());
		col.setSummary(c.isSummary());

		GridViewerColumn vcol = new GridViewerColumn(viewer, col);
		DataGridColumnLabelProvider labelProvider = new DataGridColumnLabelProvider(renderEngine, c);

		vcol.setLabelProvider(labelProvider);

		return col;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insert(Object item, int i) {
		((List) viewer.getInput()).add(0, item);
		viewer.insert(viewer.getInput(), item, 0);
	}

	public void replaceItem(Object elem, Object info) {
		update(BruiEngine.simpleCopy(info, elem));
	}

	public void update(Object elem) {
		viewer.update(elem, null);
	}

	public List<Object> getSelectedItems() {
		ArrayList<Object> result = new ArrayList<Object>();
		Arrays.asList(viewer.getGrid().getItems()).stream().filter(i -> i.getChecked())
				.forEach(c -> result.add(c.getData()));
		return result;
	}

	public void removeSelectedItem() {
		Arrays.asList(viewer.getGrid().getItems()).stream().filter(i -> i.getChecked()).forEach(c -> {
			c.dispose();
		});
	}

	public void removeAllItem() {
		viewer.getGrid().removeAll();
	}

	/**
	 * 执行查询
	 */
	public void openQueryEditor() {
		Assembly queryConfig = (Assembly) BruiEngine.simpleCopy(config, new Assembly());
		queryConfig.setType(Assembly.TYPE_EDITOR);
		queryConfig.setTitle("查询");

		String bundleId = config.getQueryBuilderBundle();
		String classId = config.getQueryBuilderClass();
		Object input;
		if (bundleId != null && classId != null) {
			input = BruiQueryEngine.create(bundleId, classId, bruiService, context).getTarget();
		} else {
			input = new Document();
		}

		Editor editor = bruiService.createEditor(queryConfig, input, true, true, context);
		if (Window.OK == editor.open()) {
			doQuery((BasicDBObject) editor.getResult());
		}
	}

	private void doQuery(BasicDBObject result) {
		filter = result;
		currentPage = 0;
		skip = 0;
		count = dataSetEngine.count(filter);
		page.setCount(count);
		setInput();

	}

}
