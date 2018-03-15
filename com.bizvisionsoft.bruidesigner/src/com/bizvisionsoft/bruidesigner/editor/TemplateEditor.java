package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Template;

public class TemplateEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("������Ϣ");
		
		createTextField(parent, "Ψһ��ʶ��:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "ģ������:", inputData, "name", SWT.BORDER);

		createTextField(parent, "����:", inputData, "description", SWT.BORDER);

		createTextField(parent, "���Ψһ��ʶ����Bundle Id��:", inputData, "bundleId", SWT.BORDER);

		createTextField(parent, "����������:", inputData, "className", SWT.BORDER);

		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Template.class;
	}

}
