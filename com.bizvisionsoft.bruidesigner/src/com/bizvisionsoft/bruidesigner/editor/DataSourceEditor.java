package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.DataSource;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class DataSourceEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "数据源名称:", inputData, "name", SWT.BORDER);

		createTextField(parent, "描述:", inputData, "description", SWT.BORDER);

		createTextField(parent, "服务名称:", inputData, "serviceName", SWT.BORDER);
		
		createTextField(parent, "数据类型:", inputData, "className", SWT.BORDER);

		createCheckboxField(parent, "返回多条数据:", inputData, "list", SWT.CHECK);

		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return DataSource.class;
	}
	
	@Override
	protected boolean enableJsonViewer() {
		return false;
	}
	
	@Override
	protected boolean enableParameter() {
		return false;
	}

}
