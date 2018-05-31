package com.bizvisionsoft.bruiengine.assembly;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
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

		parent.setLayout(new FormLayout());

		String message = Optional.ofNullable(config.getMessage()).orElse("");
		Label label = toolkit.newStyledControl(Label.class, parent, SWT.NONE, BruiToolkit.CSS_TEXT_SUBHEAD);
		toolkit.enableMarkup(label);
		label.setText("<blockquote class=\"layui-elem-quote\">" + message + "</blockquote>");

		FormData fd = new FormData();
		label.setLayoutData(fd);
		fd.left = new FormAttachment(0, 32);
		fd.top = new FormAttachment(0, 32);
		fd.right = new FormAttachment(100, -32);

		Composite panel = new Composite(parent,SWT.NONE);
		fd = new FormData();
		panel.setLayoutData(fd);
		fd.left = new FormAttachment(0, 32);
		fd.top = new FormAttachment(label, 32);
		fd.right = new FormAttachment(100, -32);
		fd.bottom = new FormAttachment(100,-32);
		
		RowLayout layout = new RowLayout();
		layout.spacing = 32;
		panel.setLayout(layout);
		config.getRowActions().forEach(a -> createAction(panel, a));

	}

	private void createAction(Composite parent, Action a) {
		Label btn = createButton(parent, a);
		btn.addListener(SWT.MouseDown, e -> BruiActionEngine.execute(a, e, context, service));
		btn.setLayoutData(new RowData(128	, 128));
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
						+ "px;text-align:center;font-size:16px;margin-top:8px;'>" + buttonText
						+ "</div>";
			} else {
				text += "<div style='width:" + size.x
						+ "px;text-align:center;font-size:16px;margin-top:8px;'>" + buttonText
						+ "</div>";
			}

			String desc = a.getTooltips();
			if (!Util.isEmptyOrNull(desc)) {
				text += "<div style='width:" + size.x
						+ "px;text-align:center;font-size:14px;margin-top:8px;'>" + desc + "</div>";
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
