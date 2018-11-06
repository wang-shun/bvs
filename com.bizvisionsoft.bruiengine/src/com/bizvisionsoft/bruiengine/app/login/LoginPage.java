package com.bizvisionsoft.bruiengine.app.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Controls;

public class LoginPage extends SiteHomePage {

	@Inject
	private IBruiService br;
	private Text tName;
	private Text tPassword;
	private Button bRemember;

	@CreateUI
	protected void createUI(Composite parent) {
		super.createUI(parent);
	}

	protected void createInputs(Composite parent) {
		FormLayout layout = new FormLayout();
		layout.marginWidth = 36;
		layout.marginHeight = 36;
		layout.spacing = 8;
		parent.setLayout(layout);

		tName = Controls.label(parent).html("<div class='label_button' style='color:rgb(74, 74, 74);'>系统账号</div>")
				.loc(SWT.LEFT | SWT.RIGHT | SWT.TOP).add(0, () -> Controls.text(parent).loc(SWT.LEFT | SWT.RIGHT)).get();

		tPassword = Controls.label(parent).html("<div class='label_button' style='color:rgb(74, 74, 74);'>密码</div>")
				.loc(SWT.LEFT | SWT.RIGHT).top(tName, 12)
				.add(0, () -> Controls.text(parent, SWT.BORDER | SWT.PASSWORD).loc(SWT.LEFT | SWT.RIGHT)).get();

		bRemember = Controls.button(parent).rwt(BruiToolkit.CSS_INFO).loc(SWT.LEFT | SWT.RIGHT).top(tPassword, 12)
				.html("<div class='label_button' style='font-size:13px;'>登录</div>").defaultButton().select(this::login)//
				.add(0, () -> Controls.button(parent, SWT.CHECK).loc(SWT.LEFT | SWT.RIGHT).setText("在本机记录我的登录状态")).get();
	}

	private void login(Event event) {
		String userName = tName.getText().trim();
		String password = tPassword.getText().trim();

		try {
			br.checkLogin(userName, password);
			if (bRemember.getSelection())
				br.saveClientLogin(userName, password);
			br.closeCurrentPart();
		} catch (Exception e) {
			Layer.error(e);
		}

	}

}
