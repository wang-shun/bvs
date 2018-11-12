package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruicommons.model.ModelObject;
import com.bizvisionsoft.bruidesigner.editor.field.FormFieldsEditPane;
import com.bizvisionsoft.bruidesigner.editor.pane.ActionsEditPane;

public class AssemblyMessengerEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("基本信息");

		createTextField(parent, "唯一标识符:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "组件名称:", inputData, "name", SWT.BORDER);

		createTextField(parent, "描述:", inputData, "description", SWT.BORDER);

		createTextField(parent, "标题栏文本:", inputData, "stickerTitle", SWT.BORDER);

		createCheckboxField(parent, "上边框：", inputData, "borderTop", SWT.CHECK);

		createCheckboxField(parent, "右边框：", inputData, "borderRight", SWT.CHECK);

		createCheckboxField(parent, "下边框：", inputData, "borderBottom", SWT.CHECK);

		createCheckboxField(parent, "左边框：", inputData, "borderLeft", SWT.CHECK);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("自定义如何取数，您可以使用插件或选择调用服务：");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "取数插件唯一标识符（Bundle Id）:", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "取数完整的类名:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "取数服务名称:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("例如：UserService.list");
		
		createCheckboxField(parent, "翻页加载数据：", inputData, "gridPageControl", SWT.CHECK);

		createCheckboxField(parent, "滚动加载数据：", inputData, "scrollLoadData", SWT.CHECK);
		
		createIntegerField(parent, "每页加载多少条（0表示默认，30条）：", inputData, "gridPageCount", SWT.BORDER, 0, 500);
		
		parent = createTabItemContent("查询字段");
		List<FormField> fields = ((Assembly) inputData).getFields();
		if (fields == null)
			((Assembly) inputData).setFields(fields = new ArrayList<FormField>());
		new FormFieldsEditPane(parent, fields, this, "query");
		
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
