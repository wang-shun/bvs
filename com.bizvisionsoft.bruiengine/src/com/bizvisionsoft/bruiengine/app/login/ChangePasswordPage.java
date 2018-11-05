package com.bizvisionsoft.bruiengine.app.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Controls;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.datatools.FilterAndUpdate;
import com.bizvisionsoft.service.model.UserPassword;
import com.bizvisionsoft.serviceconsumer.Services;
import com.mongodb.BasicDBObject;

public class ChangePasswordPage extends SiteHomePage {

	@Inject
	private IBruiService bruiService;
	private Text tPassword1;
	private Text tPassword2;

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

		tPassword1 = Controls.label(parent).html("<div class='label_button' style='color:rgb(74, 74, 74);'>œµÕ≥’À∫≈</div>")
				.loc(SWT.LEFT | SWT.RIGHT | SWT.TOP)
				.add(0, () -> Controls.text(parent, SWT.BORDER | SWT.PASSWORD).loc(SWT.LEFT | SWT.RIGHT)).get();

		tPassword2 = Controls.label(parent).html("<div class='label_button' style='color:rgb(74, 74, 74);'>√‹¬Î</div>")
				.loc(SWT.LEFT | SWT.RIGHT).top(tPassword1, 12)
				.add(0, () -> Controls.text(parent, SWT.BORDER | SWT.PASSWORD).loc(SWT.LEFT | SWT.RIGHT)).get();

		Controls.button(parent).rwt(BruiToolkit.CSS_INFO).loc(SWT.LEFT | SWT.RIGHT).top(tPassword2, 12)
				.html("<div class='label_button' style='font-size:13px;'>–ﬁ∏ƒ√‹¬Î</div>").defaultButton().select(this::ok);

	}

	private void ok(Event event) {
		String password1 = tPassword1.getText().trim();
		String password2 = tPassword2.getText().trim();

		UserPassword up = new UserPassword();
		up.password = password1;
		try {
			up.setPassword2(password2);
			FilterAndUpdate filterAndUpdate = new FilterAndUpdate().filter(new BasicDBObject("userId", bruiService.getCurrentUserId()))
					.set(new BasicDBObject("password", password1).append("changePSW", false));
			Services.get(UserService.class).update(filterAndUpdate.bson());
			bruiService.closeCurrentPart();
		} catch (Exception e) {
			Layer.error(e);
		}

	}

}
