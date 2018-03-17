package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.bizvisionsoft.bruiengine.util.Util;

public class TextAreaField extends EditorField {

	private Text control;

	public TextAreaField() {
	}

	@Override
	protected Control createControl(Composite parent) {

		control = new Text(parent, SWT.BORDER|SWT.MULTI);

		// �����ı��Ƿ�ֻ��
		control.setEditable(!isReadOnly());

		// �����ı��Ƿ���ʾΪ����
		// ^ style

		// ���������޶�
		if (fieldConfig.getTextLimit() > 0)
			control.setTextLimit(fieldConfig.getTextLimit());

		// ����Ϊ����

		// �����޸�
		control.addListener(SWT.FocusOut, e -> {
			try {
				writeToInput(false);
			} catch (Exception e1) {
				MessageDialog.openError(control.getShell(), "����", e1.getMessage());
			}
		});
		return control;
	}
	
	@Override
	protected Object getControlLayoutData() {
		GridData gd = (GridData) super.getControlLayoutData();
		gd.heightHint = 120;
		return gd;
	}

	@Override
	public void setValue(Object value) {
		String text = "";
		if (value != null) {
			String format = fieldConfig.getFormat();
			if (format != null && !format.isEmpty()) {
				text = Util.getFormatText(value, format, locale);
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
		// ������
		String text = control.getText().trim();
		if (fieldConfig.isRequired() && text.isEmpty()) {
			throw new Exception(fieldConfig.getFieldText() + "���");
		}
	}


}
