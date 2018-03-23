package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.bizvisionsoft.annotations.AUtil;

public class ComboField extends EditorField {

	private Combo control;
	private List<String> labels;
	private ArrayList<Object> values;

	public ComboField() {
	}

	@Override
	protected Control createControl(Composite parent) {

		control = new Combo(parent,SWT.BORDER | SWT.READ_ONLY);

		//////////////////////////////////////////////////////////////////////////////////////
		// ��ȡ���ý�������

		// �����ı��Ƿ�ֻ��
		control.setEnabled(!isReadOnly());

		// ����ѡ��
		setOptions();

		control.addListener(SWT.Selection, e -> {
			try {
				writeToInput(false);
			} catch (Exception e1) {
				MessageDialog.openError(control.getShell(), "����", e1.getMessage());
			}
		});

		// �����޸�
		return control;
	}

	private void setOptions() {
		labels = new ArrayList<String>();
		values = new ArrayList<Object>();
		// 1. ������������ѡ��
		String opt = fieldConfig.getOptionText();
		String opv = fieldConfig.getOptionValue();
		if (opt != null && !opt.isEmpty()) {
			Arrays.asList(opt.split("#")).forEach(s -> labels.add(s.trim()));
			if (opv != null && !opv.trim().isEmpty()) {
				Arrays.asList(opv.split("#")).forEach(s -> values.add(s.trim()));
			} else {
				labels.forEach(s -> values.add(s.trim()));
			}
			labels.forEach(s -> control.add(s));
		} else {
			Map<String, Object> options = AUtil.readOptions(input, assemblyConfig.getName(),
					fieldConfig.getName());
			options.keySet().forEach(k -> {
				labels.add(k);
				control.add(k);
				values.add(options.get(k));
			});
		}
	}

	@Override
	public void setValue(Object value) {
		control.select(values.indexOf(value));
	}

	@Override
	public Object getValue() {
		int idx = control.getSelectionIndex();
		if (idx == -1)
			return null;
		else
			return values.get(idx);
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		// ������
		if (saveCheck && fieldConfig.isRequired() && control.getSelectionIndex() == -1) {
			throw new Exception(fieldConfig.getFieldText() + "���");
		}

	}

}
