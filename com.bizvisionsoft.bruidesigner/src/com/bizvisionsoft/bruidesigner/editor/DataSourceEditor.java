package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.DataSource;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class DataSourceEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ��:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "����Դ����:", inputData, "name", SWT.BORDER);

		createTextField(parent, "����:", inputData, "description", SWT.BORDER);

		createTextField(parent, "��������:", inputData, "serviceName", SWT.BORDER);
		
		createTextField(parent, "��������:", inputData, "className", SWT.BORDER);

		createCheckboxField(parent, "���ض�������:", inputData, "list", SWT.CHECK);

		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return DataSource.class;
	}
	
	@Override
	protected boolean enableJsonViewer() {
		return false;
	}
	
	@Override
	protected boolean enableParameter() {
		return false;
	}

}
