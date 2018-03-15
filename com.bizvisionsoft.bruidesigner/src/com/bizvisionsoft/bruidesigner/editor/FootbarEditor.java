package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Footbar;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class FootbarEditor extends ModelEditor {

	@Override
	public void createContent() {
		
		Composite parent = createTabItemContent("������Ϣ");
		createCheckboxField(parent, "����:", inputData, "enabled", SWT.CHECK);
		
		createIntegerField(parent, "�߶ȣ����أ�:", inputData, "height", SWT.BORDER,0,2000);
		
		addPartNamePropertyChangeListener("enabled");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Footbar.class;
	}

}
