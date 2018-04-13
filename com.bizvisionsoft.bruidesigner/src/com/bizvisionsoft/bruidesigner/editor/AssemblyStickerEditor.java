package com.bizvisionsoft.bruidesigner.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruicommons.model.ModelObject;

public class AssemblyStickerEditor extends ModelEditor {

	@Override
	public void createContent() {

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ��:", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "�������:", inputData, "name", SWT.BORDER);

		createTextField(parent, "����:", inputData, "description", SWT.BORDER);

		createTextField(parent, "�������ı�:", inputData, "stickerTitle", SWT.BORDER);
		
		createCheckboxField(parent, "�Ƿ���Թرյ�ǰ��������", inputData, "closable", SWT.CHECK);

		createCheckboxField(parent, "�Ƿ��ڱ���������ʾ����������ƣ�", inputData, "displayInputLabelInTitlebar", SWT.CHECK);

		createCheckboxField(parent, "�ϱ߿�", inputData, "borderTop", SWT.CHECK);

		createCheckboxField(parent, "�ұ߿�", inputData, "borderRight", SWT.CHECK);

		createCheckboxField(parent, "�±߿�", inputData, "borderBottom", SWT.CHECK);

		createCheckboxField(parent, "��߿�", inputData, "borderLeft", SWT.CHECK);

		parent = createTabItemContent("���ֺ������");
		List<Layout> layouts = ((Assembly) inputData).getLayout();
		if (layouts == null)
			((Assembly) inputData).setLayout(layouts = new ArrayList<Layout>());

		new LayoutEditPane(parent, layouts, this);

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
