package com.bizvisionsoft.bruiengine.app.login;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Controls;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Check;
import com.bizvisionsoft.serviceconsumer.Services;

public class LoginPage {

	@Inject
	private IBruiService bruiService;
	private int barHeight = 48;
	private int barMagin = 8;
	private int loginPanelWidth = 360;

	@CreateUI
	private void createUI(Composite parent) {
		Controls.handle(parent).layout(new FormLayout());

		Controls.create(Composite.class, parent, SWT.NONE)//
				.background(BruiColor.Blue_Grey_900)//
				.put(this::createBar)//
				.height(barHeight).left().right().top().get();

		boolean custBg = Check.isAssignedThen(ModelLoader.site.getPageBackgroundImage(), BruiToolkit::getResourceURL)
				.map(url -> {
					Composite bgimg = Controls.create(Composite.class, parent, SWT.NONE).background(BruiColor.white)//
							.setImage(url).margin(-20).mLoc().below(null).get();
					Check.isAssigned(ModelLoader.site.getPageBackgroundImageCSS(),
							css -> bgimg.setHtmlAttribute("class", css));
					return true;
				}).orElse(false);

		if (!custBg) {
			Controls.create(Composite.class, parent, SWT.NONE)//
					.cssStyle("brui_login_bg")//
					.left().right().top(0, barHeight).bottom(33, 0);
		}

		Composite login = Controls.create(Composite.class, parent, SWT.BORDER)//
				.background(BruiColor.white)//
				.put(this::createLogin)//
				.width(loginPanelWidth).left(50, -loginPanelWidth / 2).centerV().above(null)//
				.get();

		Label logo = Controls.create(Label.class, parent, SWT.NONE).setImage("resource/image/icon_br_bl.svg")
				.bottom(login, -75).left(50, -60).size(120, 120).above(null).get();

		String title = parent.getShell().getText();
		Controls.create(Label.class, parent, SWT.CENTER)
				.setHTML("<div style='font-size:28px;font-weight:lighter;color:rgb(74, 74, 74);'>" + title + "</div>")
				.width(loginPanelWidth).left(50, -loginPanelWidth / 2).top(logo, 8).above(null);

		Controls.create(Label.class, parent, SWT.NONE).setText("系统状态  |  使用条款  |  许可协议  |  关于我们").mBottom().mLeft();
	}

	private void createBar(Composite parent) {
		FormLayout layout = new FormLayout();
		layout.marginWidth = 8;
		layout.marginHeight = barMagin;
		parent.setLayout(layout);

		int h2 = barHeight - 2 * barMagin;
		int left = Check.isAssignedThen(ModelLoader.site.getHeadLogo(), BruiToolkit::getResourceURL).map(url -> {
			Integer h = ModelLoader.site.getHeadLogoHeight();
			Integer w = ModelLoader.site.getHeadLogoWidth();
			if (h != null && w != null) {
				w = w * h2 / h;
			} else {
				w = 5 * h2 / 2;
			}
			Controls.create(Label.class, parent, SWT.NONE).setImage(url).width(w).height(h2).left().top();
			return w + 16;
		}).orElse(0);

		// 客户名称
		Check.isAssigned(ModelLoader.site.getFootLeftText(), s -> {
			Controls.create(Label.class, parent, SWT.NONE)
					.setHTML("<div style='font-size:25px;font-weight:lighter;color:#fff;'>" + s + "</div>")
					.left(0, left).top();
		});

		// 标准logo125x32
		Controls.create(Label.class, parent, SWT.NONE).setImage("resource/image/logo_w.svg").width(125 * h2 / 32)
				.height(h2).right().top();

	}

	private void createLogin(Composite parent) {
		FormLayout layout = new FormLayout();
		layout.marginWidth = 36;
		layout.marginHeight = 36;
		layout.spacing = 8;
		parent.setLayout(layout);

		Label label = Controls.create(Label.class, parent, SWT.NONE)
				.setHTML("<div class='label_button' style='color:rgb(74, 74, 74);'>系统账号</div>").left().top().right()
				.get();

		final Text tName = Controls.create(Text.class, parent, SWT.BORDER).left().top(label).right().get();

		label = Controls.create(Label.class, parent, SWT.NONE)
				.setHTML("<div class='label_button' style='color:rgb(74, 74, 74);'>密码</div>").left().top(tName, 16)
				.right().get();

		final Text tPassword = Controls.create(Text.class, parent, SWT.BORDER | SWT.PASSWORD).left().top(label).right()
				.get();

		Button bLogin = Controls.create(Button.class, parent, SWT.PUSH, BruiToolkit.CSS_INFO, null).left().right()
				.top(tPassword, 32).setHTML("<div class='label_button' style='font-size:13px;'>登录</div>").get();

		bLogin.addListener(SWT.Selection, e -> {
			try {
				login(tName.getText().trim(), tPassword.getText().trim());
			} catch (Exception e1) {
				Layer.message(e1.getMessage(), Layer.ICON_LOCK);
			}
		});
	}

	private void login(String userName, String password) throws Exception {
		if (userName.isEmpty()) {
			throw new Exception("请输入正确的用户名。");
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
			throw new Exception("无法通过账户验证，请输入正确的用户名和密码。");
		}
		bruiService.loginUser(user);
		if (bruiService != null) {
			bruiService.closeCurrentPart();
		}

	}

}
