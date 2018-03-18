package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ComboQueryField extends ComboField {

	public ComboQueryField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite pane = new Composite(parent, SWT.NONE);
		pane.setLayout(new FormLayout());
		Combo combo = (Combo) super.createControl(pane);
		Button btn = new Button(pane, SWT.PUSH);
		btn.setText("Çå¿Õ");
		btn.addListener(SWT.Selection, e -> {
			combo.clearSelection();
			combo.deselectAll();
		});

		FormData fd = new FormData();
		btn.setLayoutData(fd);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
		fd.top = new FormAttachment();
		fd.width = 80;

		fd = new FormData();
		combo.setLayoutData(fd);
		fd.right = new FormAttachment(btn,-8);
		fd.bottom = new FormAttachment(100);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		return pane;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void setValue(Object value) {
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {

	}

}
