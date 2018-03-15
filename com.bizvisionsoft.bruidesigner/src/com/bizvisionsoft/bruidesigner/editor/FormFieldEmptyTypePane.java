package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;

public class FormFieldEmptyTypePane {

	public FormFieldEmptyTypePane(FormField element, ModelEditor editor, Composite parent) {
		editor.createTextField(parent, "唯一标识符:", element, "id", SWT.READ_ONLY);

		editor.createComboField(parent,
				new String[] { FormField.TYPE_PAGE, FormField.TYPE_INLINE, FormField.TYPE_TEXT, FormField.TYPE_COMBO,
						FormField.TYPE_RADIO, FormField.TYPE_CHECK, FormField.TYPE_DATETIME, FormField.TYPE_SELECTION,
						FormField.TYPE_MULTI_SELECTION, FormField.TYPE_FILE, FormField.TYPE_MULTI_FILE },
				new Object[] { FormField.TYPE_PAGE, FormField.TYPE_INLINE, FormField.TYPE_TEXT, FormField.TYPE_COMBO,
						FormField.TYPE_RADIO, FormField.TYPE_CHECK, FormField.TYPE_DATETIME, FormField.TYPE_SELECTION,
						FormField.TYPE_MULTI_SELECTION, FormField.TYPE_FILE, FormField.TYPE_MULTI_FILE },
				"字段类型:", element, "type", SWT.READ_ONLY | SWT.BORDER);

	}

}
