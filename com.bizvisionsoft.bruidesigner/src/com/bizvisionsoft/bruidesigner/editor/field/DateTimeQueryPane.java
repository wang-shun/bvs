package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class DateTimeQueryPane extends TypeSelectionPane {

	public DateTimeQueryPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createCheckboxField(parent, "���ı�������ʾ��ʾ��", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);

		editor.createComboField(parent, new String[] { "����", "����ʱ��", "ʱ��", "��", "��" },
				new String[] { FormField.DATE_TYPE_DATE, FormField.DATE_TYPE_DATETIME, FormField.DATE_TYPE_TIME,
						FormField.DATE_TYPE_YEAR, FormField.DATE_TYPE_MONTH },
				"��������", element, "dateType", SWT.READ_ONLY | SWT.BORDER);

	}

}
