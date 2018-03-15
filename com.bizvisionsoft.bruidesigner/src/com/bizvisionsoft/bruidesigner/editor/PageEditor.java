package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Page;

public class PageEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ��:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "ҳ������:", inputData, "name", SWT.BORDER);

		createTextField(parent, "ҳ�����:", inputData, "title", SWT.BORDER);

		createTextField(parent, "����:", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "վ����ҳ:", inputData, "home", SWT.CHECK);
		
		createCheckboxField(parent, "��֤�û�:", inputData, "checkLogin", SWT.CHECK);
		
		createCheckboxField(parent, "�����Ƿ��¼����ǿ����֤�û�:", inputData, "forceCheckLogin", SWT.CHECK);
		
		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Page.class;
	}

}
