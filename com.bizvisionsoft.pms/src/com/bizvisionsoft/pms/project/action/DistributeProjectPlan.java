package com.bizvisionsoft.pms.project.action;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.ProjectService;
import com.bizvisionsoft.service.model.Project;
import com.bizvisionsoft.service.model.Result;
import com.bizvisionsoft.serviceconsumer.Services;

public class DistributeProjectPlan {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute(@MethodParam(value = Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(value = Execute.PARAM_EVENT) Event event) {
		Project project = (Project) context.getRootInput();

		// �����Ŀ�ǰ��׶��ƽ���
		if (project.isStageEnable()) {
			Shell s = brui.getCurrentShell();
			boolean ok = MessageDialog.openConfirm(s, "�´���Ŀ�׶μƻ�",
					"��ȷ���´���Ŀ" + project + "��ǰ�Ľ׶μƻ���</p>ϵͳ��֪ͨ���׶θ��������ձ��ƻ��ƶ���ϸ�ƻ���</p>[��ʾ]δȷ�������˵Ľ׶Σ�����ȷ�������´�ƻ���");
			if (!ok) {
				return;
			}
			List<Result> result = Services.get(ProjectService.class).distributeProjectPlan(project.get_id(),
					brui.getCurrentUserId());
			if (result.isEmpty()) {
				MessageDialog.openInformation(s, "�´���Ŀ�׶μƻ�", "��Ŀ�׶μƻ��´���ɡ�");
			}else {
				// TODO ��ʾ����������Ϣ��ͨ�÷���
				MessageDialog.openError(s, "�´���Ŀ�׶μƻ�", "��Ŀ�׶μƻ��´�ʧ�ܡ�</p>"+result.get(0).message);
			}
		} else {
			// TODO ���ǰ��׶��ƽ����´�
		}
	}

}
