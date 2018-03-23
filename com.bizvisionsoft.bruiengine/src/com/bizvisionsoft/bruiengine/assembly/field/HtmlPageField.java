package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.richtext.RichTextEditor;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.util.Util;

public class HtmlPageField extends EditorField {

	private RichTextEditor control;

	public HtmlPageField() {
	}

	@Override
	public Composite createUI(Composite parent) {
		editor.getContext().add(context = new BruiAssemblyContext().setParent(editor.getContext()));
		locale = RWT.getLocale();
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		createControl(container).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setValue(AUtil.readValue(input, assemblyConfig.getName(), fieldConfig.getName(), null));
		container.addListener(SWT.Dispose, e -> dispose());
		return container;
	}

	@Override
	protected Control createControl(Composite parent) {

		control = new RichTextEditor(parent, SWT.BORDER);

		// �����ı��Ƿ�ֻ��
		control.setEditable(!isReadOnly());

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
		if (saveCheck && fieldConfig.isRequired() && text.isEmpty()) {
			throw new Exception(fieldConfig.getFieldText() + "���");
		}
	}

}
