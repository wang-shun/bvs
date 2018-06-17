package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblyLinkEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("基本信息");

		createAssemblyField(parent, "组件:", inputData, "id",false);

		createCheckboxField(parent, "默认:", inputData, "defaultAssembly", SWT.CHECK);
		
		createTextField(parent, "角色（多个#分割）", inputData, "role", SWT.BORDER);
		
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return AssemblyLink.class;
	}


	

}
