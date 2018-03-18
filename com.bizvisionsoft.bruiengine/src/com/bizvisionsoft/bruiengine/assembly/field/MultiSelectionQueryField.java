package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.assembly.DataGrid;
import com.bizvisionsoft.bruiengine.assembly.ToolItemDescriptor;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class MultiSelectionQueryField extends SelectionField {

	private List<Object> value;
	private DataGrid grid;

	public MultiSelectionQueryField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		// 1. ʵ����������������
		BruiAssemblyEngine brui = BruiAssemblyEngine
				.newInstance(Brui.site.getAssembly(fieldConfig.getSelectorAssemblyId()));
		BruiAssemblyContext gridContext;
		context.add(gridContext = new BruiAssemblyContext().setParent(context));
		gridContext.setEngine(brui);
		grid = ((DataGrid) brui.getTarget()).disablePagination().disableDateSetEngine();

		// ������Ա༭������ ��Ӱ�ť��ɾ����ť��������ӹ�ѡ��
		if (!isReadOnly()) {
			// 2. ���ñ�����ѡ��
			grid.setCheckOn()
					// 3. ���ӹ�������ť
					.addToolItem(new ToolItemDescriptor("���","inbox", e -> {
						showSelector();
					})).addToolItem(new ToolItemDescriptor("ɾ��","inbox", e -> {
						if (value == null)
							return;
						value.removeAll(grid.getSelectedItems());
						grid.removeSelectedItem();
					})).addToolItem(new ToolItemDescriptor("���","inbox", e -> {
						value.clear();
						grid.removeAllItem();
					}));

		}

		Composite panel = new Composite(parent, SWT.BORDER);
		brui.init(new IServiceWithId[] { editor.getBruiService(), gridContext }).createUI(panel);

		return panel;
	}

	@Override
	protected Object getControlLayoutData() {
		GridData gd = (GridData) super.getControlLayoutData();
		gd.heightHint = isReadOnly()?215:227;// ��ʾ4�У����й�����
		return gd;
	}

	private void showSelector() {
		editor.switchContent(this, fieldConfig.getSelectorAssemblyId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		this.value = (List<Object>) value;
		presentation();
	}

	protected void presentation() {
		grid.setInput(value);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		if (saveCheck && fieldConfig.isRequired() && (value == null || value.isEmpty()))
			throw new Exception(fieldConfig.getFieldText() + "���");
	}

	public boolean setSelection(List<Object> data) {
		try {
			if (value == null)
				value = new ArrayList<Object>();

			data.removeAll(value);
			value.addAll(data);

			presentation();
			writeToInput(false);
			return true;
		} catch (Exception e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "����", e.getMessage());
			return false;
		}
	}

}
