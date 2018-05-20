package com.bizvisionsoft.bruiengine.assembly;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.bizivisionsoft.widgets.pagination.Pagination;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.mongodb.BasicDBObject;

public class MessengerInboxPart {

	private static final int LIMIT = 30;

	@Inject
	private IBruiService bruiService;

	private Assembly config;

	@GetContent("viewer")
	protected GridTreeViewer viewer;

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

	private BasicDBObject filter;

	public MessengerInboxPart() {
	}

	public MessengerInboxPart(Assembly gridConfig) {
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
		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);
		Assert.isNotNull(dataSetEngine, config.getName()+"组件缺少数据集定义");
	}

	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}

	@CreateUI
	public void createUI(Composite parent) {
		Composite panel = createSticker(parent);
		panel.setLayout(new FormLayout());
		Control grid = createGridControl(panel);
		Control pagec = createToolbar(panel);

		FormData fd = new FormData();
		grid.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100, -48);

		fd = new FormData();
		pagec.setLayoutData(fd);
		fd.height = 48;
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		setViewerInput();
	}

	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);
		sticker.context = context;
		sticker.service = bruiService;
		sticker.createUI(parent);
		return sticker.content;
	}

	private Control createToolbar(Composite parent) {

		toolbar = new Composite(parent, SWT.NONE);
		toolbar.setLayout(new FormLayout());
		FormData fd = new FormData();
		new Label(toolbar, SWT.HORIZONTAL | SWT.SEPARATOR).setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		// 求出有多少记录
		createPageControl();

		fd = new FormData();
		page.setLayoutData(fd);
		fd.top = new FormAttachment(0, 1);
		fd.bottom = new FormAttachment(100);
		fd.left = new FormAttachment(0, 8);
		fd.right = new FormAttachment(100, -8);
		return toolbar;
	}

	private Control createPageControl() {
		count = dataSetEngine.count(filter, context);
		// 获得最佳的每页记录数
		limit = LIMIT;
		// 起始
		skip = 0;
		page = new Pagination(toolbar, SWT.LONG).setCount(count).setLimit(LIMIT);
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
		createColumn(grid);

		/////////////////////////////////////////////////////////////////////////////////////
		// 内容提供
		viewer.setContentProvider(new GridPartContentProvider(config));

		context.setSelectionProvider(viewer);

		return grid;
	}

	protected GridTreeViewer createGridViewer(Composite parent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// 创建表格

		GridTreeViewer viewer = new GridTreeViewer(parent, SWT.V_SCROLL);
		Grid grid = viewer.getGrid();
		grid.setHeaderVisible(false);
		grid.setFooterVisible(false);
		grid.setLinesVisible(true);
		UserSession.bruiToolkit().enableMarkup(grid);

		return viewer;
	}

	public void setViewerInput() {
		setViewerInput((List<?>) dataSetEngine.query(skip, limit, filter, context));
	}

	public void setViewerInput(List<?> input) {
		String cName = config.getName();
		String fName = "发送日期";
		Date defaultDate = new Date();

		ArrayList<Object> result = new ArrayList<Object>();
		String currentDate = null;
		for (int i = 0; i < input.size(); i++) {
			Date date = (Date) AUtil.readValue(input.get(i), cName, fName, defaultDate);
			String _date = new SimpleDateFormat("yyyy年M月d日").format(date);
			if (!_date.equals(currentDate)) {
				result.add(_date);
				currentDate = _date;
			}
			result.add(input.get(i));
		}

		viewer.setInput(result);
	}

	protected GridViewerColumn createColumn(Grid grid) {
		final GridColumn col = new GridColumn(grid, SWT.NONE);
		GridViewerColumn vcol = new GridViewerColumn(viewer, col);
		vcol.setLabelProvider(new MessageLabelProvider(config.getName()));

		grid.addListener(SWT.Resize, e -> {
			int width = grid.getBounds().width;
			if (width != 0)
				col.setWidth(width);
		});

		return vcol;
	}

	public List<Object> getCheckedItems() {
		ArrayList<Object> result = new ArrayList<Object>();
		Arrays.asList(viewer.getGrid().getItems()).stream().filter(i -> i.getChecked())
				.forEach(c -> result.add(c.getData()));
		return result;
	}

}
