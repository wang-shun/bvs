package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CheckQueryField extends EditorField {

	private Combo control;

	public CheckQueryField() {
	}

	@Override
	protected Control createControl(Composite parent) {

		control = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		control.add("��");
		control.add("��");
		control.add("");

		control.addListener(SWT.Selection, e -> {
			try {
				writeToInput(false);
			} catch (Exception e1) {
				MessageDialog.openError(control.getShell(), "����", e1.getMessage());
			}
		});

		return control;
	}

	@Override
	public void setValue(Object value) {
	}

	@Override
	public Object getValue() {
		if ("��".equals(control.getText())) {
			return true;
		} else if ("��".equals(control.getText())) {
			return false;
		} else if ("".equals(control.getText())) {
			return null;
		}
		return null;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
	}

}
