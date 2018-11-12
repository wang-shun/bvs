package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.editor.pane.LayoutEditPane;

public class AssemblyBoardEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符：", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称：", inputData, "name", SWT.BORDER);

		createTextField(parent, "组件标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "描述：", inputData, "description", SWT.BORDER);
		
		parent = createTabItemContent("布局和子组件");
		List<Layout> layouts = ((Assembly) inputData).getLayout();
		if (layouts == null)
			((Assembly) inputData).setLayout(layouts = new ArrayList<Layout>());

		new LayoutEditPane(parent, layouts, this);
		
		
		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}
	
	@Override
	protected boolean enableCustomized() {
		return true;
	}
}