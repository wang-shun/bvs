package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;

public class FormFieldPageTypePane extends FormFieldEmptyTypePane{

	public FormFieldPageTypePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent, type);
		
		editor.createTextField(parent, "��ǩҳ����:", element, "name", SWT.BORDER);
		
		editor.createTextField(parent, "��ǩҳ�ı�:", element, "text", SWT.BORDER);

		editor.createTextField(parent, "����:", element, "description", SWT.BORDER);

	}

}
