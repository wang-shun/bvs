package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.FormField;

public class FormFieldComboTypePane extends FormFieldEmptyTypePane {

	public FormFieldComboTypePane(FormField element, ModelEditor editor, Composite parent) {
		super(element, editor, parent);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createCheckboxField(parent, "���ı�������ʾ��ʾ��", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);
		
		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("������ʹ�����ַ�ʽ����ѡ�ϵͳ�����µ�˳���ȡѡ�\n1)ֱ���趨ѡ���ı���ѡ��ֵ��\n2)�༭�Ķ�����ע��Ϊ@ProvideOption�ķ��������ԡ�");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		editor.createTextField(parent, "ѡ���ı���#�ָ��", element, "optionText", SWT.BORDER);

		editor.createTextField(parent, "ѡ��ֵ��#�ָĬ�����ı�һ�£���", element, "optionValue", SWT.BORDER);

	}

}
