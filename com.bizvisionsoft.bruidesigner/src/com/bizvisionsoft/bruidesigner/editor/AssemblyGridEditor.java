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

		Composite parent = createTabItemContent("������Ϣ");

		createTextField(parent, "Ψһ��ʶ����", inputData, "id", SWT.READ_ONLY);

		createTextField(parent, "������ƣ�", inputData, "name", SWT.BORDER);
		
		createTextField(parent, "������", inputData, "description", SWT.BORDER);

		createCheckboxField(parent, "��ʾ���߿�", inputData, "gridHasBorder", SWT.CHECK);

		createCheckboxField(parent, "��ʾ�����������", inputData, "gridHasHScroll", SWT.CHECK);

		createCheckboxField(parent, "��ʾ�����������", inputData, "gridHasVScroll", SWT.CHECK);

		createCheckboxField(parent, "��������չ������ͼ�꣺", inputData, "gridHideIndentionImage", SWT.CHECK);

		createCheckboxField(parent, "��ʾ�����У�", inputData, "gridHeaderVisiable", SWT.CHECK);

		createCheckboxField(parent, "��ʾ�����У�", inputData, "gridFooterVisiable", SWT.CHECK);

		createCheckboxField(parent, "��ʾ����ߣ�", inputData, "gridLineVisiable", SWT.CHECK);

		createIntegerField(parent, "�иߣ�0��ʾ�����ã���", inputData, "gridCustomItemHeight", SWT.BORDER, 0, 999);

		createCheckboxField(parent, "�Զ������иߣ�", inputData, "gridAutoHeight", SWT.CHECK);

		createCheckboxField(parent, "�����������п�", inputData, "gridAutoColumnWidth", SWT.CHECK);

		createCheckboxField(parent, "ʹ�ó��ı���ʾ���ݣ�", inputData, "gridMarkupEnabled", SWT.CHECK);

		createIntegerField(parent, "չ��������-1������ȫչ������", inputData, "gridAutoExpandLevel", SWT.BORDER, -1, 999);

		createCheckboxField(parent, "֧�ֶ�ѡ��", inputData, "gridMultiSelection", SWT.CHECK);

		createIntegerField(parent, "�̶�������0�����̶�����", inputData, "gridFix", SWT.BORDER, 0, 999);

		createCheckboxField(parent, "��ҳ�������ݣ�", inputData, "gridPageControl", SWT.CHECK);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Label l = new Label(parent, SWT.NONE);
		l.setText("������Ⱦ�������ڿ������������ʾ��");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "��Ⱦ�����Ψһ��ʶ����Bundle Id��:", inputData, "gridRenderBundleId", SWT.BORDER);

		createTextField(parent, "��Ⱦ������������:", inputData, "gridRenderClassName", SWT.BORDER);

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		l = new Label(parent, SWT.NONE);
		l.setText("�Զ��������ȡ����������ʹ�ò����ѡ����÷���");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "ȡ�����Ψһ��ʶ����Bundle Id��:", inputData, "gridDataSetBundleId", SWT.BORDER);

		createTextField(parent, "ȡ������������:", inputData, "gridDataSetClassName", SWT.BORDER);

		createTextField(parent, "ȡ����������:", inputData, "gridDataSetService", SWT.BORDER)
				.setMessage("���磺UserService.list");

		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL)
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		l = new Label(parent, SWT.NONE);
		l.setText("�Զ����ѯ����ģ�ͣ�");
		l.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		createTextField(parent, "��ѯ����ģ�ͣ�Bundle Id��:", inputData, "queryBuilderBundle", SWT.BORDER);

		createTextField(parent, "��ѯ����ģ������������:", inputData, "queryBuilderClass", SWT.BORDER);
		
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
		new GridColumnsEditPane(parent, cols, this);

		parent = createTabItemContent("�в���");
		List<Action> actions = ((Assembly) inputData).getRowActions();
		if (actions == null)
			((Assembly) inputData).setRowActions(actions = new ArrayList<Action>());
		new ActionsEditPane(parent, actions, true, this);
		
		parent = createTabItemContent("����������");
		List<Action> toolbarActions = ((Assembly) inputData).getActions();
		if (toolbarActions == null)
			((Assembly) inputData).setActions(toolbarActions = new ArrayList<Action>());
		new ActionsEditPane(parent, toolbarActions, true, this);

		parent = createTabItemContent("��ѯ�ֶ�");
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
