package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.model.FormField;

public class RadioField extends EditorField {

	private Composite control;
	private List<String> labels;
	private List<Object> choice;
	private Object value;
	private ArrayList<Button> buttons;

	public RadioField() {
	}

	@Override
	protected Control createControl(Composite parent) {


		RowLayout layout = new RowLayout();
		layout.fill = true;
		layout.marginBottom = 0;
		layout.marginLeft = 8;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.wrap = false;
		if (FormField.RADIO_STYLE_CLASSIC.equals(fieldConfig.getRadioStyle())) {
			layout.spacing = 16;
			layout.type = SWT.HORIZONTAL;
			control = new Composite(parent, SWT.BORDER);
			control.setLayout(layout);
		} else if (FormField.RADIO_STYLE_VERTICAL.equals(fieldConfig.getRadioStyle())) {
			layout.spacing = 16;
			layout.type = SWT.VERTICAL;
			control = new Composite(parent, SWT.BORDER);
			control.setLayout(layout);
		} else {
			control = new Composite(parent, SWT.NONE);
			control.setLayout(new FillLayout());
		}
		control.setEnabled(!isReadOnly());
		// 设置选项
		setOptions();

		buttons = new ArrayList<Button>();
		for (int i = 0; i < choice.size(); i++) {
			Button item;
			if (i == 0) {
				if (FormField.RADIO_STYLE_SEGMENT.equals(fieldConfig.getRadioStyle())) {
					item = new Button(control, SWT.TOGGLE);
					item.setData(RWT.CUSTOM_VARIANT, "segmentleft");
				} else {
					item = new Button(control, SWT.RADIO);
				}
			} else if (i == choice.size() - 1) {
				if (FormField.RADIO_STYLE_SEGMENT.equals(fieldConfig.getRadioStyle())) {
					item = new Button(control, SWT.TOGGLE);
					item.setData(RWT.CUSTOM_VARIANT, "segmentright");
				} else {
					item = new Button(control, SWT.RADIO);
				}
			} else {
				if (FormField.RADIO_STYLE_SEGMENT.equals(fieldConfig.getRadioStyle())) {
					item = new Button(control, SWT.TOGGLE);
					item.setData(RWT.CUSTOM_VARIANT, "segment");
				} else {
					item = new Button(control, SWT.RADIO);
				}
			}
			item.setText(labels.get(i));
			item.setData(choice.get(i));
			if (!isReadOnly()) {
				item.addListener(SWT.Selection, e -> {
					try {
						this.value = e.widget.getData();
						for (int j = 0; j < buttons.size(); j++)
							buttons.get(j).setSelection(j == choice.indexOf(value));
						writeToInput(false);
					} catch (Exception e1) {
						MessageDialog.openError(control.getShell(), "错误", e1.getMessage());
					}
				});
			}
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
			Map<String, Object> options = AUtil.readOptions(input, assemblyConfig.getName(), fieldConfig.getName());
			options.keySet().forEach(k -> {
				labels.add(k);
				choice.add(options.get(k));
			});
		}
	}

	@Override
	public void setValue(Object value) {
		int indexOf = choice.indexOf(value);
		if (indexOf != -1) {
			this.value = value;
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).setSelection(i == indexOf);
			}
		}
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		// 必填检查
		if (saveCheck && fieldConfig.isRequired() && value == null)
			throw new Exception(fieldConfig.getFieldText() + "必填。");
	}

}
