package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Sidebar;

public class SidebarEditor extends ModelEditor {


	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createCheckboxField(parent, "启用:", inputData, "enabled", SWT.CHECK);

		createIntegerField(parent, "宽度（像素）:", inputData, "width", SWT.BORDER,100,9999);

		createAssemblyField(parent, "标题区组件:", inputData, "header",true);

		addPartNamePropertyChangeListener("enabled");

		parent = createTabItemContent("菜单项");

		new ActionsEditPane(parent, ((Sidebar) inputData).getSidebarItems(),true,this);
		
		parent = createTabItemContent("工具栏");

		new ActionsEditPane(parent, ((Sidebar) inputData).getToolbarItems(),false,this);

	}


	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Sidebar.class;
	}

}
