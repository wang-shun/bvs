package com.bizvisionsoft.bruiengine.app.buzman;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;

public class SidebarHeader {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	@CreateUI
	public void createUI(Composite parent) {
		BruiToolkit bruiToolkit = UserSession.bruiToolkit();
		parent.setHtmlAttribute("class", "gradient-orange");

		int size = 48;
		parent.setLayout(new FormLayout());
		Label pic = new Label(parent, SWT.NONE);
		bruiToolkit.enableMarkup(pic);
		Label title = new Label(parent, SWT.NONE);
		bruiToolkit.enableMarkup(title);
		FormData fd = new FormData(size, size);
		pic.setLayoutData(fd);
		fd.left = new FormAttachment();
		fd.top = new FormAttachment();
		fd = new FormData();
		title.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(pic);
		fd.height = size;
		fd.right = new FormAttachment(100);

		String url = bruiService.getResourceURL("/img/left_w.svg");
		pic.setText("<img alt='headpic' style='cursor:pointer;margin-top:8px;' src='" + url + "' width=" + 32
				+ "px height=" + 32 + "px/>");

		title.setText(
				"<div style='font-size:18px;font-weight:lighter;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;color:white;margin-left:2px;margin-top:12px;width:180px;'>业务和基础数据管理</div>");
		pic.addListener(SWT.MouseDown, e -> {
			WidgetToolkit.execJS("history.back()");
		});
	}
}
