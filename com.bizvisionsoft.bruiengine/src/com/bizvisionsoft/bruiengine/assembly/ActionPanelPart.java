package com.bizvisionsoft.bruiengine.assembly;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;
import com.bizvisionsoft.bruiengine.util.Util;

public class ActionPanelPart {

	@Inject
	IBruiService service;

	private Assembly config;

	@Inject
	IBruiContext context;

	private BruiToolkit toolkit;

	public ActionPanelPart(Assembly assembly) {
		this.config = assembly;
	}

	@CreateUI
	public void createUI(Composite parent) {
		toolkit = UserSession.bruiToolkit();
		
		Composite panel;
		if (config.isHasTitlebar()) {
			panel = createSticker(parent);
		} else {
			panel = parent;
		}

		panel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		createContent(panel);
	}
	
	private Composite createSticker(Composite parent) {
		StickerPart sticker = new StickerPart(config);
		sticker.context = context;
		sticker.service = service;
		sticker.createUI(parent);
		return sticker.content;
	}

	private void createContent(Composite parent) {
		GridLayout layout = new GridLayout(config.getActionPanelColumnCount(), true);
		layout.horizontalSpacing = 32;
		layout.verticalSpacing = 32;
		layout.marginHeight = 32;
		layout.marginWidth = 32;
		parent.setLayout(layout);

		String message = config.getMessage();
		if (!Util.isEmptyOrNull(message)) {
			Label label = toolkit.newStyledControl(Label.class, parent, SWT.NONE, BruiToolkit.CSS_TEXT_SUBHEAD);
			toolkit.enableMarkup(label);
			label.setText("<blockquote class=\"layui-elem-quote\">"+message+"</blockquote>");

			GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, config.getActionPanelColumnCount(), 1);
			label.setLayoutData(gd);
		}

		config.getRowActions().forEach(a -> createAction(parent, a));

	}

	private void createAction(Composite parent, Action a) {
		Label btn = createButton(parent, a);
		btn.addListener(SWT.MouseDown, e -> BruiActionEngine.execute(a, e, context, service));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd.widthHint = 256	;
		gd.heightHint = 256;
		btn.setLayoutData(gd);
	}

	public Label createButton(Composite parent, Action a) {
		final Label btn = new Label(parent, SWT.NONE);
		toolkit.enableMarkup(btn);

		btn.addListener(SWT.Resize, e -> {
			Point size = btn.getSize();
			if (size.x == 0 || size.y == 0) {
				return;
			}

			int imageSize = Math.min(size.x, size.y) / 2;
			int marginWidth = (size.x - imageSize) / 2;
			int marginTop = (size.y - (imageSize + 50)) / 2;
			String imageUrl = a.getImage();
			String buttonText = Util.isEmptyOrNull(a.getText()) ? "" : a.getText();

			String text = "";
			if (imageUrl != null) {
				text += "<img alter='" + a.getName() + "' src='" + BruiToolkit.getResourceURL(a.getImage())
						+ "' style='margin-top:" + marginTop + "px;margin-left:" + marginWidth
						+ "px;cursor:pointer;' width='" + imageSize + "px' height='" + imageSize + "px'></img>";
			}
			if (a.isForceText()) {
				text += "<div style='width:" + size.x
						+ "px;text-align:center;font-size:18px;font-weight:lighter;margin-top:8px;'>" + buttonText
						+ "</div>";
			} else {
				text += "<div style='width:" + size.x
						+ "px;text-align:center;font-size:18px;font-weight:lighter;margin-top:8px;'>" + buttonText
						+ "</div>";
			}

			String desc = a.getTooltips();
			if (!Util.isEmptyOrNull(desc)) {
				text += "<div style='width:" + size.x
						+ "px;text-align:center;font-size:13px;font-weight:lighter;margin-top:8px;'>" + desc + "</div>";
			}

			btn.setText(text);

		});

		// String style = a.getStyle();
		// if (style != null && !style.isEmpty()) {
		// btn.setData(RWT.CUSTOM_VARIANT, style);
		// }
		return btn;
	}

}
