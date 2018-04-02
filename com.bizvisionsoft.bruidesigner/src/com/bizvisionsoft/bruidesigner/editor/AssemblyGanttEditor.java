package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblyGanttEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符：", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称：", inputData, "name", SWT.BORDER);

		createCheckboxField(parent, "带有顶部的标题栏和工具栏：", inputData, "hasTitlebar", SWT.CHECK);

		createTextField(parent, "组件标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "描述：", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "是否只读打开：", inputData, "readonly", SWT.CHECK);

		createCheckboxField(parent, "根据列宽自动设置表格宽度：", inputData, "ganttGridWidthCalculate", SWT.CHECK);

		createIntegerField(parent, "手动设置表格宽度：", inputData, "ganttGridWidth", SWT.BORDER, 200, 4000);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("自定义甘特图如何取数，您可以使用插件或选择调用服务：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "取数插件唯一标识符（Bundle Id）：", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "取数完整的类名:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "取数服务名称:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("例如：com.bizvisionsoft.service.UserService");
		
		
		parent = createTabItemContent("容器设置");
		
		createCheckboxField(parent, "带有顶部的标题栏和工具栏：", inputData, "hasTitlebar", SWT.CHECK);

		createTextField(parent, "组件标题:", inputData, "stickerTitle", SWT.BORDER);
		
		createCheckboxField(parent, "容器上边框：", inputData, "borderTop", SWT.CHECK);
		
		createCheckboxField(parent, "容器右边框：", inputData, "borderRight", SWT.CHECK);
		
		createCheckboxField(parent, "容器下边框：", inputData, "borderBottom", SWT.CHECK);
		
		createCheckboxField(parent, "容器左边框：", inputData, "borderLeft", SWT.CHECK);
		

		parent = createTabItemContent("表格列");
		List<Column> cols = ((Assembly) inputData).getColumns();
		if (cols == null)
			((Assembly) inputData).setColumns(cols = new ArrayList<Column>());
		new GanttColumnsEditPane(parent, cols, this);

		parent = createTabItemContent("行操作");
		List<Action> actions = ((Assembly) inputData).getRowActions();
		if (actions == null)
			((Assembly) inputData).setRowActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);

		parent = createTabItemContent("表头操作");
		List<Action> headActions = ((Assembly) inputData).getHeadActions();
		if (headActions == null)
			((Assembly) inputData).setHeadActions(headActions = new ArrayList<Action>());
		new ActionsEditPane(parent, headActions, true, this);

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
