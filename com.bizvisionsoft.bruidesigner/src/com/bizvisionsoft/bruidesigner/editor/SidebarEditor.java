package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Sidebar;

public class SidebarEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createCheckboxField(parent, "����:", inputData, "enabled", SWT.CHECK);

		createIntegerField(parent, "��ȣ����أ�:", inputData, "width", SWT.BORDER, 100, 9999);

		createAssemblyField(parent, "���������:", inputData, "header", true);

		addPartNamePropertyChangeListener("enabled");

		parent = createTabItemContent("�˵���");

		List<Action> items = ((Sidebar) inputData).getSidebarItems();
		if (items == null)
			((Sidebar) inputData).setSidebarItems(items = new ArrayList<Action>());
		new ActionsEditPane(parent, items, true, this);

		parent = createTabItemContent("������");

		items = ((Sidebar) inputData).getToolbarItems();
		if (items == null)
			((Sidebar) inputData).setToolbarItems(items = new ArrayList<Action>());
		new ActionsEditPane(parent, items, false, this);

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Sidebar.class;
	}
	
	@Override
	protected boolean enableJsonViewer() {
		return false;
	}

}
