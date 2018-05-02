package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class TypeSelectionPane {

	public TypeSelectionPane(FormField element, ModelEditor editor, Composite parent, String type) {
		editor.createTextField(parent, "唯一标识符:", element, "id", SWT.READ_ONLY);

		String[] fields = null;
		if (type.equals("editor") ) {
			fields = new String[] {
					FormField.TYPE_PAGE, 
					FormField.TYPE_PAGE_HTML, 
					FormField.TYPE_PAGE_NOTE, 
					FormField.TYPE_INLINE, 
					FormField.TYPE_TEXT,
					FormField.TYPE_LABEL,
					FormField.TYPE_TEXT_RANGE,
					FormField.TYPE_TEXT_MULTILINE,
					FormField.TYPE_COMBO, 
					FormField.TYPE_RADIO, 
					FormField.TYPE_CHECK, 
					FormField.TYPE_MULTI_CHECK, 
					FormField.TYPE_DATETIME,
					FormField.TYPE_SELECTION, 
					FormField.TYPE_MULTI_SELECTION, 
					FormField.TYPE_FILE,
					FormField.TYPE_MULTI_FILE 
					};
		} else if (type.equals("query")) {
			fields = new String[] {
					FormField.TYPE_PAGE, 
					FormField.TYPE_INLINE, 
					FormField.TYPE_QUERY_TEXT,
					FormField.TYPE_QUERY_COMBO,
					FormField.TYPE_QUERY_TEXT_RANGE,
					FormField.TYPE_QUERY_CHECK, 
					FormField.TYPE_QUERY_DATETIME,
					FormField.TYPE_QUERY_DATETIME_RANGE,
					FormField.TYPE_QUERY_SELECTION,
					FormField.TYPE_QUERY_MULTI_SELECTION
					};
		} else if (type.equals("info")) {
			fields = new String[] {
					FormField.TYPE_PAGE, 
					FormField.TYPE_PAGE_HTML, 
					FormField.TYPE_PAGE_NOTE, 
					FormField.TYPE_INLINE, 
					FormField.TYPE_LABEL,
					FormField.TYPE_RADIO, 
					FormField.TYPE_CHECK, 
					FormField.TYPE_MULTI_CHECK 
					};
		}

		editor.createComboField(parent, fields, fields, "字段类型:", element, "type", SWT.READ_ONLY | SWT.BORDER);

	}

}
