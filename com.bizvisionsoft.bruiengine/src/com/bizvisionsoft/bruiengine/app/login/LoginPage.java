package com.bizvisionsoft.bruiengine.app.login;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.carousel.Carousel;
import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Controls;
import com.bizvisionsoft.service.tools.Check;

public class LoginPage {

	@Inject
	private IBruiService bruiService;
	private int barHeight = 48;
	private int barMagin = 12;
	private int panelWidth = 400;
	private int iconSize = 100;
	private Text tName;
	private Text tPassword;
	private Button bRemember;

	@CreateUI
	private void createUI(Composite parent) {
		Controls.handle(parent).layout(new FormLayout());

		// 创建顶栏
		Controls.comp(parent).bg(BruiColor.Blue_Grey_900).put(this::createBar).loc(SWT.LEFT | SWT.RIGHT | SWT.TOP, barHeight).get();

		// 创建背景，如有客户图片采用客户图片
		Check.isAssignedThen(ModelLoader.site.getPageBackgroundImage(), BruiToolkit::getResourceURL).map(url -> {
			Composite bgimg = Controls.comp(parent).bg(BruiColor.white).img(url).margin(-20).mLoc().below(null).get();
			Check.isAssigned(ModelLoader.site.getPageBackgroundImageCSS(), css -> bgimg.setHtmlAttribute("class", css));
			return bgimg;
		}).orElseGet(() -> Controls.comp(parent).css("brui_login_bg").loc(SWT.LEFT | SWT.RIGHT).top(0, barHeight).bottom(33, 0).get());

		// 创建左侧的登录窗口
		Controls.label(parent).img("resource/image/icon_br_bl.svg").top(10, 0).left(25, -iconSize / 2).size(iconSize, iconSize).above(null)
				.add(() -> Controls.label(parent, SWT.CENTER)
						.html("<div style='font-size:28px;font-weight:lighter;color:rgb(74, 74, 74);'>" + parent.getShell().getText()
								+ "</div>")
						.width(panelWidth).left(25, -panelWidth / 2).above(null))
				.add(() -> Controls.comp(parent, SWT.BORDER).bg(BruiColor.white).put(this::createLogin).width(panelWidth)
						.left(25, -panelWidth / 2).above(null));

		createCarousel(parent);

		// 创建底部右侧说明
		Controls.label(parent).html("使用条款  |  许可协议  |  关于我们").mBottom().right(50, -24);

		//
		Controls.label(parent, SWT.CENTER).html("Copyright 2018 BizVision Software Wuhan Ltd.").mBottom().width(panelWidth).left(25,
				-panelWidth / 2);

		// 创建底部左侧说明
		Check.isAssigned(ModelLoader.site.getFootLeftText(), s -> Controls.label(parent).html(s).mBottom().left(0, 24));
	}

	private void createCarousel(Composite parent) {
		if (ModelLoader.site.isDisableNotice())
			return;

		List<Entry<String, String>> pages = new ArrayList<>();
		Check.isAssigned(ModelLoader.site.getNoticeContent1(),
				c -> pages.add(new AbstractMap.SimpleEntry<>(c, ModelLoader.site.getNoticeImg1())));
		Check.isAssigned(ModelLoader.site.getNoticeContent2(),
				c -> pages.add(new AbstractMap.SimpleEntry<>(c, ModelLoader.site.getNoticeImg2())));
		Check.isAssigned(ModelLoader.site.getNoticeContent3(),
				c -> pages.add(new AbstractMap.SimpleEntry<>(c, ModelLoader.site.getNoticeImg3())));

		if (pages.isEmpty())
			return;

		Controls.create(Carousel.class, parent, SWT.NONE)
				.set(t -> t.setAnimation("default").setInterval(10000).setIndicator("none").setArrow("none"))
				.set(c -> fillCarousel(c, pages)).above(null).top(0, barHeight).right().bottom().left(50);
	}

	private void fillCarousel(Carousel caro, List<Entry<String, String>> contents) {
		Rectangle bounds = Display.getCurrent().getBounds();
		int margin = 8;
		int width = bounds.width / 2 - margin*2;
		int height = bounds.height;
		contents.forEach(c -> {
			Composite parent = caro.addPage(new Composite(caro, SWT.NONE));
			parent.setLayout(new FormLayout());

			Check.isAssigned(c.getValue(),
					url -> Controls.comp(parent).loc().img(BruiToolkit.getResourceURL(url), "left bottom", "100% auto"));

			StringBuffer sb = new StringBuffer();
			sb.append("<div style='margin:"+margin+"px;width:" + width + "px;height:" + height + "px'>");
			sb.append(c.getKey());
			sb.append("</div>");
			Controls.label(parent,SWT.WRAP).html(sb.toString()).loc();
		});
	}

	private void createBar(Composite parent) {
		FormLayout layout = new FormLayout();
		layout.marginWidth = 8;
		layout.marginHeight = barMagin;
		parent.setLayout(layout);

		Integer h = ModelLoader.site.getHeadLogoHeight();
		Integer w = ModelLoader.site.getHeadLogoWidth();
		final int h2 = barHeight - 2 * barMagin;
		final int w2;
		if (h != null && w != null) {
			w2 = w * h2 / h;
		} else {
			w2 = 5 * h2 / 2;
		}

		Check.isAssignedThen(ModelLoader.site.getHeadLogo(), BruiToolkit::getResourceURL)//
				.map(url -> Controls.create(Label.class, parent, SWT.NONE)//
						.img(url)//
						.loc(SWT.LEFT | SWT.TOP, w2, h2))//
				.orElseGet(() -> Controls.create(Label.class, parent, SWT.NONE)//
						.img("resource/image/logo_w.svg")//
						.loc(SWT.LEFT | SWT.TOP, 120 * h2 / 15, h2));//

	}

	private void createLogin(Composite parent) {
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
				.html("<div class='label_button' style='font-size:13px;'>登录</div>").select(this::login)//
				.add(0, () -> Controls.button(parent, SWT.CHECK).loc(SWT.LEFT | SWT.RIGHT).setText("在本机记录我的登录状态")).get();

	}

	private void login(Event event) {
		String userName = tName.getText().trim();
		String password = tPassword.getText().trim();

		try {
			bruiService.checkLogin(userName, password);
			if (bRemember.getSelection())
				bruiService.saveClientLogin(userName, password);
			bruiService.closeCurrentPart();
		} catch (Exception e) {
			Layer.error(e);
		}

	}

}
