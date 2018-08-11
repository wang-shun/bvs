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
		backupBtn.setText("开始备份");
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
		return "<div style='font-size:30px'><img src='/resource/image/warning_c.svg' width=48px height=48px/>  重要提示</div>";
	}

	private String getInfo() {
		return "<blockquote class='layui-elem-quote' style='border-left: 5px solid #ff9800;'>"
				+ "<div class='label_headline'>请务必了解您将要执行的操作</div>"
				+ "<br/>您在本页面可执行的操作需经过专业培训，不适当的操作可能对系统产生严重影响。当您不确定是否了解将要执行的操作对系统所造成的影响以前，请退出系统设置，或咨询专业人士。"
				+ "</blockquote>";
	}

	private String getMntInfo() {
		return "<blockquote class='layui-elem-quote'>" + "<div class='label_headline'>是否需要启动系统维护？</div>"
				+ "<br/>启用系统维护后，除了<b style='color:red'>su</b>、<b style='color:red'>业务管理员</b>和<b style='color:red'>系统管理员</b>账户以外的"
				+ "所有用户将在维护开始后<b style='color:red'>无法使用系统</b>，" + "直到您关闭系统维护。当维护起始时间尚未到来以前，已登录的用户将收到提示，"
				+ "并可继续未完的操作，当维护起始时间到来时，这些用户将被<b style='color:red'>强制退出。" + "</blockquote>";
	}

	private String getBackupInfo() {
		return "<blockquote class='layui-elem-quote' style='border-left: 5px solid #03a9f4;'>"
				+ "<div class='label_headline'>备份系统设置和业务基础数据？</div>"
				+ "<br>对系统进行备份是避免错误的维护操作造成不可挽回影响的有效措施，对整个运行系统进行备份需要进入维护状态。如果需要防止因基础设置故障造成数据的损失时（如硬件损坏或操作系统崩溃等情况），您应当采用多机的复制集模式，而不是系统备份。"
				+ "</blockquote>";
	}

}
