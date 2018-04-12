package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblySelectorEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符：", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称：", inputData, "name", SWT.BORDER);
		
		createTextField(parent, "描述：", inputData, "description", SWT.BORDER);
		
		createAssemblyField(parent, "表格组件:", inputData, "selectorGridAssemblyId", true);

		createCheckboxField(parent, "返回多条记录", inputData, "selectorMultiSelection", SWT.CHECK);
		
		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
