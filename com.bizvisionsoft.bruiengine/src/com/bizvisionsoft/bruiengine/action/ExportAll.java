package com.bizvisionsoft.bruiengine.action;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiDataSetEngine;
import com.bizvisionsoft.bruiengine.assembly.IDataSetEngineProvider;
import com.bizvisionsoft.bruiengine.assembly.IExportable;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.service.tools.Check;

public class ExportAll {

	@Inject
	private IBruiService br;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context,
			@MethodParam(Execute.ACTION) Action action) {
		String fName = action.getName();

		Map<Action, IBruiContext> actions = context.stream(IBruiContext.SEARCH_DOWN)//
				.filter(c -> isExportable(c.getContent(), c, fName))//
				.map(this::createExportAction)//
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		int count = actions.size();
		if (count == 1) {
			doExport(actions.values().iterator().next(), fName);
		} else if (count > 1) {
			ActionMenu menu = new ActionMenu(br).setActions(new ArrayList<>(actions.keySet()));
			actions.forEach((a, c) -> menu.handleActionExecute(a.getName(), e -> doExport(c, fName)));
			menu.open();
		}

	}

	private boolean isExportable(Object content, final IBruiContext context, final String fName) {
		return content instanceof IExportable || //
				Check.instanceOf(content, IDataSetEngineProvider.class)//
						.map(d -> d.getDataSetEngine())//
						.map(d -> d.exportable(fName, context))//
						.orElse(false);//
	}

	private Entry<Action, IBruiContext> createExportAction(IBruiContext context) {
		Assembly assembly = context.getAssembly();
		String title = Stream.of(assembly.getStickerTitle(), assembly.getTitle(), assembly.getName())
				.filter(Check::isAssigned).findFirst().orElse("");

		Action action = new Action();
		action.setName(assembly.getName());
		action.setText("导出" + title);
		action.setStyle("normal");
		return new AbstractMap.SimpleEntry<Action, IBruiContext>(action, context);
	}

	private boolean doExport(IBruiContext context, String fName) {
		Object content = context.getContent();
		// 判断content是否继承于IExportable
		if (content instanceof IExportable) {
			((IExportable) content).export();
		} else if (content instanceof IDataSetEngineProvider) {
			BruiDataSetEngine de = ((IDataSetEngineProvider) content).getDataSetEngine();
			if (de != null) {
				de.export(fName, context);
			} else {
				String msg = "组件：" + context.getAssembly() + ", DataSet为 null。";
				throw new RuntimeException(msg);
			}
		}
		return false;
	}

}
