package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;

public class FormFieldMultiFileTypePane extends FormFieldEmptyTypePane {

	public FormFieldMultiFileTypePane(FormField element, ModelEditor editor, Composite parent) {
		super(element, editor, parent);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本（不超过120像素的宽度显示）：", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);

		editor.createCheckboxField(parent, "在文本框外显示提示：", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "不可为空：", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "只读：", element, "readOnly", SWT.CHECK);
		
		editor.createTextField(parent, "上传文件保存的名称空间：", element, "fileNamespace", SWT.BORDER);
		
		editor.createIntegerField(parent, "上传文件的尺寸限制（兆, 0代表不限）：", element, "maxFileSize", SWT.BORDER, 0, 1024);

		editor.createIntegerField(parent, "上传超时设置限制（秒, 0代表不限）：", element, "timeLimit", SWT.BORDER, 0, 120);
		
		editor.createTextField(parent, "文件扩展名（不填代表不限制。逗号分隔多个.jpg,.png）：", element, "fileFilerExts", SWT.BORDER);

	}

}
