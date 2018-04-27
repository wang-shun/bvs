package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.editor.field.FormFieldsEditPane;

public class AssemblyInfoPadEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ����", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "������ƣ�", inputData, "name", SWT.BORDER);

		createTextField(parent, "�������:", inputData, "title", SWT.BORDER);

		createTextField(parent, "������", inputData, "description", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("������Ⱦ�������ڿ������������ʾ��");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "��Ⱦ�����Ψһ��ʶ����Bundle Id��:", inputData, "gridRenderBundleId", SWT.BORDER);

		createTextField(parent, "��Ⱦ������������:", inputData, "gridRenderClassName", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		l = new Label(parent, SWT.NONE);
		l.setText("�Զ������ȡ����������ʹ�ò����ѡ����÷���");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "ȡ�����Ψһ��ʶ����Bundle Id��:", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "ȡ������������:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "ȡ����������:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("���磺UserService.list");
		
		
		parent = createTabItemContent("��������");
		
		createCheckboxField(parent, "���ж����ı������͹�������", inputData, "hasTitlebar", SWT.CHECK);

		createTextField(parent, "�������:", inputData, "stickerTitle", SWT.BORDER);

		createCheckboxField(parent, "�Ƿ��ڱ���������ʾ����������ƣ�", inputData, "displayInputLabelInTitlebar", SWT.CHECK);
		createCheckboxField(parent, "�Ƿ��ڱ���������ʾ�������Ĵ���������ƣ�", inputData, "displayRootInputLabelInTitlebar", SWT.CHECK);

		createCheckboxField(parent, "�����ϱ߿�", inputData, "borderTop", SWT.CHECK);
		
		createCheckboxField(parent, "�����ұ߿�", inputData, "borderRight", SWT.CHECK);
		
		createCheckboxField(parent, "�����±߿�", inputData, "borderBottom", SWT.CHECK);
		
		createCheckboxField(parent, "������߿�", inputData, "borderLeft", SWT.CHECK);
		

		parent = createTabItemContent("�ֶ�");
		List<FormField> fields = ((Assembly) inputData).getFields();
		if (fields == null)
			((Assembly) inputData).setFields(fields = new ArrayList<FormField>());
		new FormFieldsEditPane(parent, fields, this, "info");

		// parent = createTabItemContent("����");
		// List<Action> actions = ((Assembly) inputData).getActions();
		// if (actions == null)
		// ((Assembly) inputData).setActions(actions = new ArrayList<Action>());
		// new ActionsEditPane(parent, actions, true, this);

		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
