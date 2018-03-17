package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class FormFieldTextTypePane extends FormFieldEmptyTypePane {

	public FormFieldTextTypePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createTextField(parent, "�ı�������ʾ��Ϣ��", element, "textMessage", SWT.BORDER);

		editor.createCheckboxField(parent, "���ı�������ʾ��ʾ��", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);

		editor.createCheckboxField(parent, "��ʾΪ���룺", element, "textPasswordStyle", SWT.CHECK);

		editor.createIntegerField(parent, "�����޶���0�����޶�����", element, "textLimit", SWT.BORDER, 0, 999);

		editor.createComboField(parent,
				new String[] { FormField.TEXT_RESTRICT_INT, FormField.TEXT_RESTRICT_FLOAT,
						FormField.TEXT_RESTRICT_INVALID_CHAR, FormField.TEXT_RESTRICT_NONE },
				new String[] { FormField.TEXT_RESTRICT_INT, FormField.TEXT_RESTRICT_FLOAT,
						FormField.TEXT_RESTRICT_INVALID_CHAR, FormField.TEXT_RESTRICT_NONE },
				"������飺", element, "textRestrict", SWT.READ_ONLY | SWT.BORDER);

		editor.createTextField(parent, "�Զ�������У��(JS�ű�)��", element, "textCustomizedRestrict", SWT.BORDER|SWT.MULTI);
		
		editor.createTextField(parent, "������ʾ��ʽ��", element, "format", SWT.BORDER);

	}

}
