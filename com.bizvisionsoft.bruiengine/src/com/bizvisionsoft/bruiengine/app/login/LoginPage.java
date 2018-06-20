package com.bizvisionsoft.bruiengine.app.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;

public class LoginPage {

	@Inject
	private IBruiService bruiService;

	@CreateUI
	private void createUI(Composite parent) {
		int u = 8;
		parent.setLayout(new FormLayout());

		final Text tName = new Text(parent, SWT.BORDER);
		tName.setMessage("�����������˺�");
		FormData fd = new FormData();
		tName.setLayoutData(fd);
		fd.top = new FormAttachment(0, 4 * u);
		fd.left = new FormAttachment(0, 4 * u);
		fd.right = new FormAttachment(100, -4 * u);
		fd.width = 240;

		final Text tPassword = new Text(parent, SWT.BORDER | SWT.PASSWORD);
		tPassword.setMessage("��¼����");
		fd = new FormData();
		tPassword.setLayoutData(fd);
		fd.top = new FormAttachment(tName, 2 * u);
		fd.left = new FormAttachment(0, 4 * u);
		fd.right = new FormAttachment(100, -4 * u);
		fd.width = 240;

		Button bLogin = UserSession.bruiToolkit().newStyledControl(Button.class, parent, SWT.PUSH,
				BruiToolkit.CSS_INFO);
		bLogin.setText("��¼");
		fd = new FormData();
		bLogin.setLayoutData(fd);
		fd.top = new FormAttachment(tPassword, 4 * u);
		fd.left = new FormAttachment(0, 4 * u);
		fd.right = new FormAttachment(100, -4 * u);
		fd.bottom = new FormAttachment(100, -4 * u);
		fd.height = 40;

		bLogin.addListener(SWT.Selection, e -> {
			try {
				login(tName.getText().trim(), tPassword.getText().trim());
			} catch (Exception e1) {
				Layer.message(e1.getMessage(), Layer.ICON_CANCEL);
			}
		});
	}

	private void login(String userName, String password) throws Exception {
		if (userName.isEmpty()) {
			throw new Exception("��������ȷ���û�����");
		}

		User user = null;
		if ("su".equals(userName) && password.equals(ModelLoader.site.getPassword())) {
			user = User.SU();
		} else {
			try {
				user = Services.get(UserService.class).check(userName, password);
			} catch (Exception e) {
			}
		}

		if (user == null) {
			throw new Exception("�޷�ͨ���˻���֤����������ȷ���û��������롣");
		}
		try {
			bruiService.loginUser(user);
		} catch (Exception e) {
			throw new Exception("�޷�ͨ���˻�Ȩ����֤����������ȷ���û��������롣");
		}
		if (bruiService != null) {
			bruiService.closeCurrentPart();
		}

	}

}
