package com.bizvisionsoft.bruiengine.assembly;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.annotations.AUtil;
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

	private Assembly assembly;

	@Inject
	IBruiContext context;

	private BruiToolkit toolkit;

	public ActionPanelPart(Assembly assembly) {
		this.assembly = assembly;
	}

	@CreateUI
	public void createUI(Composite parent) {
		toolkit = UserSession.bruiToolkit();

		parent.setLayout(new FormLayout());

		String cssClass = "";
		if (assembly.isBorderTop()) {
			cssClass += " brui_borderTop";
		}
		if (assembly.isBorderRight()) {
			cssClass += " brui_borderRight";
		}
		if (assembly.isBorderBottom()) {
			cssClass += " brui_borderBottom";
		}
		if (assembly.isBorderLeft()) {
			cssClass += " brui_borderLeft";
		}

		parent.setHtmlAttribute("class", cssClass);

		String text = assembly.getStickerTitle();
		if (assembly.isDisplayInputLabelInTitlebar()) {
			text += Optional.ofNullable(context.getInput()).map(o -> AUtil.readLabel(o, "")).map(l -> " - " + l)
					.orElse("");
		} else if (assembly.isDisplayRootInputLabelInTitlebar()) {
			text += Optional.ofNullable(context.getRootInput()).map(o -> AUtil.readLabel(o, "")).map(l -> " - " + l)
					.orElse("");
		}

		Action closeAction = null;
		if (context.isCloseable()) {
			closeAction = new Action();
			closeAction.setName("close");
			closeAction.setImage("/img/left.svg");
		}
		StickerTitlebar bar = new StickerTitlebar(parent, closeAction, null);
		bar.setText(text);
		FormData fd = new FormData();
		bar.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.top = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.height = 48;

		Composite content = UserSession.bruiToolkit().newContentPanel(parent);
		fd = new FormData();
		content.setLayoutData(fd);
		fd.left = new FormAttachment(0, 12);
		fd.top = new FormAttachment(bar, 12);
		fd.right = new FormAttachment(100, -12);
		fd.bottom = new FormAttachment(100, -12);

		bar.addListener(SWT.Selection, e -> {
			Action action = ((Action) e.data);
			if ("close".equals(action.getName())) {
				service.closeCurrentContent();
			} else {
				toolkit.runAction(action, e, service, context);
			}
		});

		createContent(content);
	}

	private void createContent(Composite parent) {
		GridLayout layout = new GridLayout(assembly.getActionPanelColumnCount(), true);
		layout.horizontalSpacing = 32;
		layout.verticalSpacing = 32;
		layout.marginHeight = 32;
		layout.marginWidth = 128;
		parent.setLayout(layout);

		String message = assembly.getMessage();
		if (!Util.isEmptyOrNull(message)) {
			Label label = toolkit.newStyledControl(Label.class, parent, SWT.NONE, BruiToolkit.CSS_TEXT_HEADLINE);
			label.setText(message);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, assembly.getActionPanelColumnCount(), 1);
			label.setLayoutData(gd);
		}

		assembly.getActions().forEach(a -> createAction(parent, a));

	}

	private void createAction(Composite parent, Action a) {
		Label btn = createButton(parent, a);
		btn.addListener(SWT.MouseDown, e -> BruiActionEngine.execute(a, e, context, service));
		btn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
