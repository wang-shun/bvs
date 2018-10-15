package com.bizvisionsoft.onlinedesigner.systemupdate;

import org.eclipse.jface.dialogs.MessageDialog;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.serviceconsumer.Services;

public class SystemUpdateV0501_pmo {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute() {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("���θ������ݣ�<br/>");
			sb.append("1. ������֯ģ�壬����Ŀ�����󣬿�ֱ��ͨ��������֯ģ��ķ�ʽ������Ŀ�Ŷӡ�<br/>");
			sb.append("2. �޸���֯����Ŀ�е���λ�ֶ�����Ϊ��qualifiedContractor��<br/>");
			sb.append("3. ���ӱ�����������п��Բ鿴�ҹ������Ŀ���ձ����ܱ����±���<br/>");
			sb.append("4. ������Ŀ��ɫ����Ŀ�����飨��ɫ���Ϊ��PMO�������ܽ�ɫ����Ӧ���������쾭��������");
			sb.append("��ͨ��PMO��ɫ��������Ŀ���ɹ��������������ɱ�����ͱ����������ʾ�����ݽ���ϸ�֡�<br/>");
			sb.append("������Ŀ��������Ŀ�ܼ����Ŀ����ԱȨ�޵��˻����Է��ʣ�������Ŀ�ܼ�Ȩ�޵��û������п��Բ鿴������Ŀ��Ϣ����Ŀ����ԱȨ�޵��˻�ֻ�ܿ�������Ϊ��ĿPMO�Ŷӳ�Ա����Ŀ��<br/>");
			sb.append(
					"�ɹ��������й�Ӧ������͹�Ӧ������Ȩ�޵��˻����Է��ʣ����й�Ӧ������Ȩ�޵��û������п��Բ鿴������Ŀ�Ĳɹ���������Ӧ������Ȩ�޵��˻�ֻ�ܿ�������Ϊ��ĿPMO�Ŷӳ�Ա����Ŀ�Ĳɹ�������<br/>");
			sb.append("�����������������������쾭��Ȩ�޵��˻����Է��ʣ������������Ȩ�޵��û������п��Բ鿴������Ŀ���������������쾭��Ȩ�޵��˻�ֻ�ܿ�������Ϊ��ĿPMO�Ŷӳ�Ա�ĵ�����������<br/>");
			sb.append("�ɱ��������гɱ�����Ͳ�����Ȩ�޵��˻����Է��ʣ����гɱ�����Ȩ�޵��û������п��Բ鿴������Ŀ�ĳɱ����ݡ�������Ȩ�޵��˻�ֻ�ܿ�������Ϊ��ĿPMO�Ŷӳ�Ա����Ŀ�ɱ����ݡ�<br/>");
			sb.append("�������������Ŀ�ܼ����Ŀ����ԱȨ�޵��˻����Է��ʣ�������Ŀ�ܼ�Ȩ�޵��û������п��Բ鿴������Ŀ�ı��档��Ŀ����ԱȨ�޵��˻�ֻ�ܿ�������Ϊ��ĿPMO�Ŷӳ�Ա����Ŀ�ı��档<br/>");
			sb.append("<span class='layui-badge'>ע</span>��");
			sb.append("���¸ù���ʱ��ϵͳ���Զ����Ѵ�������Ŀ�����PMO�Ŷӡ��ù��ܸ�����ɺ����ڷ������˴��js��ѯ��Ŀ����������������ļ���<br/>");
			sb.append("1.��ѯ-��ĿPMO��Ա.js��<br/>");
			sb.append("2.׷��-CBS-CBSҶ�ӽڵ�ID.js��<br/>");
			sb.append("3.׷��-CBSScope-CBSҶ�ӽڵ�ID.js��");
			sb.append("<br/>");
			sb.append("��ȷ�Ͻ��б��θ��¡�");
			if (brui.confirm("������Ŀ������", sb.toString())) {
				Services.get(SystemService.class).updateSystem("5.1M1", "PMO");
				Layer.message("�����Ŀ���������");
			}
		} catch (Exception e) {
			MessageDialog.openError(brui.getCurrentShell(), "��Ŀ��������´���", e.getMessage());
		}
	}
}
