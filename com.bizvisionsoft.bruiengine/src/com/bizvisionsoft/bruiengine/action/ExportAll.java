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

		Map<Action, IBruiContext> actions = context.stream(IBruiContext.SEARCH_DOWN)// ���±�����������ĵ���
				.filter(c -> isExportable(c, fName))// ���˲��ܵ������ݵ�������
				.map(c -> createExportAction(c, fName))// ӳ��ΪEntry
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));// �ռ���Map��
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
		return content instanceof IExportable || // ������ݲ������Ե���
				Check.instanceOf(content, IDataSetEngineProvider.class)// ���content��IDataSetEngineProvider
						.map(d -> d.getDataSetEngine())// ������ݼ�����
						.map(d -> d.exportable(fName, context))// ������ݼ������Ƿ�֧�ֵ���
						.orElse(false);//
	}

	private Entry<Action, IBruiContext> createExportAction(IBruiContext context, String fName) {
		// Assembly assembly = context.getAssembly();
		// ͨ��context��ȡ������ť����
		String title = getExportActionText(context, fName);
		// Stream.of(assembly.getStickerTitle(), assembly.getTitle(),
		// assembly.getName()).filter(Check::isAssigned)
		// .findFirst().orElse("");

		Action action = new Action();
		action.setName(title);
		action.setText("����<br>" + title);
		action.setStyle("normal");
		// ����һ����ֵ��
		return new AbstractMap.SimpleEntry<Action, IBruiContext>(action, context);
	}

	private String getExportActionText(IBruiContext context, String fName) {
		Object content = context.getContent();
		// �ж�content�Ƿ�̳���IExportable
		if (content instanceof IExportable) {
			return ((IExportable) content).getExportActionText();
		} else if (content instanceof IDataSetEngineProvider) {
			BruiDataSetEngine de = ((IDataSetEngineProvider) content).getDataSetEngine();
			if (de != null) {
				de.getExportActionText(fName, context);
			} else {
				String msg = "�����" + context.getAssembly() + ", DataSetΪ null��";
				throw new RuntimeException(msg);
			}
		}
		return "";
	}

	/**
	 * ����
	 * 
	 * @param context
	 * @param fName
	 * @return
	 */
	private boolean doExport(IBruiContext context, String fName) {
		Object content = context.getContent();
		// �ж�content�Ƿ�̳���IExportable
		if (content instanceof IExportable) {
			((IExportable) content).export();
		} else if (content instanceof IDataSetEngineProvider) {
			BruiDataSetEngine de = ((IDataSetEngineProvider) content).getDataSetEngine();
			if (de != null) {
				de.export(fName, context);
			} else {
				String msg = "�����" + context.getAssembly() + ", DataSetΪ null��";
				throw new RuntimeException(msg);
			}
		}
		return false;
	}

}
