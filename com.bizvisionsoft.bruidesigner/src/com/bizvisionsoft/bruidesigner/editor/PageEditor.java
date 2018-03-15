package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Page;

public class PageEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "页面名称:", inputData, "name", SWT.BORDER);

		createTextField(parent, "页面标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "描述:", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "站点首页:", inputData, "home", SWT.CHECK);
		
		createCheckboxField(parent, "验证用户:", inputData, "checkLogin", SWT.CHECK);
		
		createCheckboxField(parent, "无论是否登录都需强制验证用户:", inputData, "forceCheckLogin", SWT.CHECK);
		
		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Page.class;
	}

}
