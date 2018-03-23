package com.bizvisionsoft.bruiengine.assembly.field;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.datetime.DateTime;
import com.bizivisionsoft.widgets.datetime.DateTimeSetting;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.model.FormField;

public class DateTimeField extends EditorField {

	private Control control;
	private Date value;

	public DateTimeField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		if (isReadOnly()) {
			return createText(parent);
		} else {
			return createDateTime(parent);
		}
	}

	private Control createDateTime(Composite parent) {
		DateTimeSetting setting;
		String type = fieldConfig.getDateType();
		if (FormField.DATE_TYPE_YEAR.equals(type)) {
			setting = DateTimeSetting.year();
		} else if (FormField.DATE_TYPE_MONTH.equals(type)) {
			setting = DateTimeSetting.month();
		} else if (FormField.DATE_TYPE_TIME.equals(type)) {
			setting = DateTimeSetting.time();
		} else if (FormField.DATE_TYPE_DATETIME.equals(type)) {
			setting = DateTimeSetting.dateTime();
		} else {
			setting = DateTimeSetting.date();
		}

		// 获取mark
		Map<String, Object> marks = AUtil.readOptions(input, assemblyConfig.getName(), fieldConfig.getName());
		if (marks != null) {
			marks.keySet().forEach(k -> {
				setting.addMark(k, marks.get(k).toString());
			});
		}

		// 最大值和最小值
		Object validation = AUtil.readValidation(input, assemblyConfig.getName(), fieldConfig.getName());
		if (validation instanceof String[]) {
			if (((String[]) validation).length > 0)
				setting.setMin(((String[]) validation)[0]);
			if (((String[]) validation).length > 1)
				setting.setMax(((String[]) validation)[1]);
		}

		control = new DateTime(parent, setting);

		control.setEnabled(!isReadOnly());

		control.addListener(SWT.Modify, e -> {
			try {
				value = (Date) e.data;
				writeToInput(false);
			} catch (Exception e1) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", e1.getMessage());
			}
		});
		return control;
	}

	private Control createText(Composite parent) {
		control = new Text(parent, SWT.BORDER);
		((Text) control).setEditable(false);
		return control;
	}

	@Override
	public void setValue(Object value) {
		this.value = (Date) value;
		if (control instanceof DateTime) {
			((DateTime) control).setDate((Date) value);
		} else {
			if (value == null) {
				((Text) control).setText("");
			} else {
				String type = fieldConfig.getDateType();
				if (FormField.DATE_TYPE_YEAR.equals(type)) {
					((Text) control).setText(new SimpleDateFormat(DateTimeSetting.FORMAT_YEAR).format((Date) value));
				} else if (FormField.DATE_TYPE_MONTH.equals(type)) {
					((Text) control).setText(new SimpleDateFormat(DateTimeSetting.FORMAT_MONTH).format((Date) value));
				} else if (FormField.DATE_TYPE_TIME.equals(type)) {
					((Text) control).setText(new SimpleDateFormat(DateTimeSetting.FORMAT_TIME).format((Date) value));
				} else if (FormField.DATE_TYPE_DATETIME.equals(type)) {
					((Text) control)
							.setText(new SimpleDateFormat(DateTimeSetting.FORMAT_DATETIME).format((Date) value));
				} else {
					((Text) control).setText(new SimpleDateFormat(DateTimeSetting.FORMAT_DATE).format((Date) value));
				}
			}
		}
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		if (saveCheck && fieldConfig.isRequired() && value == null) {
			throw new Exception(fieldConfig.getFieldText() + "必填。");
		}
	}

}
