package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblyLinkEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("������Ϣ");

		createAssemblyField(parent, "���:", inputData, "id",false);

		createCheckboxField(parent, "Ĭ��:", inputData, "defaultAssembly", SWT.CHECK);
		
		createPathField(parent, "ͼ��URL:", inputData, "image", SWT.BORDER);

		createTextField(parent, "�ı���ǩ:", inputData, "text", SWT.BORDER);
		
		createTextField(parent, "��ɫ�����#�ָ", inputData, "role", SWT.BORDER);
		
		createTextField(parent, "�ų���ɫ�����#�ָ", inputData, "excludeRole", SWT.BORDER);

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return AssemblyLink.class;
	}


	

}
