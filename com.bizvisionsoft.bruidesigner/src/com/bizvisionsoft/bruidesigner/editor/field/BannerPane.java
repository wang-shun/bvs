package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class BannerPane extends TypeSelectionPane {

	public BannerPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本：", element, "text", SWT.BORDER|SWT.MULTI);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);

		editor.createIntegerField(parent, "高度（默认64）：", element, "height",SWT.BORDER , 0, 2000);

		editor.createCheckboxField(parent, "只使用静态内容：", element, "staticContent", SWT.CHECK);
	}

}
