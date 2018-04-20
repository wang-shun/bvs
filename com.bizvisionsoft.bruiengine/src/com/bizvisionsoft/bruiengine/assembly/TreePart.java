package com.bizvisionsoft.bruiengine.assembly;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

import com.bizivisionsoft.widgets.diagram.Diagram;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiEventEngine;
import com.bizvisionsoft.bruiengine.BruiGridDataSetEngine;
import com.bizvisionsoft.bruiengine.action.TreePartZoomInAction;
import com.bizvisionsoft.bruiengine.action.TreePartZoomOutAction;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class TreePart {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	private Assembly config;

	@GetContent("tree")
	private Diagram tree;

	private BruiGridDataSetEngine dataSetEngine;

	private BruiEventEngine eventEngine;

	public TreePart(Assembly config) {
		this.config = config;
	}

	@Init
	private void init() {
		dataSetEngine = BruiGridDataSetEngine.create(config, bruiService, context);
		eventEngine = BruiEventEngine.create(config, bruiService, context);
	}

	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);
		sticker.addActions(new TreePartZoomInAction(tree), new TreePartZoomOutAction(tree));
		sticker.context = context;
		sticker.service = bruiService;
		sticker.createUI(parent);
		return sticker.content;
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
		tree = new Diagram(panel).setContainer(config.getName());

		// 查询数据
		List<?> input = (List<?>) dataSetEngine.query(null, null, null, context);

		// 设置为gantt输入
		tree.setInputData(input);

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

	public void addEventListener(String eventCode, Listener listener) {
		tree.addEventListener(eventCode, listener);
	}

}
