package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class NumberRangeQueryPane extends TypeSelectionPane {

	public NumberRangeQueryPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent, type);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本（不超过120像素的宽度显示）：", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);
		
		editor.createTextField(parent, "工具提示：", element, "tooltips", SWT.BORDER);

		editor.createTextField(parent, "最小值文本框内显示的信息：", element, "textMessage", SWT.BORDER);

		editor.createTextField(parent, "最大值文本框内显示的信息：", element, "textMessage2", SWT.BORDER);

	}

}
