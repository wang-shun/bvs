package com.bizvisionsoft.pms.cbs.assembly;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.BruiGridDataSetEngine;
import com.bizvisionsoft.bruiengine.BruiGridRenderEngine;
import com.bizvisionsoft.bruiengine.assembly.GridPartActionColumnLabelProvider;
import com.bizvisionsoft.bruiengine.assembly.GridPartColumnLabelProvider;
import com.bizvisionsoft.bruiengine.assembly.GridPartContentProvider;
import com.bizvisionsoft.bruiengine.assembly.IStructuredDataPart;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;
import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.model.ICBSScope;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class BudgetCBS implements IStructuredDataPart {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	private GridTreeViewer viewer;

	private Assembly config;

	private BruiGridRenderEngine renderEngine;

	private BruiGridDataSetEngine dataSetEngine;

	private ICBSScope input;

	@Init
	private void init() {
		config = context.getAssembly();
		input = (ICBSScope) context.getRootInput();
		renderEngine = BruiGridRenderEngine.create(config, bruiService);

		Object[] service = Services.getService(CBSService.class.getName());
		dataSetEngine = new BruiGridDataSetEngine((Class<?>) service[0], service[1]).setAssembly(config).newInstance();
		dataSetEngine.init(new IServiceWithId[] { context, bruiService });
	}

	@CreateUI
	public void createUI(Composite parent) {
		parent.setLayout(new FillLayout());

		createGrid(parent);

		setViewerInput();
	}

	private void setViewerInput() {
		setViewerInput((List<?>) dataSetEngine.query(null, null, null, context));
	}

	public void setViewerInput(List<?> input) {
		renderEngine.setInput(input);
		viewer.getGrid().removeAll();
		viewer.setInput(input);
	}

	private Control createGrid(Composite parent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// 创建表格

		viewer = new GridTreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		Grid grid = viewer.getGrid();
		grid.setHeaderVisible(true);
		grid.setFooterVisible(false);
		grid.setLinesVisible(true);

		viewer.setAutoExpandLevel(3);
		viewer.setUseHashlookup(false);
		UserSession.bruiToolkit().enableMarkup(grid);

		grid.setData(RWT.FIXED_COLUMNS, 3);

		/////////////////////////////////////////////////////////////////////////////////////
		// 创建列
		Column c = new Column();
		c.setName("id");
		c.setText("编号");
		c.setWidth(80);
		c.setAlignment(SWT.LEFT);
		c.setMoveable(false);
		c.setResizeable(true);
		createColumn(grid, c);

		c = new Column();
		c.setName("name");
		c.setText("名称");
		c.setWidth(120);
		c.setAlignment(SWT.LEFT);
		c.setMoveable(false);
		c.setResizeable(true);
		createColumn(grid, c);

		c = new Column();
		c.setName("summary");
		c.setText("合计");
		c.setWidth(120);
		c.setAlignment(SWT.RIGHT);
		c.setMoveable(false);
		c.setResizeable(true);
		createColumn(grid, c);

		Date[] range = input.getCBSRange();
		Calendar start = Calendar.getInstance();
		start.setTime(range[0]);
		start.set(Calendar.DATE, 1);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);

		Calendar end = Calendar.getInstance();
		end.setTime(range[1]);

		String year = null;
		GridColumnGroup grp = null;
		while (start.before(end)) {
			String nYear = "" + start.get(Calendar.YEAR);
			if (!nYear.equals(year)) {
				// 创建合计列
				if (grp != null) {
					c = new Column();
					c.setName("summary_" + (start.get(Calendar.YEAR) - 1));
					c.setText("合计");
					c.setWidth(100);
					c.setAlignment(SWT.RIGHT);
					c.setMoveable(false);
					c.setResizeable(true);
					c.setDetail(true);
					c.setSummary(true);
					createColumn(grp, c);
				}

				// 创建gruop
				grp = new GridColumnGroup(grid, SWT.TOGGLE);
				grp.setText(nYear);
				grp.setExpanded(true);
				year = nYear;
			}
			int i = start.get(Calendar.MONTH) + 1;
			String month = String.format("%02d", i);
			c = new Column();
			c.setName(nYear + month);
			c.setText(i + "月");
			c.setWidth(80);
			c.setAlignment(SWT.RIGHT);
			c.setMoveable(false);
			c.setResizeable(true);
			c.setDetail(true);
			c.setSummary(false);
			createColumn(grp, c);
			start.add(Calendar.MONTH, 1);
		}
		///////////////////////////////////////////////////////////////////////////////////
		// 创建操作列

		final List<Action> actions = config.getRowActions();
		int actionColWidth = BruiToolkit.actionMargin;

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
		vcol.setLabelProvider(new GridPartActionColumnLabelProvider(config, actions));
		grid.addListener(SWT.Selection, e -> {
			actions.stream().filter(a -> a.getId().equals(e.text)).findFirst().ifPresent(action -> {
				Object elem = e.item.getData();
				viewer.setSelection(new StructuredSelection(elem));
				invoveAction(e, elem, action);
			});
		});

		/////////////////////////////////////////////////////////////////////////////////////
		// 内容提供
		viewer.setContentProvider(new GridPartContentProvider(config));

		context.setSelectionProvider(viewer);

		return grid;
	}

	private void invoveAction(Event e, Object elem, Action action) {
		if (action.getChildren() == null || action.getChildren().isEmpty()) {
			BruiActionEngine.create(action, bruiService).invokeExecute(e, context);
		} else {
			// 显示菜单
			new ActionMenu(bruiService).setAssembly(config).setInput(elem).setContext(context)
					.setActions(action.getChildren()).setEvent(e).open();
		}
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
		col.setMoveable(false);
		col.setResizeable(true);
		col.setDetail(c.isDetail());
		col.setSummary(c.isSummary());

		GridViewerColumn vcol = new GridViewerColumn(viewer, col);
		GridPartColumnLabelProvider labelProvider = new GridPartColumnLabelProvider(renderEngine, c);

		vcol.setLabelProvider(labelProvider);

		return col;
	}

	@Override
	public void add(Object parent, Object child) {
		viewer.add(parent, child);
		viewer.refresh(parent);
		viewer.expandToLevel(parent, 1);
	}

	@Override
	public void doModify(Object element, Object newElement, BasicDBObject newData) {
		try {
			dataSetEngine.replace(element, newData);
			replaceItem(element, newElement);
		} catch (Exception e) {
			MessageDialog.openError(bruiService.getCurrentShell(), "更新", e.getMessage());
		}
	}

	@Override
	public void replaceItem(Object element, Object newElement) {
		update(AUtil.simpleCopy(newElement, element));
	}

	public void update(Object elem) {
		viewer.update(elem, null);
	}

	@Override
	public void doDelete(Object element) {
		try {
			Object parentElement = getParentElement(element);
			dataSetEngine.delete(element, parentElement);
			remove(parentElement, element);
		} catch (Exception e) {
			MessageDialog.openError(bruiService.getCurrentShell(), "删除", e.getMessage());
		}
	}

	public Object getParentElement(Object element) {
		Object parentData = Optional.ofNullable((GridItem) viewer.testFindItem(element)).map(i -> i.getParentItem())
				.map(p -> p.getData()).orElse(null);
		return parentData;
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

	@Override
	public void refresh(Object parent) {
		viewer.refresh(parent);
	}

	@Override
	public Object doGetEditInput(Object element) {
		return dataSetEngine.query(element);
	}

}
