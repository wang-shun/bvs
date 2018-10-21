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

		Map<Action, IBruiContext> actions = context.stream(IBruiContext.SEARCH_DOWN)// ���±�����������ĵ���
				.filter(this::isExportable)// ���˲��ܵ������ݵ�������
				.map(this::createExportAction)// ӳ��ΪEntry
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));// �ռ���Map��
		int count = actions.size();
		if (count == 1) {
			doExport(actions.values().iterator().next());
		} else if (count > 1) {
			ActionMenu menu = new ActionMenu(br).setActions(new ArrayList<>(actions.keySet()));
			actions.forEach((a, c) -> menu.handleActionExecute(a.getName(), e -> doExport(c)));
			menu.open();
		}

	}

	private boolean isExportable(IBruiContext context) {
		Object content = context.getContent();
		return content instanceof IExportable; // ������ݲ������Ե���

	}

	private Entry<Action, IBruiContext> createExportAction(IBruiContext context) {
		// ͨ��context��ȡ������ť����
		String title = getExportActionText(context);

		Action action = new Action();
		action.setName(title);
		action.setText("����<br>" + title);
		action.setStyle("normal");
		// ����һ����ֵ��
		return new AbstractMap.SimpleEntry<Action, IBruiContext>(action, context);
	}

	private String getExportActionText(IBruiContext context) {
		Object content = context.getContent();
		// �����ж�content�Ƿ�̳���IExportable��ֻ��IExportable�Ż����˷���
		return ((IExportable) content).getExportActionText();
	}

	/**
	 * ����
	 * 
	 * @param context
	 * @param fName
	 * @return
	 */
	private boolean doExport(IBruiContext context) {
		Object content = context.getContent();
		// �����ж�content�Ƿ�̳���IExportable��ֻ��IExportable�Ż����˷���
		((IExportable) content).export();
		return false;
	}

}
