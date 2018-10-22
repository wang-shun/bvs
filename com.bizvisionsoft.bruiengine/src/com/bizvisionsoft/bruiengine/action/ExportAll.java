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
import com.bizvisionsoft.bruiengine.assembly.IExportable;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;

public class ExportAll {

	@Inject
	private IBruiService br;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context,
			@MethodParam(Execute.ACTION) Action action) {

		Map<Action, IExportable> actions = context.stream(IBruiContext.SEARCH_DOWN)// 向下遍历获得上下文的流
				.filter(c->c.getContent() instanceof IExportable )// 过滤不能导出数据的上下文
				.map(this::createExportAction)// 映射为Entry
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));// 收集到Map中
		int count = actions.size();
		if (count == 1) {
			actions.values().iterator().next().export();
		} else if (count > 1) {
			ActionMenu menu = new ActionMenu(br).setActions(new ArrayList<>(actions.keySet()));
			actions.forEach((a, c) -> menu.handleActionExecute(a.getName(), e -> {
				c.export();
				return false;
			}));
			menu.open();
		}

	}

	private Entry<Action, IExportable> createExportAction(IBruiContext context) {
		// 通过context获取导出按钮名称
		IExportable content = (IExportable) context.getContent();
		String title = content.getExportActionText();

		Action action = new Action();
		action.setName(title);
		action.setText("导出<br>" + title);
		action.setStyle("normal");
		// 创建一个键值对
		return new AbstractMap.SimpleEntry<Action, IExportable>(action, content);
	}

}
