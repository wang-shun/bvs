package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Sidebar;

public class SidebarEditor extends ModelEditor {


	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createCheckboxField(parent, "����:", inputData, "enabled", SWT.CHECK);

		createIntegerField(parent, "��ȣ����أ�:", inputData, "width", SWT.BORDER,100,9999);

		createAssemblyField(parent, "���������:", inputData, "header",true);

		addPartNamePropertyChangeListener("enabled");

		parent = createTabItemContent("�˵���");

		new ActionsEditPane(parent, ((Sidebar) inputData).getSidebarItems(),true,this);
		
		parent = createTabItemContent("������");

		new ActionsEditPane(parent, ((Sidebar) inputData).getToolbarItems(),false,this);

	}


	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Sidebar.class;
	}

}
