package com.bizvisionsoft.demo.rsclient;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.timeline.TimeLine;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContainer;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class TimeLineDemo {

	@Inject
	private IBruiService bruiService;

	@GetContainer
	private Composite content;

	@CreateUI
	private void createUI(Composite parent) {
		parent.setLayout(new FillLayout());
		createTimeLine(parent);
	}

	private void createTimeLine(Composite parent) {
		TimeLine tl = new TimeLine(parent, SWT.BORDER);
		tl.append("3��17������", "        �й����ڵȴ�һ��ʱ�̵�����ÿ���˶����Ǽ�֤�ߣ���һʱ��ǰ��δ�С�"
				+ "        <br>����10ʱ38�֣���ʮ����ȫ���˴�һ�λ�������ȫ������ϣ�ϰ��ƽȫƱ��ѡ�л����񹲺͹���ϯ���л����񹲺͹��������ίԱ����ϯ��" + "      ");
		tl.append("8��17��", "        �й�������ս��ʤ��72����" + "        <br>�������룬���ܶ�������������������ı�Թ�������ǵ�ȷ��������õ�ʱ��"
				+ "        <br>���ǡ��ж�" + "        <br>����Ϊ�л�����ԡѪ��ս��Ӣ�۽�ʿ" + "        <br>��������" + "      ");
		tl.append("8��18��", "        �й�������ս��ʤ��72����" + "        <br>�������룬���ܶ�������������������ı�Թ�������ǵ�ȷ��������õ�ʱ��"
				+ "        <br>���ǡ��ж�" + "        <br>����Ϊ�л�����ԡѪ��ս��Ӣ�۽�ʿ" + "        <br>��������" + "      ");
		tl.append("8��20��", "" + "        �й�������ս��ʤ��72����" + "        <br>�������룬���ܶ�������������������ı�Թ�������ǵ�ȷ��������õ�ʱ��"
				+ "        <br>���ǡ��ж�" + "        <br>����Ϊ�л�����ԡѪ��ս��Ӣ�۽�ʿ" + "        <br>��������");
	}


}
