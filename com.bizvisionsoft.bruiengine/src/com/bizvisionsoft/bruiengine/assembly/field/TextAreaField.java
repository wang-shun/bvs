package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.bizvisionsoft.service.tools.Formatter;

public class TextAreaField extends EditorField {

	private Text control;

	public TextAreaField() {
	}
	
	@Override
	protected boolean isVertivalLayout() {
		return true;
	}

	@Override
	protected Control createControl(Composite parent) {

		control = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP);

		// 设置文本是否只读
		control.setEditable(!isReadOnly());

		// 设置文本是否显示为密码
		// ^ style

		// 设置字数限定
		if (fieldConfig.getTextLimit() > 0)
			control.setTextLimit(fieldConfig.getTextLimit());

		// 设置为必填
		if (!isReadOnly()) {

			// 设置修改
			control.addListener(SWT.FocusOut, e -> {
				try {
					writeToInput(false);
				} catch (Exception e1) {
					MessageDialog.openError(control.getShell(), "错误", e1.getMessage());
				}
			});
		}
		return control;
	}

	@Override
	protected Object getControlLayoutData() {
		GridData gd = (GridData) super.getControlLayoutData();
		if (!fieldConfig.isGrabVertical()) {
			int bh = fieldConfig.getHeight();
			if (bh == 0) {
				gd.heightHint = 64;
			} else {
				gd.heightHint = bh;
			}
		}else {
			gd.grabExcessVerticalSpace = true;
		}
		return gd;
	}

	@Override
	public void setValue(Object value) {
		String text = "";
		if (value != null) {
			String format = fieldConfig.getFormat();
			if (format != null && !format.isEmpty()) {
				text = Formatter.getString(value, format, locale);
			} else {
				text = value.toString();
			}
		}

		control.setText(text);
	}

	@Override
	public Object getValue() {
		return control.getText();
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		// 必填检查
		String text = control.getText().trim();
		if (saveCheck && fieldConfig.isRequired() && text.isEmpty()) {
			throw new Exception(fieldConfig.getFieldText() + "必填。");
		}
	}

}
