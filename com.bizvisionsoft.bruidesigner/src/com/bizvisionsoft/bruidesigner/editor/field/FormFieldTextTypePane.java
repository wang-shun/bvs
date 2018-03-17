package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class FormFieldTextTypePane extends FormFieldEmptyTypePane {

	public FormFieldTextTypePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本（不超过120像素的宽度显示）：", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);

		editor.createTextField(parent, "文本框内提示信息：", element, "textMessage", SWT.BORDER);

		editor.createCheckboxField(parent, "在文本框外显示提示：", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "不可为空：", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "只读：", element, "readOnly", SWT.CHECK);

		editor.createCheckboxField(parent, "显示为密码：", element, "textPasswordStyle", SWT.CHECK);

		editor.createIntegerField(parent, "字数限定（0代表不限定）：", element, "textLimit", SWT.BORDER, 0, 999);

		editor.createComboField(parent,
				new String[] { FormField.TEXT_RESTRICT_INT, FormField.TEXT_RESTRICT_FLOAT,
						FormField.TEXT_RESTRICT_INVALID_CHAR, FormField.TEXT_RESTRICT_NONE },
				new String[] { FormField.TEXT_RESTRICT_INT, FormField.TEXT_RESTRICT_FLOAT,
						FormField.TEXT_RESTRICT_INVALID_CHAR, FormField.TEXT_RESTRICT_NONE },
				"输入检验：", element, "textRestrict", SWT.READ_ONLY | SWT.BORDER);

		editor.createTextField(parent, "自定义输入校验(JS脚本)：", element, "textCustomizedRestrict", SWT.BORDER|SWT.MULTI);
		
		editor.createTextField(parent, "数据显示格式：", element, "format", SWT.BORDER);

	}

}
