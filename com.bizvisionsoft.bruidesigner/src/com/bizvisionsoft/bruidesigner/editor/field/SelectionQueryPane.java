package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class SelectionQueryPane extends TypeSelectionPane {

	public SelectionQueryPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "�ֶ����ƣ�", element, "name", SWT.BORDER);

		editor.createTextField(parent, "�ֶ���ʾ�ı���������120���صĿ�����ʾ����", element, "text", SWT.BORDER);

		editor.createTextField(parent, "������", element, "description", SWT.BORDER);

		editor.createAssemblyField(parent, "ѡ�������:", element, "selectorAssemblyId",true);

		editor.createTextField(parent, "ѡ���������ֶ�����", element, "valueFieldName", SWT.BORDER);

	}

}
