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
		// 1. 实例化，设置上下文
		BruiAssemblyEngine brui = BruiAssemblyEngine
				.newInstance(Brui.site.getAssembly(fieldConfig.getSelectorAssemblyId()));
		BruiAssemblyContext gridContext;
		context.add(gridContext = new BruiAssemblyContext().setParent(context));
		gridContext.setEngine(brui);
		grid = ((DataGrid) brui.getTarget()).disablePagination().disableDateSetEngine();

		// 如果可以编辑，增加 添加按钮，删除按钮，表格增加勾选框
		if (!isReadOnly()) {
			// 2. 设置表格项的选择
			grid.setCheckOn()
					// 3. 增加工具栏按钮
					.addToolItem(new ToolItemDescriptor("添加","inbox", e -> {
						showSelector();
					})).addToolItem(new ToolItemDescriptor("删除","inbox", e -> {
						if (value == null)
							return;
						value.removeAll(grid.getSelectedItems());
						grid.removeSelectedItem();
					})).addToolItem(new ToolItemDescriptor("清空","inbox", e -> {
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
		gd.heightHint = isReadOnly()?215:227;// 显示4行，带有工具栏
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
			throw new Exception(fieldConfig.getFieldText() + "必填。");
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
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", e.getMessage());
			return false;
		}
	}

}
