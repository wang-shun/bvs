package com.bizvisionsoft.bruiengine.app.sysman;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.tools.WidgetHandler;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class AlertASM {

	@Inject
	private IBruiService brui;

	@CreateUI
	private void createUI(Composite parent) {
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormLayout layout = new FormLayout();
		layout.marginHeight = 12;
		layout.marginWidth = 12;
		parent.setLayout(layout);

		Composite titlePanel = new Composite(parent, SWT.NONE);
		WidgetHandler.getHandler(titlePanel).setHtmlContent(getTitle());
		FormData fd = new FormData();
		titlePanel.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(0, 24);
		fd.height = 48;

		Composite infoPanel = new Composite(parent, SWT.NONE);
		WidgetHandler.getHandler(infoPanel).setHtmlContent(getInfo());
		fd = new FormData();
		infoPanel.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(titlePanel, 72);
		fd.height = 120;

		Composite mntPanel = new Composite(parent, SWT.NONE);
		WidgetHandler.getHandler(mntPanel).setHtmlContent(getMntInfo());
		fd = new FormData();
		mntPanel.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(infoPanel, 24);
		fd.height = 120;

		final Button mntBtn = new Button(parent, SWT.CHECK);
		mntBtn.setData(RWT.CUSTOM_VARIANT, "switch");
		fd = new FormData();
		mntBtn.setLayoutData(fd);
		fd.right = new FormAttachment(100, -12);
		fd.top = new FormAttachment(infoPanel, 30);
		fd.height = 38;
		mntBtn.moveAbove(mntPanel);
		mntBtn.setOrientation(SWT.RIGHT_TO_LEFT);
		mntBtn.addListener(SWT.Selection, e -> switchMnt(mntBtn));
		mntBtn.setSelection(ModelLoader.site.getShutDown() != null);
		updateMntBtn(mntBtn);

		Composite backupPanel = new Composite(parent, SWT.NONE);
		WidgetHandler.getHandler(backupPanel).setHtmlContent(getBackupInfo());
		fd = new FormData();
		backupPanel.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.top = new FormAttachment(mntPanel, 24);
		fd.height = 120;

		Button backupBtn = new Button(parent, SWT.PUSH);
		backupBtn.setText("��ʼ����");
		backupBtn.setData(RWT.CUSTOM_VARIANT, "info");
		fd = new FormData();
		backupBtn.setLayoutData(fd);
		fd.right = new FormAttachment(100, -12);
		fd.top = new FormAttachment(mntPanel, 36);
		fd.height = 38;
		backupBtn.moveAbove(backupPanel);
		backupBtn.addListener(SWT.Selection, e -> backup());

	}

	private void backup() {
		brui.backup();
	}

	private void switchMnt(Button mntBtn) {
		brui.switchMnt(mntBtn.getSelection());
		updateMntBtn(mntBtn);
	}

	private void updateMntBtn(Button mntBtn) {
		if (mntBtn.getSelection()) {
			mntBtn.setText("" + ModelLoader.site.getShutDown());
		} else {
			mntBtn.setText("");
		}
	}

	private String getTitle() {
		return "<div style='font-size:30px'><img src='/resource/image/warning_c.svg' width=48px height=48px/>  ��Ҫ��ʾ</div>";
	}

	private String getInfo() {
		return "<blockquote class='layui-elem-quote' style='border-left: 5px solid #ff9800;'>"
				+ "<div class='label_headline'>������˽�����Ҫִ�еĲ���</div>"
				+ "<br/>���ڱ�ҳ���ִ�еĲ����辭��רҵ��ѵ�����ʵ��Ĳ������ܶ�ϵͳ��������Ӱ�졣������ȷ���Ƿ��˽⽫Ҫִ�еĲ�����ϵͳ����ɵ�Ӱ����ǰ�����˳�ϵͳ���ã�����ѯרҵ��ʿ��"
				+ "</blockquote>";
	}

	private String getMntInfo() {
		return "<blockquote class='layui-elem-quote'>" + "<div class='label_headline'>�Ƿ���Ҫ����ϵͳά����</div>"
				+ "<br/>����ϵͳά���󣬳���<b style='color:red'>su</b>��<b style='color:red'>ҵ�����Ա</b>��<b style='color:red'>ϵͳ����Ա</b>�˻������"
				+ "�����û�����ά����ʼ��<b style='color:red'>�޷�ʹ��ϵͳ</b>��" + "ֱ�����ر�ϵͳά������ά����ʼʱ����δ������ǰ���ѵ�¼���û����յ���ʾ��"
				+ "���ɼ���δ��Ĳ�������ά����ʼʱ�䵽��ʱ����Щ�û�����<b style='color:red'>ǿ���˳���" + "</blockquote>";
	}

	private String getBackupInfo() {
		return "<blockquote class='layui-elem-quote' style='border-left: 5px solid #03a9f4;'>"
				+ "<div class='label_headline'>����ϵͳ���ú�ҵ��������ݣ�</div>"
				+ "<br>��ϵͳ���б����Ǳ�������ά��������ɲ������Ӱ�����Ч��ʩ������������ϵͳ���б�����Ҫ����ά��״̬�������Ҫ��ֹ��������ù���������ݵ���ʧʱ����Ӳ���𻵻����ϵͳ���������������Ӧ�����ö���ĸ��Ƽ�ģʽ��������ϵͳ���ݡ�"
				+ "</blockquote>";
	}

}
