package com.bizvisionsoft.bruiengine.assembly;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bson.Document;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizivisionsoft.widgets.pagination.Pagination;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.BruiEventEngine;
import com.bizvisionsoft.bruiengine.BruiGridRenderEngine;
import com.bizvisionsoft.bruiengine.BruiQueryEngine;
import com.bizvisionsoft.bruiengine.assembly.exporter.GridPartExcelExporter;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.service.tools.Check;
import com.mongodb.BasicDBObject;

public class GridPart implements IStructuredDataPart, IQueryEnable, IExportable, IClientCustomizable {

	public Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private IBruiService bruiService;

	protected Assembly config;

	@GetContent("viewer")
	protected GridTreeViewer viewer;

	protected BruiGridRenderEngine renderEngine;

	protected BruiDataSetEngine dataSetEngine;

	protected long count;

	protected Integer limit;

	protected Integer skip;

	protected int currentPage;

	@GetContent("pagination")
	protected Pagination page;

	@Inject
	private BruiAssemblyContext context;

	private Composite toolbar;

	protected ToolItemDescriptor itemSelector;

	private List<ToolItemDescriptor> toolitems = new ArrayList<ToolItemDescriptor>();

	private boolean checkOn;

	private boolean forceDisablePagination;

	private boolean disableDateSetEngine;

	private BasicDBObject filter;

	private boolean queryOn;

	private Column sortColumn;

	private int sortSequance;

	private int actionColWidth;

	private boolean vertialQueryPanel;

	protected boolean asEditorField;

	private BruiEventEngine eventEngine;

	private Composite parent;

	public GridPart() {
	}

	public GridPart(Assembly gridConfig) {
		setConfig(gridConfig);
	}

	protected void setConfig(Assembly config) {
		this.config = config;
	}

	protected void setBruiService(IBruiService bruiService) {
		this.bruiService = bruiService;
	}

	protected void setContext(BruiAssemblyContext context) {
		this.context = context;
	}

	@Init
	protected void init() {
		if (config.isGridPageControl()) {
			int _limit = config.getGridPageCount();
			if (_limit == 0) {
				limit = 30;
			} else {
				limit = _limit;
			}
		} else {
			limit = null;
		}

		// 注册渲染器
		renderEngine = BruiGridRenderEngine.create(config, bruiService, context);

		eventEngine = BruiEventEngine.create(config, bruiService, context);

		// 注册数据集引擎
		if (!disableDateSetEngine)
			dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);

		if (dataSetEngine == null)
			disableDateSetEngine();

	}

	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}

	public GridPart addItemSelector(ToolItemDescriptor listener) {
		this.itemSelector = listener;
		return this;
	}

	public GridPart setAsEditorField(boolean asEditorField) {
		this.asEditorField = asEditorField;
		return this;
	}

	public GridPart addToolItem(ToolItemDescriptor ti) {
		toolitems.add(ti);
		return this;
	}

	public GridPart setCheckOn(boolean checkOn) {
		this.checkOn = checkOn;
		return this;
	}

	public GridPart setQueryOn(boolean queryOn) {
		this.queryOn = queryOn;
		return this;
	}

	/**
	 * 忽略配置，强行取消翻页
	 * 
	 * @return
	 */
	public GridPart disablePagination() {
		forceDisablePagination = true;
		return this;
	}

	public GridPart disableDateSetEngine() {
		disableDateSetEngine = true;
		return this;
	}

	public GridPart setVertialQueryPanel(boolean vertialQueryPanel) {
		this.vertialQueryPanel = vertialQueryPanel;
		return this;
	}

	@CreateUI
	public void createUI(Composite parent) {
		this.parent = parent;

		// 如果初始化时不加载数据，queryOn必须打开
		if (config.isDisableInitLoadData()) {
			queryOn = true;
		}

		Composite panel;
		if (config.isHasTitlebar() && itemSelector == null && !asEditorField) {
			panel = createSticker(parent);
		} else {
			panel = parent;
		}
		panel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		panel.setLayout(new FormLayout());
		Control queryPanel = createQueryPanel(panel);
		Control grid = createGridControl(panel);
		Control pagec = createToolbar(panel);

		if (vertialQueryPanel) {
			layoutVertiacal(panel, queryPanel, grid, pagec);
		} else {
			layoutHorizontal(panel, queryPanel, grid, pagec);
		}

		if (!config.isDisableInitLoadData()) {
			setViewerInput();
		} else {
			setViewerInput(new ArrayList<>());
		}

		renderEngine.uiCreated();

		if (dataSetEngine != null) {
			dataSetEngine.attachListener((eventCode, m) -> {
				addEventListener(eventCode, e1 -> {
					try {
						m.invoke(dataSetEngine.getTarget(), e1);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
						e2.printStackTrace();
					}
				});
			});
		}

		// 处理客户端事件侦听
		if (eventEngine != null) {
			eventEngine.attachListener((eventCode, m) -> {
				addEventListener(eventCode, e1 -> {
					try {
						m.invoke(eventEngine.getTarget(), e1);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
						e2.printStackTrace();
					}
				});
			});
		}
	}

	private void addEventListener(String eventCode, Listener listener) {
		int eventType;
		if (eventCode.toLowerCase().equals("MouseDoubleClick".toLowerCase())) {
			eventType = SWT.MouseDoubleClick;
		} else if (eventCode.toLowerCase().equals("Selection".toLowerCase())) {
			eventType = SWT.Selection;
		} else {
			return;
		}
		viewer.getGrid().addListener(eventType, listener);

	}

	protected void layoutVertiacal(Composite panel, Control queryPanel, Control grid, Control pagec) {
		Label sep = null;
		if (queryPanel != null) {
			FormData fd = new FormData();
			queryPanel.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(100);
			fd.height = 256;

			sep = new Label(panel, SWT.HORIZONTAL | SWT.SEPARATOR);
			fd = new FormData();
			sep.setLayoutData(fd);
			fd.top = new FormAttachment(queryPanel);
			fd.left = new FormAttachment(0);
			fd.right = new FormAttachment(100);
			fd.height = 1;
		}

		FormData fd = new FormData();
		grid.setLayoutData(fd);
		fd.top = new FormAttachment(sep);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);

		if (pagec != null) {
			fd.bottom = new FormAttachment(pagec);
			fd = new FormData();
			pagec.setLayoutData(fd);
			fd.height = 48;
			fd.left = new FormAttachment(0);
			fd.right = new FormAttachment(100);
		}
		fd.bottom = new FormAttachment(100);
	}

	protected void layoutHorizontal(Composite panel, Control queryPanel, Control grid, Control pagec) {
		Label sep = null;
		if (queryPanel != null) {
			FormData fd = new FormData();
			queryPanel.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
			fd.right = new FormAttachment(30);
			fd.bottom = new FormAttachment(100);

			sep = new Label(panel, SWT.VERTICAL | SWT.SEPARATOR);
			fd = new FormData();
			sep.setLayoutData(fd);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment(queryPanel);
			fd.bottom = new FormAttachment(100);
			fd.width = 1;
		}

		FormData fd = new FormData();
		grid.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(sep);
		fd.right = new FormAttachment(100);

		if (pagec != null) {
			fd.bottom = new FormAttachment(pagec);
			fd = new FormData();
			pagec.setLayoutData(fd);
			fd.height = 48;
			fd.left = new FormAttachment(sep);
			fd.right = new FormAttachment(100);
		}
		fd.bottom = new FormAttachment(100);
	}

	protected Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);
		sticker.context = context;
		sticker.service = bruiService;

		sticker.createDefaultActions(this);

		sticker.createUI(parent);
		return sticker.content;
	}

	protected Control createQueryPanel(Composite parent) {
		if (!queryOn) {
			return null;
		}
		Composite queryPanel = new Composite(parent, SWT.NONE);

		Assembly queryConfig = (Assembly) AUtil.simpleCopy(config, new Assembly());
		queryConfig.setType(Assembly.TYPE_EDITOR);
		queryConfig.setTitle("查询");

		String bundleId = config.getQueryBuilderBundle();
		String classId = config.getQueryBuilderClass();
		Object input;
		if (Check.isAssigned(bundleId, classId)) {
			input = BruiQueryEngine.create(bundleId, classId, bruiService, context).getTarget();
		} else {
			input = new Document();
		}

		BruiAssemblyEngine brui = BruiAssemblyEngine.newInstance(queryConfig);
		IBruiContext childContext = UserSession.newEditorContext().setEditable(true).setEmbeded(true).setInput(input)
				.setParent(context).setAssembly(queryConfig).setEngine(brui);
		context.add(childContext);

		final EditorPart editor = (EditorPart) brui.getTarget();
		// 2. 设置查询
		editor.addToolItem(new ToolItemDescriptor("查询", BruiToolkit.CSS_INFO, e -> {
			try {
				doQuery((BasicDBObject) editor.save());
			} catch (Exception e1) {
				bruiService.error("错误", e1.getMessage());
			}

		}));

		brui.init(new IServiceWithId[] { bruiService, childContext }).createUI(queryPanel);

		return queryPanel;
	}

	protected Control createToolbar(Composite parent) {
		if (toolitems.isEmpty() && !pageEnabled()) {
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

		if (pageEnabled()) {
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

	private boolean pageEnabled() {
		return config.isGridPageControl() && !forceDisablePagination;
	}

	private Control createPageControl() {
		count = dataSetEngine.count(filter, context);
		// 获得最佳的每页记录数
		skip = 0;
		page = new Pagination(toolbar, toolitems.isEmpty() ? SWT.LONG : SWT.MEDIUM).setCount(count).setLimit(limit);
		page.addListener(SWT.Selection, e -> {
			currentPage = e.index;
			skip = (currentPage - 1) * limit;
			setViewerInput();
		});

		return page;
	}

	protected Grid createGridControl(Composite parent) {
		viewer = createGridViewer(parent);
		Grid grid = viewer.getGrid();

		/////////////////////////////////////////////////////////////////////////////////////
		// 创建列
		createColumns(grid);

		/////////////////////////////////////////////////////////////////////////////////////
		// 创建操作列
		//
		final List<Action> actions = config.getRowActions();
		// 如果作为选择器，无需创建操作
		if (itemSelector == null && actions != null && actions.size() > 0 && !asEditorField) {
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
			vcol.setLabelProvider(new GridPartActionColumnLabelProvider(config, actions, context));
			grid.addListener(SWT.Selection, e -> {
				actions.stream().filter(a -> a.getId().equals(e.text)).findFirst().ifPresent(action -> {
					Object elem = e.item.getData();
					viewer.setSelection(new StructuredSelection(elem));
					invoveAction(e, elem, action);
				});
			});
		} else if (itemSelector != null && !asEditorField) {
			actionColWidth = 2 * BruiToolkit.actionMargin + BruiToolkit.actionTextBtnWidth;

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
			vcol.setLabelProvider(
					new GridPartActionColumnLabelProvider(config, Arrays.asList(new Action[] { a }), context)
							.setEnablement(itemSelector.enablement));

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
				width -= actionColWidth;
				GridColumn[] cols = grid.getColumns();
				int total = 0;
				for (int i = 0; i < cols.length; i++) {
					if (!Boolean.TRUE.equals(cols[i].getData("fixedRight")))
						total += cols[i].getWidth();
				}
				for (int i = 0; i < cols.length; i++) {
					if (total != 0 && !Boolean.TRUE.equals(cols[i].getData("fixedRight"))) {
						cols[i].setWidth(width * cols[i].getWidth() / total);
					}
				}
			});
		}

		/////////////////////////////////////////////////////////////////////////////////////
		// 内容提供
		viewer.setContentProvider(new GridPartContentProvider(config));

		// 准备排序
		if (sortColumn != null) {
			viewer.setSorter(new ViewerSorter() {
				@Override
				public int compare(Viewer viewer, Object e1, Object e2) {
					return sortSequance * renderEngine.compare(sortColumn, e1, e2);
				}
			});
		}

		context.setSelectionProvider(viewer);

		return grid;
	}

	protected GridTreeViewer createGridViewer(Composite parent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// 创建表格
		int style = config.isGridHasBorder() ? SWT.BORDER : SWT.NONE;
		style = config.isGridHasVScroll() ? (style | SWT.V_SCROLL) : style;
		style = config.isGridHasHScroll() ? (style | SWT.H_SCROLL) : style;
		style = config.isGridMultiSelection() ? (style | SWT.MULTI) : style;

		if (checkOn)
			style |= SWT.CHECK;

		GridTreeViewer viewer = new GridTreeViewer(parent, style);
		Grid grid = viewer.getGrid();
		grid.setHeaderVisible(!asEditorField && config.isGridHeaderVisiable());
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
		viewer.setUseHashlookup(false);

		if (itemSelector != null || config.isGridMarkupEnabled()
				|| (config.getRowActions() != null && !config.getRowActions().isEmpty()))
			UserSession.bruiToolkit().enableMarkup(grid);

		if (config.getGridFix() > 0)
			grid.setData(RWT.FIXED_COLUMNS, config.getGridFix());

		return viewer;
	}

	protected void createColumns(Grid grid) {
		Optional.ofNullable(getStore()).orElse(config.getColumns()).forEach(c -> {
			if (Check.isAssigned(c.getColumns())) {
				createGroup(grid, c);
			} else {
				createColumn(grid, c);
			}
		});
	}

	private void invoveAction(Event e, Object elem, Action action) {
		if (action.getChildren() == null || action.getChildren().isEmpty()) {
			BruiActionEngine.execute(action, e, context, bruiService);
		} else {
			// 显示菜单
			new ActionMenu(bruiService).setAssembly(config).setInput(elem).setContext(context)
					.setActions(action.getChildren()).setEvent(e).open();
		}
	}

	public void setViewerInput() {
		if (!disableDateSetEngine) {
			try {
				setViewerInput((List<?>) dataSetEngine.query(skip, limit, filter, context));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				Layer.message(e.getMessage(), Layer.ICON_CANCEL);
			}
		}
	}

	public void setViewerInput(List<?> input) {
		renderEngine.setInput(input);
		viewer.getGrid().removeAll();
		viewer.setInput(input);
	}

	private GridColumnGroup createGroup(Grid grid, Column cc) {
		int style = cc.isNoToggleGridColumnGroup() ? cc.getAlignment() : (cc.getAlignment() | SWT.TOGGLE);
		GridColumnGroup grp = new GridColumnGroup(grid, style);
		if (cc.isMarkupEnabled()) {
			UserSession.bruiToolkit().enableMarkup(grp);
		}

		grp.setData("name", cc.getName());
		grp.setText(cc.getText());
		grp.setExpanded(cc.isExpanded());

		cc.getColumns().forEach(c -> {
			createColumn(grp, c);
		});
		return grp;
	}

	protected GridViewerColumn createColumn(Object parent, Column c) {
		return createColumn(parent, c, -1);
	}

	protected GridViewerColumn createColumn(Object parent, Column c, int index) {

		GridColumn col;
		if (parent instanceof Grid)
			col = new GridColumn((Grid) parent, SWT.NONE, index);
		else
			col = new GridColumn((GridColumnGroup) parent, SWT.NONE);

		if (c.isMarkupEnabled()) {
			UserSession.bruiToolkit().enableMarkup(col);
		}
		col.setData("name", c.getName());

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
		col.setHeaderTooltip(c.getTooltipText());

		GridViewerColumn vcol = new GridViewerColumn(viewer, col);
		GridPartColumnLabelProvider labelProvider = new GridPartColumnLabelProvider(renderEngine, c);
		vcol.setLabelProvider(labelProvider);

		if (c.getSort() != 0) {
			this.sortColumn = c;
			this.sortSequance = c.getSort();
		}

		renderEngine.handleColumn(vcol);
		return vcol;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insert(Object item, int i) {
		((List) viewer.getInput()).add(i, item);
		viewer.insert(viewer.getInput(), item, i);
	}

	@Override
	public void add(Object parent, Object item) {
		viewer.add(parent, item);
		viewer.refresh(parent);
		viewer.expandToLevel(parent, 1);
	}

	public void refresh(Object parent) {
		viewer.refresh(parent);
	}

	public void refreshAndExpand(Object parent) {
		viewer.refresh(parent);
		if (!Arrays.asList(viewer.getExpandedElements()).contains(parent)) {
			viewer.expandToLevel(parent, 1);
		}
	}

	@Override
	public void refreshAll() {
		viewer.refresh();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insert(Object item) {
		((List) viewer.getInput()).add(item);
		viewer.refresh();
	}

	public void replaceItem(Object oldElement, Object newElement) {
		update(AUtil.simpleCopy(newElement, oldElement));
	}

	public void update(Object elem) {
		viewer.update(elem, null);
	}

	public void remove(Object elem) {
		remove(null, elem);
	}

	@SuppressWarnings("rawtypes")
	public void remove(Object parent, Object elem) {
		if (parent == null) {
			((List) viewer.getInput()).remove(elem);
			viewer.remove(elem);
		} else {
			viewer.remove(parent, new Object[] { elem });
		}
	}

	public List<Object> getCheckedItems() {
		ArrayList<Object> result = new ArrayList<Object>();
		Arrays.asList(viewer.getGrid().getItems()).stream().filter(i -> i.getChecked())
				.forEach(c -> result.add(c.getData()));
		return result;
	}

	public void removeCheckedItem() {
		Arrays.asList(viewer.getGrid().getItems()).stream().filter(i -> i.getChecked()).forEach(c -> c.dispose());
	}

	public void removeAllItem() {
		viewer.getGrid().removeAll();
	}

	public void setCheckAll(boolean b) {
		Arrays.asList(viewer.getGrid().getItems()).stream().forEach(i -> i.setChecked(b));
	}

	public List<?> getViewerInput() {
		return (List<?>) viewer.getInput();
	}

	/**
	 * 
	 * @param em
	 * @param o
	 */
	public void doModify(Object element, Object newElement, BasicDBObject newData) {
		if (dataSetEngine != null) {
			try {
				dataSetEngine.replace(element, newData, context);
				replaceItem(element, newElement);
			} catch (Exception e) {
				bruiService.error("更新", e.getMessage());
			}
		}
	}

	public void doDelete(Object element) {
		if (dataSetEngine != null) {
			try {
				Object parentElement = getParentElement(element);
				dataSetEngine.delete(element, parentElement, context);
				remove(parentElement, element);
				String label = AUtil.readLabel(element);
				Layer.message(Optional.ofNullable(label).map(m -> "已删除 " + m).orElse("已删除"));
				if (parentElement != null) {
					refresh(parentElement);
				}
			} catch (Exception e) {
				bruiService.error("删除", e.getMessage());
			}
		}
	}

	public Object getParentElement(Object element) {
		Object parentData = Optional.ofNullable((GridItem) viewer.testFindItem(element)).map(i -> i.getParentItem())
				.map(p -> p.getData()).orElse(null);
		return parentData;
	}

	public void doCreate(Object parent, Object element) {
		if (dataSetEngine != null) {
			Object newElement = dataSetEngine.insert(parent, element, context);
			insert(newElement);
			viewer.setSelection(new StructuredSelection(newElement), true);
		}
	}

	public void doCreateSubItem(Object parent, Object element) {
		if (dataSetEngine != null) {
			dataSetEngine.insert(parent, element, context);
			refresh(parent);
			if (!viewer.getExpandedState(parent)) {
				viewer.expandToLevel(parent, 1);
			}
		}
	}

	public Object doGetEditInput(Object em) {
		if (dataSetEngine != null) {
			return dataSetEngine.query(em, context);
		}
		return null;
	}

	public GridTreeViewer getViewer() {
		return viewer;
	}

	@Override
	public IBruiContext getContext() {
		return context;
	}

	@Override
	public Assembly getConfig() {
		return config;
	}

	@Override
	public IBruiService getBruiService() {
		return bruiService;
	}

	@Override
	public void setCount(long count) {
		this.count = count;
		if (page != null)
			page.setCount(count);
	}

	@Override
	public BasicDBObject getFilter() {
		return filter;
	}

	@Override
	public void setSkip(int skip) {
		this.skip = skip;
	}

	@Override
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public void setFilter(BasicDBObject filter) {
		this.filter = filter;
	}

	public void expand(int i) {
		viewer.expandToLevel(i);
	}

	public void collapse() {
		viewer.collapseAll();
	}

	@Override
	public void export() {
		// 获取导出文件名。使用StickerTitle作为文件名。
		String fileName = Optional.ofNullable(config).map(c -> Stream.of(c.getStickerTitle(), c.getTitle(), c.getName())
				.filter(Check::isAssigned).findFirst().orElse("")).orElse("");

		// 不分页时，直接导出表格数据
		if (config == null || !config.isGridPageControl()) {
			exportExcel(fileName, viewer.getInput());
			return;
		}

		// 构建弹出menu，选择是全部导出还是导出当前结果
		Action current = new Action();
		current.setName("current");
		current.setText("导出本页");
		current.setStyle("normal");

		Action all = new Action();
		all.setName("all");
		all.setText("导出所有页");
		all.setStyle("normal");

		new ActionMenu(bruiService).setActions(Arrays.asList(current, all)).handleActionExecute("current", a -> {
			// 导出所有时，传入viewer和dataSetEngine获取的值
			exportExcel(fileName, viewer.getInput());
			return false;
		}).handleActionExecute("all", a -> {
			// 导出当前结果时，只用传入viewer的input
			exportExcel(fileName, dataSetEngine.query(null, null, filter, context));
			return false;
		}).open();

	}

	protected void exportExcel(String fileName, Object input) {
		exportExcel(fileName, viewer, input);
	}

	protected void exportExcel(String fileName, GridTreeViewer viewer, Object input) {
		try {
			new GridPartExcelExporter().setViewer(viewer).setInput(input).setFileName(fileName).export();
		} catch (Exception e) {
			logger.error("导出Grid： " + fileName + " 的Excel数据时出错。" + e.getMessage());
			bruiService.error("错误", "导出Grid： " + fileName + " 的Excel数据时出错。");
		}
	}

	@Override
	public void customized(List<Column> result) {
		Arrays.asList(parent.getChildren()).forEach(c -> c.dispose());
		createUI(parent);
		parent.layout();
	}

	private String exportActionText;

	@Override
	public void setExportActionText(String exportActionText) {
		this.exportActionText = exportActionText;
	}

	@Override
	public String getExportActionText() {
		return Optional.ofNullable(exportActionText)
				.orElse(Optional.ofNullable(config).map(c -> Stream.of(c.getStickerTitle(), c.getTitle(), c.getName())
						.filter(Check::isAssigned).findFirst().orElse("")).orElse(""));
	}

}
