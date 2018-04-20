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

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ����", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "������ƣ�", inputData, "name", SWT.BORDER);

		createTextField(parent, "�������:", inputData, "title", SWT.BORDER);

		createTextField(parent, "������", inputData, "description", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("�¼���������");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "���Ψһ��ʶ����Bundle Id����", inputData, "eventHandlerBundleId", SWT.BORDER);

		createTextField(parent, "����������:", inputData, "eventHandlerClassName", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		l = new Label(parent, SWT.NONE);
		l.setText("�Զ���ȡ����������ʹ�ò����ѡ����÷���");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "ȡ�����Ψһ��ʶ����Bundle Id����", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "ȡ������������:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "ȡ����������:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("���磺com.bizvisionsoft.service.UserService");

		parent = createTabItemContent("��������");

		createCheckboxField(parent, "���ж����ı������͹�������", inputData, "hasTitlebar", SWT.CHECK);

		createTextField(parent, "�������:", inputData, "stickerTitle", SWT.BORDER);
		
		createCheckboxField(parent, "�Ƿ���Թرյ�ǰ��������", inputData, "closable", SWT.CHECK);

		createCheckboxField(parent, "�Ƿ��ڱ���������ʾ����������ƣ�", inputData, "displayInputLabelInTitlebar", SWT.CHECK);
		createCheckboxField(parent, "�Ƿ��ڱ���������ʾ�������Ĵ���������ƣ�", inputData, "displayRootInputLabelInTitlebar", SWT.CHECK);

		createCheckboxField(parent, "�����ϱ߿�", inputData, "borderTop", SWT.CHECK);

		createCheckboxField(parent, "�����ұ߿�", inputData, "borderRight", SWT.CHECK);

		createCheckboxField(parent, "�����±߿�", inputData, "borderBottom", SWT.CHECK);

		createCheckboxField(parent, "������߿�", inputData, "borderLeft", SWT.CHECK);

		parent = createTabItemContent("�ڵ����");
		List<Action> actions = ((Assembly) inputData).getRowActions();
		if (actions == null)
			((Assembly) inputData).setRowActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);

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
