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

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ����", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "������ƣ�", inputData, "name", SWT.BORDER);

		createTextField(parent, "�������:", inputData, "title", SWT.BORDER);

		createTextField(parent, "������", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "�Ƿ�ֻ���򿪣�", inputData, "readonly", SWT.CHECK);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("�Զ������ͼ���ȡ����������ʹ�ò����ѡ����÷���");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "ȡ�����Ψһ��ʶ����Bundle Id��:", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "ȡ������������:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "ȡ����������:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("���磺com.bizvisionsoft.service.UserService");

		parent = createTabItemContent("�����");
		List<Column> cols = ((Assembly) inputData).getColumns();
		if (cols == null)
			((Assembly) inputData).setColumns(cols = new ArrayList<Column>());
		new GanttColumnsEditPane(parent, cols, this);

		parent = createTabItemContent("����");
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
