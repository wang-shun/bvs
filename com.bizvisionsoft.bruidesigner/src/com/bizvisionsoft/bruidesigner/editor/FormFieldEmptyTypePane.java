package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;

public class FormFieldEmptyTypePane {

	public FormFieldEmptyTypePane(FormField element, ModelEditor editor, Composite parent, String type) {
		editor.createTextField(parent, "唯一标识符:", element, "id", SWT.READ_ONLY);

		String[] fields = null;
		if (type.equals("edit")) {
			fields = new String[] {
					FormField.TYPE_PAGE, 
					FormField.TYPE_INLINE, 
					FormField.TYPE_TEXT,
					FormField.TYPE_TEXT_RANGE,//TODO
					FormField.TYPE_TEXT_MULTILINE,//TODO
					FormField.TYPE_COMBO, 
					FormField.TYPE_RADIO, 
					FormField.TYPE_CHECK, 
					FormField.TYPE_MULTI_CHECK, //TODO
					FormField.TYPE_DATETIME,
					FormField.TYPE_DATETIME_RANGE,//TODO
					FormField.TYPE_SELECTION, 
					FormField.TYPE_MULTI_SELECTION, 
					FormField.TYPE_FILE,
					FormField.TYPE_MULTI_FILE 
					};
		} else if (type.equals("query")) {
			fields = new String[] {
					FormField.TYPE_PAGE, 
					FormField.TYPE_INLINE, 
					FormField.TYPE_QUERY_TEXT,//TODO
					FormField.TYPE_TEXT_RANGE,
					FormField.TYPE_COMBO, 
					FormField.TYPE_RADIO, 
					FormField.TYPE_QUERY_CHECK, //TODO
					FormField.TYPE_QUERY_MULTI_CHECK, //TODO
					FormField.TYPE_DATETIME,
					FormField.TYPE_DATETIME_RANGE,//TODO
					FormField.TYPE_SELECTION, 
					FormField.TYPE_QUERY_MULTI_SELECTION//TODO
					};
		}

		editor.createComboField(parent, fields, fields, "字段类型:", element, "type", SWT.READ_ONLY | SWT.BORDER);

	}

}
