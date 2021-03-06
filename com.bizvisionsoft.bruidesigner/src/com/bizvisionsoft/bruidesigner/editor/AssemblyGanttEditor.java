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
import com.bizvisionsoft.bruidesigner.editor.pane.ActionsEditPane;
import com.bizvisionsoft.bruidesigner.editor.pane.GanttColumnsEditPane;

public class AssemblyGanttEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符：", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称：", inputData, "name", SWT.BORDER);

		createTextField(parent, "组件标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "描述：", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "是否只读打开：", inputData, "readonly", SWT.CHECK);
		
		createCheckboxField(parent, "支持行内编辑（测试）：", inputData, "inlineEdit", SWT.CHECK);

		createCheckboxField(parent, "启动对比甘特图（将对比start_date1, end_date1注解的字段）：", inputData, "enableGanttCompare",
				SWT.CHECK);

		createComboField(parent, new String[] { "年月 - 周次 - 日期", "年份 - 月份 - 周次", "年份 - 月份", "年月 - 周次" },
				new String[] { "month-week-date", "year-month-week", "year-month", "month-week" }, "时间刻度", inputData,
				"ganttTimeScaleType", SWT.READ_ONLY | SWT.BORDER);

		createCheckboxField(parent, "根据列宽自动设置表格宽度：", inputData, "ganttGridWidthCalculate", SWT.CHECK);

		createIntegerField(parent, "手动设置表格宽度：", inputData, "ganttGridWidth", SWT.BORDER, 0, 4000);
		
		createCheckboxField(parent, "禁用标准导出功能：", inputData, "disableStandardExport", SWT.CHECK);
		
		createCheckboxField(parent, "禁用客户端设置功能：", inputData, "disableCustomized", SWT.CHECK);
		
		createCheckboxField(parent, "禁用标准查询功能：", inputData, "disableStdQuery", SWT.CHECK);

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
		l.setText("自定义甘特图如何取数，您可以使用插件或选择调用服务：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "取数插件唯一标识符（Bundle Id）：", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "取数完整的类名:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "取数服务名称:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("例如：com.bizvisionsoft.service.UserService");

		parent = createTabItemContent("容器设置");

		createCheckboxField(parent, "带有顶部的标题栏和工具栏：", inputData, "hasTitlebar", SWT.CHECK);
		
		createCheckboxField(parent, "卡片式标题栏：", inputData, "compactTitleBar", SWT.CHECK);

		createTextField(parent, "组件标题:", inputData, "stickerTitle", SWT.BORDER);

		createCheckboxField(parent, "是否在标题栏上显示传入对象名称：", inputData, "displayInputLabelInTitlebar", SWT.CHECK);
		createCheckboxField(parent, "是否在标题栏上显示根上下文传入对象名称：", inputData, "displayRootInputLabelInTitlebar", SWT.CHECK);

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
