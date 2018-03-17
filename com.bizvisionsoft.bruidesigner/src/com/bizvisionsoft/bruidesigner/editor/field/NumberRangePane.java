package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class NumberRangePane extends TypeSelectionPane {

	public NumberRangePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent, type);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本（不超过120像素的宽度显示）：", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);

		editor.createCheckboxField(parent, "在文本框外显示提示：", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "不可为空：", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "只读：", element, "readOnly", SWT.CHECK);

		editor.createCheckboxField(parent, "限定最小值：", element, "textRangeLimitMinValue", SWT.CHECK);

		editor.createIntegerField(parent, "最小值：", element, "textRangeMinValue", SWT.BORDER, Integer.MIN_VALUE,
				Integer.MAX_VALUE);

		editor.createTextField(parent, "最小值文本框内显示的信息：", element, "textMessage", SWT.BORDER);

		editor.createCheckboxField(parent, "限定最大值：", element, "textRangeLimitMaxValue", SWT.CHECK);

		editor.createIntegerField(parent, "最大值：", element, "textRangeMaxValue", SWT.BORDER, Integer.MIN_VALUE,
				Integer.MAX_VALUE);

		editor.createTextField(parent, "最大值文本框内显示的信息：", element, "textMessage2", SWT.BORDER);

	}

}
