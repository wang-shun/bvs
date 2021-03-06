package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.editor.pane.ActionsEditPane;

public class AssemblyActionPanelEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称:", inputData, "name", SWT.BORDER);

		createTextField(parent, "描述:", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "带有顶部的标题栏和工具栏：", inputData, "hasTitlebar", SWT.CHECK);
		
		createCheckboxField(parent, "卡片式标题栏：", inputData, "compactTitleBar", SWT.CHECK);
		
		createTextField(parent, "标题栏文本:", inputData, "stickerTitle", SWT.BORDER);

		createCheckboxField(parent, "是否在标题栏上显示传入对象名称：", inputData, "displayInputLabelInTitlebar", SWT.CHECK);

		createCheckboxField(parent, "是否在标题栏上显示根上下文传入对象名称：", inputData, "displayRootInputLabelInTitlebar", SWT.CHECK);

		createCheckboxField(parent, "上边框：", inputData, "borderTop", SWT.CHECK);

		createCheckboxField(parent, "右边框：", inputData, "borderRight", SWT.CHECK);

		createCheckboxField(parent, "下边框：", inputData, "borderBottom", SWT.CHECK);

		createCheckboxField(parent, "左边框：", inputData, "borderLeft", SWT.CHECK);

		createTextField(parent, "文本区内容:", inputData, "message", SWT.BORDER|SWT.MULTI);

		createIntegerField(parent, "列数：", inputData, "actionPanelColumnCount", SWT.BORDER, 1, 20);
		
		parent = createTabItemContent("面板操作");
		List<Action>  actions = ((Assembly) inputData).getRowActions();
		if (actions == null)
			((Assembly) inputData).setRowActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);

		parent = createTabItemContent("操作");
		actions = ((Assembly) inputData).getActions();
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
