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

public class AssemblyEditorEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ����", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "������ƣ�", inputData, "name", SWT.BORDER);

		createTextField(parent, "�������:", inputData, "title", SWT.BORDER);

		createTextField(parent, "������", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "խ����ʾΪ��ȱ�׼�ߴ��2/3����", inputData, "smallEditor", SWT.CHECK);
		
		createCheckboxField(parent, "�̣���ʾΪ�߶ȱ�׼�ߴ��2/3����", inputData, "tinyEditor", SWT.CHECK);
		
		createCheckboxField(parent, "ȥ���ֶα߿�", inputData, "removeBorder", SWT.CHECK);
		
		createCheckboxField(parent, "���뵽�������ģ�ѡ�������ֶ����ݼ����ȡ��������ʱ��Ҫѡ�У���", inputData, "addToParentContext", SWT.CHECK);

		createTextField(parent, "����ʱ���ܺ���nullֵ���ֶ�����#�ָ��", inputData, "nullValueAllowedFields", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("������Ⱦ�������ڿ������������ʾ��");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "��Ⱦ�����Ψһ��ʶ����Bundle Id��:", inputData, "gridRenderBundleId", SWT.BORDER);

		createTextField(parent, "��Ⱦ������������:", inputData, "gridRenderClassName", SWT.BORDER);

		parent = createTabItemContent("�ֶ�");
		List<FormField> fields = ((Assembly) inputData).getFields();
		if (fields == null)
			((Assembly) inputData).setFields(fields = new ArrayList<FormField>());
		new FormFieldsEditPane(parent, fields, this, "editor");

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
