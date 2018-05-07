package com.bizvisionsoft.bruidesigner.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblyMessengerEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ��:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "�������:", inputData, "name", SWT.BORDER);

		createTextField(parent, "����:", inputData, "description", SWT.BORDER);

		createTextField(parent, "�������ı�:", inputData, "stickerTitle", SWT.BORDER);

		createCheckboxField(parent, "�ϱ߿�", inputData, "borderTop", SWT.CHECK);

		createCheckboxField(parent, "�ұ߿�", inputData, "borderRight", SWT.CHECK);

		createCheckboxField(parent, "�±߿�", inputData, "borderBottom", SWT.CHECK);

		createCheckboxField(parent, "��߿�", inputData, "borderLeft", SWT.CHECK);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("�Զ������ȡ����������ʹ�ò����ѡ����÷���");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "ȡ�����Ψһ��ʶ����Bundle Id��:", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "ȡ������������:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "ȡ����������:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("���磺UserService.list");

		addPartNamePropertyChangeListener("name");
	}

	@Override
	protected Class<? extends ModelObject> getDataType() {
		return Assembly.class;
	}

}
