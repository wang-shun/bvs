package com.bizvisionsoft.pms.assembly;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.bizivisionsoft.widgets.util.WidgetToolkit;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;

public class SidebarHeaderL2Page {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	@CreateUI
	public void createUI(Composite parent) {
		BruiToolkit bruiToolkit = UserSession.bruiToolkit();
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

		// 获得当前进程用户信息中的头像
		String url = bruiService.getResourceURL("/img/left_w.svg");
		pic.setText("<img alt='headpic' style='cursor:pointer;margin-top:8px;' src='" + url + "' width=" + 32
				+ "px height=" + 32 + "px/>");

		Object input = context.getParentContext().getInput();
		Object name = AUtil.readValue(input, null, ReadValue.NAME_LABEL, "");
		Object id = AUtil.readValue(input, null, ReadValue.ID_LABEL, "");
		title.setText("<div style='color:white;margin-left:2px;margin-top:4px;width:180px;'><div style='font-size:16px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;'>" + name
				+ "</div><div style='font-size:14px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;'>" + id + "</div></div>");
		pic.addListener(SWT.MouseDown, e -> {
			WidgetToolkit.execJS("history.back()");
		});
	}
}
