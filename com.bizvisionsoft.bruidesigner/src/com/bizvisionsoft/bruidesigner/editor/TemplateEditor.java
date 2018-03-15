package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruicommons.model.Template;

public class TemplateEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("基本信息");
		
		createTextField(parent, "唯一标识符:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "模板名称:", inputData, "name", SWT.BORDER);

		createTextField(parent, "描述:", inputData, "description", SWT.BORDER);

		createTextField(parent, "插件唯一标识符（Bundle Id）:", inputData, "bundleId", SWT.BORDER);

		createTextField(parent, "完整的类名:", inputData, "className", SWT.BORDER);

		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Template.class;
	}

}
