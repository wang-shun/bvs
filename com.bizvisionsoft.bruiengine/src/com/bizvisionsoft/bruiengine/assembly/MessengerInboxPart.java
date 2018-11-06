package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;

import com.bizivisionsoft.widgets.pagination.Pagination;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.Controls;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.service.tools.Formatter;
import com.mongodb.BasicDBObject;

public class MessengerInboxPart implements IQueryEnable {

	private int scrollSelection = 0;

	@Inject
	private IBruiService bruiService;

	private Assembly config;

	@GetContent("viewer")
	protected GridTreeViewer viewer;

	protected BruiDataSetEngine dataSetEngine;

	protected long count;

	protected int limit;

	protected int skip;

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
		int _limit = config.getGridPageCount();
		if (_limit == 0) {
			limit = 30;
		} else {
			limit = _limit;
		}

		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);
		Assert.isNotNull(dataSetEngine, config.getName() + "组件缺少数据集定义");
	}

	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}

	@CreateUI
	public void createUI(Composite parent) {
		Controls.handle(createSticker(parent)).formLayout().put(panel -> {
			panel.setBackground(BruiColors.getColor(BruiColor.white));
			if(config.isGridPageControl()) {
				Controls.handle(createGridControl(panel)).loc(SWT.TOP | SWT.LEFT | SWT.RIGHT).bottom(100, -48);
				Controls.handle(createToolbar(panel)).loc(SWT.BOTTOM | SWT.LEFT | SWT.RIGHT, 48);
			}else {
				Controls.handle(createGridControl(panel)).loc(SWT.TOP | SWT.LEFT | SWT.RIGHT|SWT.BOTTOM);
			}
		});

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
		toolbar = Controls.comp(parent).formLayout().get();
		Controls.label(toolbar, SWT.HORIZONTAL | SWT.SEPARATOR).loc(SWT.TOP | SWT.LEFT | SWT.RIGHT);
		Controls.handle(createPageControl()).top(0, 1).bottom().left(0, 8).right(100, -8);
		return toolbar;
	}

	private Control createPageControl() {
		count = dataSetEngine.count(filter, context);
		// 获得最佳的每页记录数
		// 起始
		skip = 0;
		page = new Pagination(toolbar, SWT.LONG).setCount(count).setLimit(limit);
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

		viewer.addPostSelectionChangedListener(c -> {
			open(c.getStructuredSelection().getFirstElement());
		});

		if (config.isScrollLoadData()) {
			viewer.getGrid().getVerticalBar().addListener(SWT.Selection, e -> {
				ScrollBar bar = (ScrollBar) e.widget;
				int sel = bar.getSelection();
				if (scrollSelection == sel) {
					return;
				} else {
					scrollSelection = sel;
				}
				int max = bar.getMaximum();
				int thu = bar.getThumb();
				if (max - sel - thu <= 0) {
					currentPage++;
					skip = (currentPage) * limit;
					appendPageViewerInput();
				}
			});
		}

		return grid;
	}

	private void open(Object element) {
		if (element == null) {
			return;
		}
		if(element instanceof String) {
			return;
		}
		String cName = this.config.getName();
		StringBuffer sb = new StringBuffer();

		// 头像
		sb.append("<div style='display:block;'>");
		String sender = (String) AUtil.readValue(element, cName, "发送者", null);
		String senderName = sender;// .substring(0, sender.indexOf("[")).trim();

		// 内容区
		sb.append("<div> ");

		String subject = (String) AUtil.readValue(element, cName, "标题", null);
		sb.append("<div>发送者：" + senderName + "</div>");
		Date sendDate = (Date) AUtil.readValue(element, cName, "发送日期", null);
		sb.append("<div>日期：" + Formatter.getString(sendDate, "yyyy-MM-dd HH:mm:ss", RWT.getLocale()) + "</div>");
		sb.append("</div>");

		sb.append("<hr>");

		String content = (String) AUtil.readValue(element, cName, "内容", null);
		sb.append("<div style='white-space:normal;word-wrap:break-word;overflow:auto;;margin-top:8px'>" + content + "</div>");

		sb.append("</div>");
		Layer.alert(subject, sb.toString(), 460, 300);

		Object _id = AUtil.readValue(element, cName, "_id", null);
		dataSetEngine.replace(element, new BasicDBObject("read", true).append("_id", _id), context);
		AUtil.writeValue(element, cName, "是否已读", true);
		viewer.update(element, null);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void appendPageViewerInput() {
		List<?> input = (List<?>) dataSetEngine.query(skip, limit, filter, context);
		String cName = config.getName();
		String fName = "发送日期";
		Date defaultDate = new Date();

		String currentDate = null;
		for (int i = 0; i < input.size(); i++) {
			Date date = (Date) AUtil.readValue(input.get(i), cName, fName, defaultDate);
			String _date = Formatter.getString(date, null, RWT.getLocale());
			if (!_date.equals(currentDate)) {
				((List) viewer.getInput()).add(_date);
				currentDate = _date;
			}
			((List) viewer.getInput()).add((input.get(i)));
		}
		viewer.refresh();
	}

	public void setViewerInput(List<?> input) {
		String cName = config.getName();
		String fName = "发送日期";
		Date defaultDate = new Date();

		ArrayList<Object> result = new ArrayList<Object>();
		String currentDate = null;
		for (int i = 0; i < input.size(); i++) {
			Date date = (Date) AUtil.readValue(input.get(i), cName, fName, defaultDate);
			String _date = Formatter.getString(date, null, RWT.getLocale());
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
		Arrays.asList(viewer.getGrid().getItems()).stream().filter(i -> i.getChecked()).forEach(c -> result.add(c.getData()));
		return result;
	}

	public IBruiService getBruiService() {
		return bruiService;
	}

	public IBruiContext getContext() {
		return context;
	}

	public Assembly getConfig() {
		return config;
	}

	public void setCount(long count) {
		this.count = count;
		page.setCount(count);
	}

	public BasicDBObject getFilter() {
		return filter;
	}

	public void setSkip(int i) {
		skip = i;
	}

	public void setCurrentPage(int i) {
		currentPage = 0;
	}

	public void setFilter(BasicDBObject result) {
		filter = result;
	}

}
