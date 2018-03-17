package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class NumberRangeQueryPane extends TypeSelectionPane {

	public NumberRangeQueryPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent, type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createTextField(parent, "��Сֵ�ı�������ʾ����Ϣ��", element, "textMessage", SWT.BORDER);

		editor.createTextField(parent, "���ֵ�ı�������ʾ����Ϣ��", element, "textMessage2", SWT.BORDER);

	}

}
