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
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.editor.field.FormFieldsEditPane;

public class AssemblyGridEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符：", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称：", inputData, "name", SWT.BORDER);

		createTextField(parent, "组件标题:", inputData, "title", SWT.BORDER);

		createTextField(parent, "描述：", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "显示边框：", inputData, "gridHasBorder", SWT.CHECK);

		createCheckboxField(parent, "显示横向滚动条：", inputData, "gridHasHScroll", SWT.CHECK);

		createCheckboxField(parent, "显示纵向滚动条：", inputData, "gridHasVScroll", SWT.CHECK);

		createCheckboxField(parent, "隐藏树的展开收起图标：", inputData, "gridHideIndentionImage", SWT.CHECK);

		createCheckboxField(parent, "显示标题行：", inputData, "gridHeaderVisiable", SWT.CHECK);

		createCheckboxField(parent, "显示汇总行：", inputData, "gridFooterVisiable", SWT.CHECK);

		createCheckboxField(parent, "显示表格线：", inputData, "gridLineVisiable", SWT.CHECK);

		createIntegerField(parent, "行高（0表示不设置）：", inputData, "gridCustomItemHeight", SWT.BORDER, 0, 999);

		createCheckboxField(parent, "自动计算行高：", inputData, "gridAutoHeight", SWT.CHECK);

		createCheckboxField(parent, "按比例计算列宽：", inputData, "gridAutoColumnWidth", SWT.CHECK);

		createCheckboxField(parent, "使用超文本显示内容：", inputData, "gridMarkupEnabled", SWT.CHECK);

		createIntegerField(parent, "展开层数（-1代表完全展开）：", inputData, "gridAutoExpandLevel", SWT.BORDER, -1, 999);

		createCheckboxField(parent, "支持多选：", inputData, "gridMultiSelection", SWT.CHECK);

		createIntegerField(parent, "固定列数（0代表不固定）：", inputData, "gridFix", SWT.BORDER, 0, 999);

		createCheckboxField(parent, "翻页加载数据：", inputData, "gridPageControl", SWT.CHECK);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("定义渲染器可用于控制数据如何显示：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "渲染器插件唯一标识符（Bundle Id）:", inputData, "gridRenderBundleId", SWT.BORDER);

		createTextField(parent, "渲染器完整的类名:", inputData, "gridRenderClassName", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		l = new Label(parent, SWT.NONE);
		l.setText("自定义表格如何取数，您可以使用插件或选择调用服务：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "取数插件唯一标识符（Bundle Id）:", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "取数完整的类名:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "取数服务名称:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("例如：UserService.list");

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		l = new Label(parent, SWT.NONE);
		l.setText("自定义查询构造模型：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "查询构造模型（Bundle Id）:", inputData, "queryBuilderBundle", SWT.BORDER);

		createTextField(parent, "查询构造模型完整的类名:", inputData, "queryBuilderClass", SWT.BORDER);

		parent = createTabItemContent("表格列");
		List<Column> cols = ((Assembly) inputData).getColumns();
		if (cols == null)
			((Assembly) inputData).setColumns(cols = new ArrayList<Column>());
		new GridColumnsEditPane(parent, cols, this);

		parent = createTabItemContent("行操作");
		List<Action> actions = ((Assembly) inputData).getActions();
		if (actions == null)
			((Assembly) inputData).setActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);

		parent = createTabItemContent("查询字段");
		List<FormField> fields = ((Assembly) inputData).getFields();
		if (fields == null)
			((Assembly) inputData).setFields(fields = new ArrayList<FormField>());
		new FormFieldsEditPane(parent, fields, this, "query");

		addPartNamePropertyChangeListener("name");

	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
