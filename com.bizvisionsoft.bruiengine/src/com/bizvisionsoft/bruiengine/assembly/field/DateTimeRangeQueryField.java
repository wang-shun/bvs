package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Date;
import java.util.Optional;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.bizivisionsoft.widgets.datetime.DateTime;
import com.bizivisionsoft.widgets.datetime.DateTimeSetting;
import com.bizvisionsoft.bruicommons.model.FormField;
import com.mongodb.BasicDBObject;

public class DateTimeRangeQueryField extends EditorField {

	private DateTime range;
	private Date value;
	private Date endValue;

	public DateTimeRangeQueryField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite pane = new Composite(parent, SWT.NONE);

		pane.setLayout(new FillLayout());
		DateTimeSetting setting;
		String type = fieldConfig.getDateType();
		if (FormField.DATE_TYPE_YEAR.equals(type)) {
			setting = DateTimeSetting.year();
		} else if (FormField.DATE_TYPE_YEAR_MONTH.equals(type)) {
			setting = DateTimeSetting.yearMonth();
		} else if (FormField.DATE_TYPE_MONTH.equals(type)) {
			setting = DateTimeSetting.month();
		} else if (FormField.DATE_TYPE_TIME.equals(type)) {
			setting = DateTimeSetting.time();
		} else if (FormField.DATE_TYPE_DATETIME.equals(type)) {
			setting = DateTimeSetting.dateTime();
		} else {
			setting = DateTimeSetting.date();
		}
		
		range = new DateTime(pane, setting.setRange(true));
		range.addListener(SWT.Modify, e -> {
			try {
				value = range.getDate();
				endValue = range.getEndDate();
			} catch (Exception e1) {
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", e1.getMessage());
			}
		});
		return pane;
	}

	@Override
	public void setValue(Object value) {
	}

	@Override
	public Object getValue() {
		BasicDBObject filter = null;
		if (value != null)
			filter = new BasicDBObject("$gte", value);
		if (endValue != null)
			filter = Optional.ofNullable(filter).orElse(new BasicDBObject()).append("$lte", endValue);
		return filter;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
		if (value != null && endValue != null && value.after(endValue))
			throw new Exception(fieldConfig.getFieldText() + "最小值必须小于最大值。");
	}

}
