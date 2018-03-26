package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.bizivisionsoft.widgets.gantt.ColumnConfig;
import com.bizivisionsoft.widgets.gantt.Config;
import com.bizivisionsoft.widgets.gantt.Gantt;
import com.bizivisionsoft.widgets.gantt.GanttEventCode;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiGridDataSetEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.model.WorkInfo;
import com.mongodb.BasicDBObject;

public class GanttPart {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

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
		gantt = new Gantt(parent, ganttConfig).setContainer(config.getName());
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
		gantt.setInputData(tasks, links);

		// 设置必须的事件侦听
		gantt.addGanttEventListener(GanttEventCode.onGridHeaderMenuClick.name(), e -> showHeadMenu(e));
		gantt.addGanttEventListener(GanttEventCode.onGridRowMenuClick.name(), e -> showRowMenu(e));
		
		
		//test
		addGanttEventListener(GanttEventCode.onAfterTaskAdd.name(), e1 -> {
			System.out.println(e1);
		});

	}

	private void showRowMenu(Event e) {
		new ActionMenu(config.getActions()).setContext(context).setService(bruiService).setEvent(e).open();
	}

	private void showHeadMenu(Event e) {
		new ActionMenu(config.getActions()).setContext(context).setService(bruiService).setEvent(e).open();
	}
	
	

	public void addGanttEventListener(String eventCode, Listener listener) {
		gantt.addGanttEventListener(eventCode, listener);
	}

	public void removeGanttListener(String eventCode, Listener listener) {
		gantt.removeGanttListener(eventCode, listener);
	}

	public void addTask() {
		WorkInfo item = new WorkInfo();
		item.setId(new ObjectId().toString());
		item.setDuration(10);
		item.setStart_date(new Date());
		item.setName("测试任务");
		gantt.addTask(item, null, 1);
	}

}
