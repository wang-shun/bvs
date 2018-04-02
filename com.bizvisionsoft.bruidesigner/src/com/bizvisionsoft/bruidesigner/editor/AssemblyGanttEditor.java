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

		createCheckboxField(parent, "���ж����ı������͹�������", inputData, "hasTitlebar", SWT.CHECK);

		createTextField(parent, "�������:", inputData, "title", SWT.BORDER);

		createTextField(parent, "������", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "�Ƿ�ֻ���򿪣�", inputData, "readonly", SWT.CHECK);

		createCheckboxField(parent, "�����п��Զ����ñ���ȣ�", inputData, "ganttGridWidthCalculate", SWT.CHECK);

		createIntegerField(parent, "�ֶ����ñ���ȣ�", inputData, "ganttGridWidth", SWT.BORDER, 200, 4000);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("�Զ������ͼ���ȡ����������ʹ�ò����ѡ����÷���");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "ȡ�����Ψһ��ʶ����Bundle Id����", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "ȡ������������:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "ȡ����������:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("���磺com.bizvisionsoft.service.UserService");
		
		
		parent = createTabItemContent("��������");
		
		createCheckboxField(parent, "���ж����ı������͹�������", inputData, "hasTitlebar", SWT.CHECK);

		createTextField(parent, "�������:", inputData, "stickerTitle", SWT.BORDER);
		
		createCheckboxField(parent, "�����ϱ߿�", inputData, "borderTop", SWT.CHECK);
		
		createCheckboxField(parent, "�����ұ߿�", inputData, "borderRight", SWT.CHECK);
		
		createCheckboxField(parent, "�����±߿�", inputData, "borderBottom", SWT.CHECK);
		
		createCheckboxField(parent, "������߿�", inputData, "borderLeft", SWT.CHECK);
		

		parent = createTabItemContent("�����");
		List<Column> cols = ((Assembly) inputData).getColumns();
		if (cols == null)
			((Assembly) inputData).setColumns(cols = new ArrayList<Column>());
		new GanttColumnsEditPane(parent, cols, this);

		parent = createTabItemContent("�в���");
		List<Action> actions = ((Assembly) inputData).getRowActions();
		if (actions == null)
			((Assembly) inputData).setRowActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);

		parent = createTabItemContent("��ͷ����");
		List<Action> headActions = ((Assembly) inputData).getHeadActions();
		if (headActions == null)
			((Assembly) inputData).setHeadActions(headActions = new ArrayList<Action>());
		new ActionsEditPane(parent, headActions, true, this);

		parent = createTabItemContent("����������");
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
