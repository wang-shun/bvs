package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.editor.field.FormFieldsEditPane;

public class AssemblyEditorEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符：", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称：", inputData, "name", SWT.BORDER);

		createTextField(parent, "组件标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "描述：", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "窄（显示为宽度标准尺寸的2/3）：", inputData, "smallEditor", SWT.CHECK);
		
		createCheckboxField(parent, "短（显示为高度标准尺寸的2/3）：", inputData, "tinyEditor", SWT.CHECK);
		
		createCheckboxField(parent, "去掉字段边框：", inputData, "removeBorder", SWT.CHECK);
		
		createCheckboxField(parent, "加入到父上下文（选择类型字段数据集需获取根上下文时需要选中）：", inputData, "addToParentContext", SWT.CHECK);

		createTextField(parent, "保存时不能忽略null值的字段名（#分割）：", inputData, "nullValueAllowedFields", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("定义渲染器可用于控制数据如何显示：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "渲染器插件唯一标识符（Bundle Id）:", inputData, "gridRenderBundleId", SWT.BORDER);

		createTextField(parent, "渲染器完整的类名:", inputData, "gridRenderClassName", SWT.BORDER);

		parent = createTabItemContent("字段");
		List<FormField> fields = ((Assembly) inputData).getFields();
		if (fields == null)
			((Assembly) inputData).setFields(fields = new ArrayList<FormField>());
		new FormFieldsEditPane(parent, fields, this, "editor");

		// parent = createTabItemContent("操作");
		// List<Action> actions = ((Assembly) inputData).getActions();
		// if (actions == null)
		// ((Assembly) inputData).setActions(actions = new ArrayList<Action>());
		// new ActionsEditPane(parent, actions, true, this);

		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
