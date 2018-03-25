package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.bizivisionsoft.widgets.gantt.ColumnConfig;
import com.bizivisionsoft.widgets.gantt.Config;
import com.bizivisionsoft.widgets.gantt.Gantt;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiGridDataSetEngine;
import com.bizvisionsoft.bruiengine.service.IBruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.bruiengine.util.Util;
import com.mongodb.BasicDBObject;

public class GanttPart {

	@Inject
	private IBruiService bruiService;

	@Inject
	private IBruiEditorContext context;

	private Assembly config;

	@GetContent("gantt")
	private Gantt gantt;

	private Config ganttConfig;

	private BruiGridDataSetEngine dataSetEngine;

	private List<?> tasks;

	private List<?> links;

	public GanttPart(Assembly config) {
		this.config = config;
	}

	@Init
	private void init() {
		dataSetEngine = BruiGridDataSetEngine.create(config, bruiService);

		ganttConfig = Config.defaultConfig(config.isReadonly());

		// 配置操作列
		ganttConfig.brui_RowMenuEnable = !Util.isEmptyOrNull(config.getActions());
		ganttConfig.brui_HeadMenuEnable = !Util.isEmptyOrNull(config.getHeadActions());

		ganttConfig.columns = new ArrayList<>();
		// 配置列和表格宽度
		int gridWidth = 0;
		config.getColumns();
		for (int i = 0; i < config.getColumns().size(); i++) {
			Column c = config.getColumns().get(i);

			ColumnConfig colConf = new ColumnConfig();
			ganttConfig.columns.add(colConf);

			colConf.label = c.getText();
			colConf.name = c.getName();
			colConf.resize = c.isResizeable();
			colConf.width = c.getWidth();
			colConf.tree = i == 0;
			switch (c.getAlignment()) {
			case SWT.CENTER:
				colConf.align = "center";
				break;
			case SWT.RIGHT:
				colConf.align = "right";
				break;
			default:
				colConf.align = "left";
				break;
			}
			if (c.isHide()) {
				colConf.hide = c.isHide();
				gridWidth += c.getWidth();
			}
		}

		if (config.isGanttGridWidthCalculate()) {
			ganttConfig.grid_width = gridWidth - 8;//
			if (ganttConfig.brui_RowMenuEnable || ganttConfig.brui_HeadMenuEnable) {
				ganttConfig.grid_width += 34;
			}
		} else {
			ganttConfig.grid_width = config.getGanttGridWidth();
		}

	}

	@CreateUI
	public void createUI(Composite parent) {
		parent.setLayout(new FillLayout());
		gantt = new Gantt(parent, ganttConfig);
		Date[] dateRange = dataSetEngine.getGanttInitDateRange();
		if (dateRange != null && dateRange.length == 2) {
			gantt.setInitDateRange(dateRange[0], dateRange[1]);
		}
		Calendar cal = Calendar.getInstance();
		Date from = cal.getTime();
		cal.add(Calendar.MONTH, 6);
		Date to = cal.getTime();
		gantt.setInitDateRange(from, to);
		
		// 查询数据
		tasks = dataSetEngine.getGanntInputData(new BasicDBObject());
		links = dataSetEngine.getGanntInputLink(new BasicDBObject());

		// 设置为gantt输入
		gantt.setInputData(config.getName(), tasks, links);

		// 设置事件侦听
		gantt.addListener(Gantt.EVENT_GRID_MENU, e -> showHeadMenu());
		gantt.addListener(Gantt.EVENT_ROW_MENU, e -> showRowMenu(e));

	}

	private void showRowMenu(Event e) {
		List<Action> actions = config.getActions();
		ActionMenu menu = new ActionMenu(actions);
		menu.open();
	}

	private void showHeadMenu() {
		List<Action> actions = config.getHeadActions();
		new ActionMenu(actions).open();
	}

}
