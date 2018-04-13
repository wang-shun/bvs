package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblyStickerEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称:", inputData, "name", SWT.BORDER);

		createTextField(parent, "描述:", inputData, "description", SWT.BORDER);

		createTextField(parent, "标题栏文本:", inputData, "stickerTitle", SWT.BORDER);
		
		createCheckboxField(parent, "是否可以关闭当前内容区：", inputData, "closable", SWT.CHECK);

		createCheckboxField(parent, "是否在标题栏上显示传入对象名称：", inputData, "displayInputLabelInTitlebar", SWT.CHECK);

		createCheckboxField(parent, "上边框：", inputData, "borderTop", SWT.CHECK);

		createCheckboxField(parent, "右边框：", inputData, "borderRight", SWT.CHECK);

		createCheckboxField(parent, "下边框：", inputData, "borderBottom", SWT.CHECK);

		createCheckboxField(parent, "左边框：", inputData, "borderLeft", SWT.CHECK);

		parent = createTabItemContent("布局和子组件");
		List<Layout> layouts = ((Assembly) inputData).getLayout();
		if (layouts == null)
			((Assembly) inputData).setLayout(layouts = new ArrayList<Layout>());

		new LayoutEditPane(parent, layouts, this);

		parent = createTabItemContent("操作");
		List<Action> actions = ((Assembly) inputData).getActions();
		if (actions == null)
			((Assembly) inputData).setActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);

		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
