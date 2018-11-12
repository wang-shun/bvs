package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.editor.pane.LayoutEditPane;

public class AssemblyBoardEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ����", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "������ƣ�", inputData, "name", SWT.BORDER);

		createTextField(parent, "�������:", inputData, "title", SWT.BORDER);

		createTextField(parent, "������", inputData, "description", SWT.BORDER);
		
		parent = createTabItemContent("���ֺ������");
		List<Layout> layouts = ((Assembly) inputData).getLayout();
		if (layouts == null)
			((Assembly) inputData).setLayout(layouts = new ArrayList<Layout>());

		new LayoutEditPane(parent, layouts, this);
		
		
		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}
	
	@Override
	protected boolean enableCustomized() {
		return true;
	}
}