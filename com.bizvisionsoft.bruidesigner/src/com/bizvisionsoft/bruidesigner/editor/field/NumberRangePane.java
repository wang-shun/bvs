package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class NumberRangePane extends TypeSelectionPane {

	public NumberRangePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent, type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createCheckboxField(parent, "���ı�������ʾ��ʾ��", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);

		editor.createCheckboxField(parent, "�޶���Сֵ��", element, "textRangeLimitMinValue", SWT.CHECK);

		editor.createIntegerField(parent, "��Сֵ��", element, "textRangeMinValue", SWT.BORDER, Integer.MIN_VALUE,
				Integer.MAX_VALUE);

		editor.createTextField(parent, "��Сֵ�ı�������ʾ����Ϣ��", element, "textMessage", SWT.BORDER);

		editor.createCheckboxField(parent, "�޶����ֵ��", element, "textRangeLimitMaxValue", SWT.CHECK);

		editor.createIntegerField(parent, "���ֵ��", element, "textRangeMaxValue", SWT.BORDER, Integer.MIN_VALUE,
				Integer.MAX_VALUE);

		editor.createTextField(parent, "���ֵ�ı�������ʾ����Ϣ��", element, "textMessage2", SWT.BORDER);

	}

}
