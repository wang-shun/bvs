package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;

public class FormFieldPageTypePane extends FormFieldEmptyTypePane{

	public FormFieldPageTypePane(FormField element, ModelEditor editor, Composite parent) {
		super(element, editor, parent);
		
		editor.createTextField(parent, "标签页名称:", element, "name", SWT.BORDER);
		
		editor.createTextField(parent, "标签页文本:", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述:", element, "description", SWT.BORDER);

	}

}
