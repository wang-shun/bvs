package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class TextMultiLinePane extends TypeSelectionPane {

	public TextMultiLinePane(FormField element, ModelEditor editor, Composite parent,String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);
		
		editor.createTextField(parent, "������ʾ��", element, "tooltips", SWT.BORDER);

		editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);
		
		editor.createIntegerField(parent, "�����޶���0�����޶�����", element, "textLimit", SWT.BORDER, 0, 999);
		
		editor.createCheckboxField(parent, "�������ʣ��ռ䣺", element, "grabVertical", SWT.CHECK);
		
		editor.createIntegerField(parent, "�������߶ȣ�0����Ĭ�ϣ���", element, "height", SWT.BORDER, 0, 600);
		
		editor.createTextField(parent, "д�����������ֶΣ�#�ָ��", element, "reloadFields", SWT.BORDER);

	}

}
