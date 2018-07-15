package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;

import org.bson.Document;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.chart.ECharts;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;

public class ChartPart implements IQueryEnable {

	@Inject
	private IBruiService bruiService;

	private Assembly config;

	@GetContent("chart")
	protected ECharts chart;

	protected BruiDataSetEngine dataSetEngine;

	@Inject
	private BruiAssemblyContext context;

	private BasicDBObject filter;

	private JsonValue option;

	public ChartPart() {
	}

	public ChartPart(Assembly gridConfig) {
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
		// 注册数据集引擎
		dataSetEngine = BruiDataSetEngine.create(config, bruiService, context);

	}

	public BruiDataSetEngine getDataSetEngine() {
		return dataSetEngine;
	}

	@CreateUI
	public void createUI(Composite parent) {
		Composite panel;
		if (config.isHasTitlebar()) {
			panel = createSticker(parent);
		} else {
			panel = parent;
		}
		panel.setLayout(new FillLayout());
		createControl(panel);
		setViewerInput();

	}

	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);
		sticker.context = context;
		sticker.service = bruiService;
		sticker.createUI(parent);
		return sticker.content;
	}

	protected ECharts createControl(Composite parent) {
		chart = new ECharts(parent, SWT.NONE);
		chart.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		return chart;
	}

	public void setViewerInput() {
		try {
			setViewerInput(dataSetEngine.query(filter, context, "list"));
		} catch (Exception e) {
			e.printStackTrace();
			Layer.message(e.getMessage(), Layer.ICON_CANCEL);
		}
	}

	public void setViewerInput(Object input) {
		if (input == null) {
			throw new RuntimeException("数据源list方法返回空。");
		}

		if (input instanceof List<?>) {
			if (((List<?>) input).isEmpty()) {
				throw new RuntimeException("数据源list方法返回的List没有数据。");
			} else if (((List<?>) input).size() == 1) {
				Object doc = ((List<?>) input).get(0);
				setViewerInput(doc);
			} else {
				JsonArray _option = new JsonArray();
				for(int i=0;i<((List<?>) input).size();i++) {
					Document d = (Document) ((List<?>) input).get(i);
					_option.add(JsonObject.readFrom(d.toJson()));
				}
				option = _option;
			}
		} else if (input instanceof Document) {
			option = JsonObject.readFrom(((Document) input).toJson());
		} else if (input instanceof String) {
			option = JsonObject.readFrom((String) input);
		} else {
			String json = new GsonBuilder().create().toJson(input);
			option = JsonObject.readFrom(json);
		}

		chart.setOption(option);
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
	public void setCount(long cnt) {
	}

	@Override
	public BasicDBObject getFilter() {
		return filter;
	}

	@Override
	public void setSkip(int i) {
	}

	@Override
	public void setCurrentPage(int i) {
	}

	@Override
	public void setFilter(BasicDBObject result) {
		this.filter = result;
	}

}
