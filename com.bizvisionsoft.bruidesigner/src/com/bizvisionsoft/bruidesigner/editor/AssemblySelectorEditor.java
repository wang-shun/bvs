package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblySelectorEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ����", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "������ƣ�", inputData, "name", SWT.BORDER);
		
		createTextField(parent, "������", inputData, "description", SWT.BORDER);
		
		createAssemblyField(parent, "������:", inputData, "selectorGridAssemblyId", true);

		createCheckboxField(parent, "���ض�����¼", inputData, "selectorMultiSelection", SWT.CHECK);
		
		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
