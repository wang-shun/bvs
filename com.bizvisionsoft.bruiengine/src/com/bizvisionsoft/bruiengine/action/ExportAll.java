package com.bizvisionsoft.bruiengine.action;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
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

		Map<Action, IBruiContext> actions = context.stream(IBruiContext.SEARCH_DOWN)// 向下遍历获得上下文的流
				.filter(c -> isExportable(c, fName))// 过滤不能导出数据的上下文
				.map(c -> createExportAction(c, fName))// 映射为Entry
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));// 收集到Map中
		int count = actions.size();
		if (count == 1) {
			doExport(actions.values().iterator().next(), fName);
		} else if (count > 1) {
			ActionMenu menu = new ActionMenu(br).setActions(new ArrayList<>(actions.keySet()));
			actions.forEach((a, c) -> menu.handleActionExecute(a.getName(), e -> doExport(c, fName)));
			menu.open();
		}

	}

	private boolean isExportable(final IBruiContext context, final String fName) {
		Object content = context.getContent();
		return content instanceof IExportable || // 如果内容部件可以导出
				Check.instanceOf(content, IDataSetEngineProvider.class)// 如果content是IDataSetEngineProvider
						.map(d -> d.getDataSetEngine())// 获得数据集引擎
						.map(d -> d.exportable(fName, context))// 获得数据集引擎是否支持导出
						.orElse(false);//
	}

	private Entry<Action, IBruiContext> createExportAction(IBruiContext context, String fName) {
		// Assembly assembly = context.getAssembly();
		// 通过context获取导出按钮名称
		String title = getExportActionText(context, fName);
		// Stream.of(assembly.getStickerTitle(), assembly.getTitle(),
		// assembly.getName()).filter(Check::isAssigned)
		// .findFirst().orElse("");

		Action action = new Action();
		action.setName(title);
		action.setText("导出<br>" + title);
		action.setStyle("normal");
		// 创建一个键值对
		return new AbstractMap.SimpleEntry<Action, IBruiContext>(action, context);
	}

	private String getExportActionText(IBruiContext context, String fName) {
		Object content = context.getContent();
		// 判断content是否继承于IExportable
		if (content instanceof IExportable) {
			return ((IExportable) content).getExportActionText();
		} else if (content instanceof IDataSetEngineProvider) {
			BruiDataSetEngine de = ((IDataSetEngineProvider) content).getDataSetEngine();
			if (de != null) {
				de.getExportActionText(fName, context);
			} else {
				String msg = "组件：" + context.getAssembly() + ", DataSet为 null。";
				throw new RuntimeException(msg);
			}
		}
		return "";
	}

	/**
	 * 导出
	 * 
	 * @param context
	 * @param fName
	 * @return
	 */
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
