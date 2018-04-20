package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblyTreeEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符：", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称：", inputData, "name", SWT.BORDER);

		createTextField(parent, "组件标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "描述：", inputData, "description", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("事件侦听器：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "插件唯一标识符（Bundle Id）：", inputData, "eventHandlerBundleId", SWT.BORDER);

		createTextField(parent, "完整的类名:", inputData, "eventHandlerClassName", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		l = new Label(parent, SWT.NONE);
		l.setText("自定义取数，您可以使用插件或选择调用服务：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "取数插件唯一标识符（Bundle Id）：", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "取数完整的类名:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "取数服务名称:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("例如：com.bizvisionsoft.service.UserService");

		parent = createTabItemContent("容器设置");

		createCheckboxField(parent, "带有顶部的标题栏和工具栏：", inputData, "hasTitlebar", SWT.CHECK);

		createTextField(parent, "组件标题:", inputData, "stickerTitle", SWT.BORDER);
		
		createCheckboxField(parent, "是否可以关闭当前内容区：", inputData, "closable", SWT.CHECK);

		createCheckboxField(parent, "是否在标题栏上显示传入对象名称：", inputData, "displayInputLabelInTitlebar", SWT.CHECK);
		createCheckboxField(parent, "是否在标题栏上显示根上下文传入对象名称：", inputData, "displayRootInputLabelInTitlebar", SWT.CHECK);

		createCheckboxField(parent, "容器上边框：", inputData, "borderTop", SWT.CHECK);

		createCheckboxField(parent, "容器右边框：", inputData, "borderRight", SWT.CHECK);

		createCheckboxField(parent, "容器下边框：", inputData, "borderBottom", SWT.CHECK);

		createCheckboxField(parent, "容器左边框：", inputData, "borderLeft", SWT.CHECK);

		parent = createTabItemContent("节点操作");
		List<Action> actions = ((Assembly) inputData).getRowActions();
		if (actions == null)
			((Assembly) inputData).setRowActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);

		parent = createTabItemContent("工具栏操作");
		List<Action> toolbarActions = ((Assembly) inputData).getActions();
		if (toolbarActions == null)
			((Assembly) inputData).setActions(toolbarActions = new ArrayList<Action>());
		new ActionsEditPane(parent, toolbarActions, true, this);

		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
