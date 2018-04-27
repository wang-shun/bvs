package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class CheckPane extends TypeSelectionPane {

	public CheckPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent, type);

		if ("editor".equals(type)) {
			editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

			editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

			editor.createTextField(parent, "������", element, "description", SWT.BORDER);

			editor.createCheckboxField(parent, "���ı�������ʾ��ʾ��", element, "hasInfoLabel", SWT.CHECK);

			editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

			editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);

			editor.createComboField(parent,
					new String[] { FormField.CHECK_STYLE_SWITCH, FormField.CHECK_STYLE_CLASSIC },
					new String[] { FormField.CHECK_STYLE_SWITCH, FormField.CHECK_STYLE_CLASSIC }, "��ʽ", element,
					"checkStyle", SWT.READ_ONLY | SWT.BORDER);
		}else if("info".equals(type)) {
			editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

			editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

			editor.createTextField(parent, "������", element, "description", SWT.BORDER);
			
			editor.createComboField(parent,
					new String[] { FormField.CHECK_STYLE_SWITCH, FormField.CHECK_STYLE_CLASSIC },
					new String[] { FormField.CHECK_STYLE_SWITCH, FormField.CHECK_STYLE_CLASSIC }, "��ʽ", element,
					"checkStyle", SWT.READ_ONLY | SWT.BORDER);
		}

	}

}
