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

		Map<Action, IExportable> actions = context.stream(IBruiContext.SEARCH_DOWN)// ���±�����������ĵ���
				.filter(c->c.getContent() instanceof IExportable )// ���˲��ܵ������ݵ�������
				.map(this::createExportAction)// ӳ��ΪEntry
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));// �ռ���Map��
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
		// ͨ��context��ȡ������ť����
		IExportable content = (IExportable) context.getContent();
		String title = content.getExportActionText();

		Action action = new Action();
		action.setName(title);
		action.setText("����<br>" + title);
		action.setStyle("normal");
		// ����һ����ֵ��
		return new AbstractMap.SimpleEntry<Action, IExportable>(action, content);
	}

}
