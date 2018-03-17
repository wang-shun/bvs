package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class FormFieldQueryMultiCheckTypePane extends TypeSelectionPane {

	public FormFieldQueryMultiCheckTypePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createCheckboxField(parent, "���ı�������ʾ��ʾ��", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);

		editor.createComboField(parent,
				new String[] { FormField.RADIO_STYLE_SEGMENT, FormField.RADIO_STYLE_CLASSIC,
						FormField.RADIO_STYLE_VERTICAL },
				new String[] { FormField.RADIO_STYLE_SEGMENT, FormField.RADIO_STYLE_CLASSIC,
						FormField.RADIO_STYLE_VERTICAL },
				"��ʽ", element, "radioStyle", SWT.READ_ONLY | SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("������ʹ�����ַ�ʽ����ѡ�ϵͳ�����µ�˳���ȡѡ�\n1)ֱ���趨ѡ���ı���ѡ��ֵ��\n2)�༭�Ķ�����ע��Ϊ@ProvideOption�ķ��������ԡ�");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		editor.createTextField(parent, "ѡ���ı���#�ָ��", element, "optionText", SWT.BORDER);

		editor.createTextField(parent, "ѡ��ֵ��#�ָĬ�����ı�һ�£���", element, "optionValue", SWT.BORDER);

	}

}
