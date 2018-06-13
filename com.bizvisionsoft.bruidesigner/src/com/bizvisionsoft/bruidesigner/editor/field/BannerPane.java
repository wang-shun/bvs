package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class BannerPane extends TypeSelectionPane {

	public BannerPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���", element, "text", SWT.BORDER|SWT.MULTI);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createIntegerField(parent, "�߶ȣ�Ĭ��64����", element, "height",SWT.BORDER , 0, 2000);

		editor.createCheckboxField(parent, "ֻʹ�þ�̬���ݣ�", element, "staticContent", SWT.CHECK);
	}

}
