package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.scripting.ClientListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NumberRangeField extends EditorField {

	private Text from;

	private Text to;

	public NumberRangeField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite pane = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 8;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		pane.setLayout(layout);
		from = new Text(pane, SWT.BORDER);
		new Label(pane, SWT.NONE).setText(" - ");
		to = new Text(pane, SWT.BORDER);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 120;
		from.setLayoutData(gd);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 120;
		to.setLayoutData(gd);

		Optional.ofNullable(fieldConfig.getTextMessage()).ifPresent(o -> from.setMessage(o));

		Optional.ofNullable(fieldConfig.getTextMessage2()).ifPresent(o -> to.setMessage(o));

		from.setEditable(!isReadOnly());
		to.setEditable(!isReadOnly());

		// �����������
		from.addListener(SWT.Modify, getVerfifyListener());
		to.addListener(SWT.Modify, getVerfifyListener());

		// �����޸�
		from.addListener(SWT.FocusOut, e -> {
			try {
				writeToInput(false);
			} catch (Exception e1) {
				MessageDialog.openError(from.getShell(), "����", e1.getMessage());
			}
		});

		to.addListener(SWT.FocusOut, e -> {
			try {
				writeToInput(false);
			} catch (Exception e1) {
				MessageDialog.openError(from.getShell(), "����", e1.getMessage());
			}
		});

		return pane;
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Number[] && ((Number[]) value).length == 2) {
			from.setText(((Number[]) value)[0] == null ? "" : ((Number[]) value)[0].toString());
			to.setText(((Number[]) value)[1] == null ? "" : ((Number[]) value)[1].toString());
		} else {
			from.setText("");
			to.setText("");
		}
	}

	@Override
	public Object getValue() {
		String fromText = from.getText().trim();
		String toText = to.getText().trim();
		Double _from = null;
		Double _to = null;
		try {
			if (!fromText.isEmpty())
				_from = Double.parseDouble(fromText);
			if (!toText.isEmpty())
				_to = Double.parseDouble(toText);
		} catch (Exception e) {
		}
		return new Double[] { _from, _to };
	}

	private ClientListener getVerfifyListener() {
		String js = "var handleEvent = function(event) {"
				+ "	var reg1 = /^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$/;" + "	var reg2 = /^-?[1-9]\\d*$/;"
				+ "	if (reg1.test(event.widget.getText())||reg2.test(event.widget.getText())) {"
				+ "		event.widget.setBackground(null);" + "		event.widget.setToolTipText(null);" + "	} else {"
				+ "		event.widget.setBackground([ 255, 152, 0  ]);"
				+ "		event.widget.setToolTipText(\"��������Ϸ�����ֵ\");" + "	}" + "};";
		return new ClientListener(js);
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		if (!saveCheck)
			return;

		String fromText = from.getText().trim();
		String toText = to.getText().trim();
		// ������
		if (fieldConfig.isRequired() && fromText.isEmpty() && toText.isEmpty()) {
			throw new Exception(fieldConfig.getFieldText() + "���");
		}
		// ���ͼ��
		// ������
		Float _from = null;
		Float _to = null;
		try {
			if (!fromText.isEmpty())
				_from = Float.parseFloat(fromText);
			if (!toText.isEmpty())
				_to = Float.parseFloat(toText);
		} catch (Exception e) {
			throw new Exception(fieldConfig.getFieldText() + "Ҫ��������ֵ��");
		}

		if (_from != null && _to != null && _from > _to)
			throw new Exception(fieldConfig.getFieldText() + "��Сֵ����С�����ֵ��");

		if (Boolean.TRUE.equals(fieldConfig.getTextRangeLimitMaxValue())) {
			// ��������ֵ�޶�
			Integer max = fieldConfig.getTextRangeMaxValue();
			if (max != null && (_to == null || _to > max)) {
				throw new Exception(fieldConfig.getFieldText() + "�������ֵ�����ƣ�" + max);
			}
		}
		if (Boolean.TRUE.equals(fieldConfig.getTextRangeLimitMinValue())) {
			// ��������ֵ�޶�
			Integer min = fieldConfig.getTextRangeMinValue();
			if (min != null && (_from == null || _from < min)) {
				throw new Exception(fieldConfig.getFieldText() + "������Сֵ�����ƣ�" + min);
			}
		}

	}

}
