package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import com.bizvisionsoft.bruiengine.BruiEngine;
import com.bizvisionsoft.service.annotations.ReadValue;

public class SelectionField extends EditorField {

	private Text text;
	private Object value;

	public SelectionField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite pane = new Composite(parent, SWT.BORDER);
		pane.setLayout(new FormLayout());

		text = new Text(pane, SWT.NONE);
		text.setEditable(false);
		if (!isReadOnly()) {
			Button clear = new Button(pane, SWT.PUSH);
			clear.setData(RWT.CUSTOM_VARIANT, "inline");
			clear.setText("Çå¿Õ");
			clear.addListener(SWT.Selection, e -> {
				setSelection(new ArrayList<Object>());
			});

			Button select = new Button(pane, SWT.PUSH);
			select.setData(RWT.CUSTOM_VARIANT, "inline");
			select.setText("Ñ¡Ôñ...");
			select.addListener(SWT.Selection, e -> {
				showSelector();
			});

			text.addListener(SWT.MouseDown, e -> {
				showSelector();
			});

			FormData fd = new FormData();
			select.setLayoutData(fd);
			fd.right = new FormAttachment(100);
			fd.bottom = new FormAttachment(100);
			fd.height = 36;
			fd.width = 80;

			fd = new FormData();
			clear.setLayoutData(fd);
			fd.right = new FormAttachment(select);
			fd.bottom = new FormAttachment(100);
			fd.height = 36;
			fd.width = 80;

			fd = new FormData();
			text.setLayoutData(fd);
			fd.right = new FormAttachment(clear);
			fd.bottom = new FormAttachment(100);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
		} else {
			FormData fd = new FormData();
			text.setLayoutData(fd);
			fd.right = new FormAttachment(100);
			fd.bottom = new FormAttachment(100);
			fd.top = new FormAttachment();
			fd.left = new FormAttachment();
		}

		return pane;
	}

	private void showSelector() {
		editor.switchContent(this, fieldConfig.getSelectorAssemblyId());
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		presentation();
	}

	protected void presentation() {
		Object label = Optional.ofNullable(value)
				.map(v -> BruiEngine.readValue(v, assemblyConfig.getName(), ReadValue.LABEL,null)).orElse("");
		text.setText("" + label);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		if (saveCheck && value == null)
			throw new Exception(fieldConfig.getFieldText() + "±ØÌî¡£");
	}

	public boolean setSelection(List<Object> data) {
		try {
			this.value = data.isEmpty() ? null : data.get(0);
			presentation();
			writeToInput(false);
			return true;
		} catch (Exception e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "´íÎó", e.getMessage());
			return false;
		}
	}

}
