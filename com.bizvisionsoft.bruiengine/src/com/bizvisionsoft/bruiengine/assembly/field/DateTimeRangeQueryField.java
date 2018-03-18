package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Date;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.bizivisionsoft.widgets.DateTime;
import com.bizivisionsoft.widgets.DateTimeEvent;
import com.bizivisionsoft.widgets.DateTimeSetting;
import com.mongodb.BasicDBObject;

public class DateTimeRangeQueryField extends EditorField {

	// private Button control;

	private DateTime from;
	private DateTime to;
	private Date value;
	private Date endValue;

	public DateTimeRangeQueryField() {
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
		from = new DateTime(pane, DateTimeSetting.dateTime());
		new Label(pane, SWT.NONE).setText(" - ");
		to = new DateTime(pane, DateTimeSetting.dateTime());

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 120;
		from.setLayoutData(gd);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.widthHint = 120;
		to.setLayoutData(gd);

		from.addListener(SWT.Modify, e -> {
			value = ((DateTimeEvent) e).getDate();
		});
		to.addListener(SWT.Modify, e -> {
			endValue = ((DateTimeEvent) e).getDate();
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
