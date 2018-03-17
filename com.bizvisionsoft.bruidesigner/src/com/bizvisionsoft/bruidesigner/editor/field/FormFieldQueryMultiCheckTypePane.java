package com.bizvisionsoft.bruidesigner.editor.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruidesigner.editor.ModelEditor;

public class FormFieldQueryMultiCheckTypePane extends TypeSelectionPane {

	public FormFieldQueryMultiCheckTypePane(FormField element, ModelEditor editor, Composite parent, String type) {
		super(element, editor, parent,type);

		editor.createTextField(parent, "字段名称：", element, "name", SWT.BORDER);

		editor.createTextField(parent, "字段显示文本（不超过120像素的宽度显示）：", element, "text", SWT.BORDER);

		editor.createTextField(parent, "描述：", element, "description", SWT.BORDER);

		editor.createCheckboxField(parent, "在文本框外显示提示：", element, "hasInfoLabel", SWT.CHECK);

		editor.createCheckboxField(parent, "不可为空：", element, "required", SWT.CHECK);

		editor.createCheckboxField(parent, "只读：", element, "readOnly", SWT.CHECK);

		editor.createComboField(parent,
				new String[] { FormField.RADIO_STYLE_SEGMENT, FormField.RADIO_STYLE_CLASSIC,
						FormField.RADIO_STYLE_VERTICAL },
				new String[] { FormField.RADIO_STYLE_SEGMENT, FormField.RADIO_STYLE_CLASSIC,
						FormField.RADIO_STYLE_VERTICAL },
				"样式", element, "radioStyle", SWT.READ_ONLY | SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("您可以使用两种方式定义选项，系统按以下的顺序获取选项。\n1)直接设定选项文本和选项值。\n2)编辑的对象有注解为@ProvideOption的方法或属性。");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		editor.createTextField(parent, "选项文本（#分割）：", element, "optionText", SWT.BORDER);

		editor.createTextField(parent, "选项值（#分割，默认与文本一致）：", element, "optionValue", SWT.BORDER);

	}

}
