package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class DateTimeRangeQueryPane extends TypeSelectionPane {

	public DateTimeRangeQueryPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本（不超过120像素的宽度显示）：", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);
		
		editor.createTextField(parent, "工具提示：", element, "tooltips", SWT.BORDER);

		editor.createComboField(parent, new String[] { "日期", "日期时间", "时间", "年", "月" },
				new String[] { FormField.DATE_TYPE_DATE, FormField.DATE_TYPE_DATETIME, FormField.DATE_TYPE_TIME,
						FormField.DATE_TYPE_YEAR, FormField.DATE_TYPE_MONTH },
				"日期类型", element, "dateType", SWT.READ_ONLY | SWT.BORDER);

	}

}
