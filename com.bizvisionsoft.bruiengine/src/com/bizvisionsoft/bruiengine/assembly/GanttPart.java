package com.bizvisionsoft.bruiengine.assembly;

import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.gantt.Config;
import com.bizivisionsoft.widgets.gantt.Gantt;
import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetContent;
import com.bizvisionsoft.bruicommons.annotation.Init;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiGridDataSetEngine;
import com.bizvisionsoft.bruiengine.service.IBruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
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

	public GanttPart(Assembly config) {
		this.config = config;
	}

	@Init
	private void init() {
		dataSetEngine = BruiGridDataSetEngine.create(config, bruiService);

		ganttConfig = Config.defaultConfig(config.isReadonly());

		// 配置列TODO

	}

	@CreateUI
	public void createUI(Composite parent) {
		parent.setLayout(new FillLayout());
		gantt = new Gantt(parent, ganttConfig);
		Date[] dateRange = dataSetEngine.getGanttInitDateRange();
		if (dateRange != null && dateRange.length == 2) {
			gantt.setInitDateRange(dateRange[0], dateRange[1]);
		}

		gantt.setInputData(dataSetEngine.getGanttInput(new BasicDBObject(),new BasicDBObject()));

		gantt.addListener(Gantt.EVENT_GRID_MENU, e -> System.out.println("grid" + e));
		gantt.addListener(Gantt.EVENT_ROW_MENU, e -> {
			MessageDialog.openInformation(parent.getShell(), "甘特图事件", "" + e.data);
		});

	}

}
