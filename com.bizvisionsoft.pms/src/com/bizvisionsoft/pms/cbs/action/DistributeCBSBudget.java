package com.bizvisionsoft.pms.cbs.action;

import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Selector;
import com.bizvisionsoft.service.CBSService;
import com.bizvisionsoft.service.model.CBSItem;
import com.bizvisionsoft.service.model.WorkInfo;
import com.bizvisionsoft.serviceconsumer.Services;

public class DistributeCBSBudget {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		context.selected(parent -> {
			new Selector(brui.getAssembly("�׶�ѡ����"), context).setInput(context.getRootInput()).setTitle("����Ԥ�㵽ָ���׶�")
					.open(r -> {
						// TODO ��CBS�ڵ�����ʾ���䵽�ĸ��׶�
						// TODO �׶�ѡ��������ʾ�������
						// TODO ������ЩԤ����Է���
						// TODO ȡ���׶ε�Ԥ�����
						WorkInfo workInfo = (WorkInfo) r.get(0);
						Services.get(CBSService.class).allocateBudget(((CBSItem) parent).get_id(),
								workInfo.get_id(),workInfo.toString());
						// TODO ���󷵻�
						// TODO �ɹ���ʾ
					});

		});
	}

}
