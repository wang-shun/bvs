package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class MultiSelectionPane extends TypeSelectionPane {

	public MultiSelectionPane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本（不超过120像素的宽度显示）：", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);

		editor.createTextField(parent, "工具提示：", element, "tooltips", SWT.BORDER);

		editor.createCheckboxField(parent, "不可为空：", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "只读：", element, "readOnly", SWT.CHECK);
		
		editor.createAssemblyField(parent, "选择器组件:", element, "selectorAssemblyId",true);
		
		editor.createTextField(parent, "写入后更新其他字段（#分割）：", element, "reloadFields", SWT.BORDER);

	}

}
