package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.bizvisionsoft.bruicommons.model.FormField;

public class CheckField extends EditorField {

	private Button control;

	public CheckField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite panel = new Composite(parent,hasBorder()?SWT.BORDER:SWT.NONE);
		FillLayout layout = new FillLayout();
		panel.setLayout(layout);
		layout.marginWidth = 16;
		if (FormField.CHECK_STYLE_SWITCH.equals(fieldConfig.getCheckStyle())) {
			control = new Button(panel, SWT.CHECK);
			control.setData(RWT.CUSTOM_VARIANT, "switch");
		} else {
			control = new Button(panel, SWT.CHECK);
		}
		//////////////////////////////////////////////////////////////////////////////////////
		// 读取配置进行设置

		// 设置文本是否只读
		control.setEnabled(!isReadOnly());
		if (!isReadOnly()) {
			control.addListener(SWT.Selection, e -> {
				try {
					writeToInput(false);
				} catch (Exception e1) {
					MessageDialog.openError(control.getShell(), "错误", e1.getMessage());
				}
			});
		}
		// 设置修改
		return panel;
	}

	@Override
	public void setValue(Object value) {
		control.setSelection(Boolean.TRUE.equals(value));
	}

	@Override
	public Object getValue() {
		return control.getSelection();
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
	}

}
