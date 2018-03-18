package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.bizvisionsoft.bruicommons.model.FormField;
import com.bizvisionsoft.bruiengine.BruiEngine;

public class MultiCheckQueryField extends EditorField {

	private Composite control;
	private List<String> labels;
	private List<Object> choice;
	private List<Object> value;
	private ArrayList<Button> buttons;

	public MultiCheckQueryField() {
		value = new ArrayList<Object>();
	}

	@Override
	protected Control createControl(Composite parent) {

		control = new Composite(parent, SWT.NONE);

		//////////////////////////////////////////////////////////////////////////////////////
		// 读取配置进行设置

		// 设置文本是否只读
		control.setEnabled(!isReadOnly());

		// 设置选项
		setOptions();

		// 创建选项

		RowLayout layout = new RowLayout();
		control.setLayout(layout);
		layout.fill = true;
		layout.marginBottom = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.wrap = false;
		if (FormField.RADIO_STYLE_CLASSIC.equals(fieldConfig.getRadioStyle())) {
			layout.spacing = 16;
			layout.type = SWT.HORIZONTAL;
		} else if (FormField.RADIO_STYLE_VERTICAL.equals(fieldConfig.getRadioStyle())) {
			layout.spacing = 16;
			layout.type = SWT.VERTICAL;
		} else {
			layout.spacing = 0;
		}

		buttons = new ArrayList<Button>();
		for (int i = 0; i < choice.size(); i++) {
			Button item;
			if (i == 0) {
				if (FormField.RADIO_STYLE_SEGMENT.equals(fieldConfig.getRadioStyle())) {
					item = new Button(control, SWT.TOGGLE);
					item.setData(RWT.CUSTOM_VARIANT, "segmentleft");
				} else {
					item = new Button(control, SWT.CHECK);
				}
			} else if (i == choice.size() - 1) {
				if (FormField.RADIO_STYLE_SEGMENT.equals(fieldConfig.getRadioStyle())) {
					item = new Button(control, SWT.TOGGLE);
					item.setData(RWT.CUSTOM_VARIANT, "segmentright");
				} else {
					item = new Button(control, SWT.CHECK);
				}
			} else {
				if (FormField.RADIO_STYLE_SEGMENT.equals(fieldConfig.getRadioStyle())) {
					item = new Button(control, SWT.TOGGLE);
					item.setData(RWT.CUSTOM_VARIANT, "segment");
				} else {
					item = new Button(control, SWT.CHECK);
				}
			}
			item.setText(labels.get(i));
			item.setData(choice.get(i));
			item.addListener(SWT.Selection, e -> {
				try {
					value.clear();
					buttons.stream().filter(b -> b.getSelection()).forEach(s -> value.add(s.getData()));
					writeToInput(false);
				} catch (Exception e1) {
					MessageDialog.openError(control.getShell(), "错误", e1.getMessage());
				}
			});
			buttons.add(item);
		}

		// 设置修改
		return control;
	}

	private void setOptions() {
		labels = new ArrayList<String>();
		choice = new ArrayList<Object>();
		// 1. 根据配置设置选项
		String opt = fieldConfig.getOptionText();
		String opv = fieldConfig.getOptionValue();
		if (opt != null && !opt.isEmpty()) {
			Arrays.asList(opt.split("#")).forEach(s -> labels.add(s.trim()));
			if (opv != null && !opv.trim().isEmpty()) {
				Arrays.asList(opv.split("#")).forEach(s -> choice.add(s.trim()));
			} else {
				labels.forEach(s -> choice.add(s.trim()));
			}
		} else {
			Map<String, Object> options = BruiEngine.readOptions(input, assemblyConfig.getName(),
					fieldConfig.getName());
			options.keySet().forEach(k -> {
				labels.add(k);
				choice.add(options.get(k));
			});
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		if (value instanceof List<?>)
			this.value = (List<Object>) value;
		else
			this.value = new ArrayList<Object>();
		presentation();
	}

	protected void presentation() {
		buttons.forEach(b -> b.setSelection(false));
		this.value.forEach(v -> {
			int idx = choice.indexOf(v);
			buttons.get(idx).setSelection(idx != -1);
		});
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		// 必填检查
		if (saveCheck &&  fieldConfig.isRequired() && (value == null || value.isEmpty())) {
			throw new Exception(fieldConfig.getFieldText() + "必填。");
		}
	}

}
