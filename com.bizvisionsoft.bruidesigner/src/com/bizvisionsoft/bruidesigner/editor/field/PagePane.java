package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class PagePane extends TypeSelectionPane{

	public PagePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent, type);
		
		editor.createTextField(parent, "��ǩҳ����:", element, "name", SWT.BORDER);
		
		editor.createTextField(parent, "��ǩҳ�ı�:", element, "text", SWT.BORDER);

		editor.createTextField(parent, "����:", element, "description", SWT.BORDER);

	}

}
