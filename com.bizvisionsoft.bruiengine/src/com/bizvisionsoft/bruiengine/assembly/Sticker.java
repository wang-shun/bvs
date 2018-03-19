package com.bizvisionsoft.bruiengine.assembly;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetContainer;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;

public class Sticker {

	@Inject
	private IBruiService service;

	private Assembly assembly;

	@GetContainer
	private Composite content;
	
	@Inject
	private IBruiContext context;

	public Sticker(Assembly assembly) {
		this.assembly = assembly;
	}

	@CreateUI
	public void createUI(Composite parent) {
		parent.setLayout(new FormLayout());

		String cssClass = "";
		if(assembly.isBorderTop()) {
			cssClass += " brui_borderTop";
//			Label label = new Label(parent,SWT.SEPARATOR|SWT.HORIZONTAL);
//			FormData fd = new FormData();
//			label.setLayoutData(fd);
//			fd.top = new FormAttachment();
//			fd.left = new FormAttachment();
//			fd.right = new FormAttachment(100);
//			fd.height =1;
		}
		if(assembly.isBorderRight()) {
			cssClass += " brui_borderRight";
//			Label label = new Label(parent,SWT.SEPARATOR|SWT.VERTICAL);
//			FormData fd = new FormData();
//			label.setLayoutData(fd);
//			fd.bottom = new FormAttachment(100);
//			fd.right = new FormAttachment(100);
//			fd.top = new FormAttachment();
//			fd.width =1;
		}
		if(assembly.isBorderBottom()) {
			cssClass += " brui_borderBottom";
//			Label label = new Label(parent,SWT.SEPARATOR|SWT.HORIZONTAL);
//			FormData fd = new FormData();
//			label.setLayoutData(fd);
//			fd.bottom = new FormAttachment(100);
//			fd.left = new FormAttachment();
//			fd.right = new FormAttachment(100);
//			fd.height =1;
		}
		if(assembly.isBorderLeft()) {
			cssClass += " brui_borderLeft";
//			Label label = new Label(parent,SWT.SEPARATOR|SWT.VERTICAL);
//			FormData fd = new FormData();
//			label.setLayoutData(fd);
//			fd.bottom = new FormAttachment(100);
//			fd.left = new FormAttachment();
//			fd.top = new FormAttachment();
//			fd.width =1;
		}
		
		parent.setHtmlAttribute("class", cssClass);
		
		Titlebar bar = UserSession.bruiToolkit().newTitleBar(parent).setServices(service)
				.setText(assembly.getStickerTitle()).setActions(assembly.getActions());
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
			Action action = ((Action)e.data);
			BruiActionEngine.create(action, service).invokeExecute(e,context);
		});
	}
}
