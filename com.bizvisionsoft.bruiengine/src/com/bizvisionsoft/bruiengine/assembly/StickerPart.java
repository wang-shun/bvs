package com.bizvisionsoft.bruiengine.assembly;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContainer;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;

public class StickerPart {

	@Inject
	IBruiService service;

	private Assembly assembly;

	@GetContainer
	Composite content;

	@Inject
	IBruiContext context;

	public StickerPart(Assembly assembly) {
		this.assembly = assembly;
	}

	@CreateUI
	public void createUI(Composite parent) {
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

		StickerTitlebar bar = UserSession.bruiToolkit().newTitleBar(parent).setText(assembly.getStickerTitle())
				.setActions(assembly.getActions());
		FormData fd = new FormData();
		bar.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.top = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.height = 48;

		content = UserSession.bruiToolkit().newContentPanel(parent);
		fd = new FormData();
		content.setLayoutData(fd);
		fd.left = new FormAttachment(0, 12);
		fd.top = new FormAttachment(bar, 12);
		fd.right = new FormAttachment(100, -12);
		fd.bottom = new FormAttachment(100, -12);

		bar.addListener(SWT.Selection, e -> {
			Action action = ((Action) e.data);
			BruiActionEngine.create(action, service).invokeExecute(e, context);
		});
	}
	
}
