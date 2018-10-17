package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class MultiFilePane extends TypeSelectionPane {

	public MultiFilePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createTextField(parent, "������ʾ��", element, "tooltips", SWT.BORDER);

		editor.createCheckboxField(parent, "����Ϊ�գ�", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "ֻ����", element, "readOnly", SWT.CHECK);
		
		editor.createTextField(parent, "�ϴ��ļ���������ƿռ䣺", element, "fileNamespace", SWT.BORDER);
		
		editor.createIntegerField(parent, "�ϴ��ļ��ĳߴ����ƣ���, 0�����ޣ���", element, "maxFileSize", SWT.BORDER, 0, 1024);

		editor.createIntegerField(parent, "�ϴ���ʱ�������ƣ���, 0�����ޣ���", element, "timeLimit", SWT.BORDER, 0, 120);
		
		editor.createTextField(parent, "�ļ���չ��������������ơ����ŷָ����.jpg,.png����", element, "fileFilerExts", SWT.BORDER);

		editor.createTextField(parent, "д�����������ֶΣ�#�ָ��", element, "reloadFields", SWT.BORDER);

	}

}
