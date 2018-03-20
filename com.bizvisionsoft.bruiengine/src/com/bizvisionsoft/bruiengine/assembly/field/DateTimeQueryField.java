package com.bizvisionsoft.bruiengine.assembly.field;

import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.bizivisionsoft.widgets.datetime.DateTime;
import com.bizivisionsoft.widgets.datetime.DateTimeEvent;
import com.bizivisionsoft.widgets.datetime.DateTimeSetting;
import com.mongodb.BasicDBObject;

public class DateTimeQueryField extends EditorField {

	private Control control;
	
	private Date value;

	private Combo operator;
	
	private static final String GT = "大于";

	private static final String GTE = "大于等于";

	private static final String EQ = "等于";

	private static final String LTE = "小于等于";

	private static final String LT = "小于";
	
	private static final String NOT_EQ = "不等于";

	public DateTimeQueryField() {
	}

	@Override
	protected Control createControl(Composite parent) {
		
		Composite pane = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 8;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		pane.setLayout(layout);
		
		operator = new Combo(pane, SWT.READ_ONLY | SWT.BORDER);
		operator.add(GT);
		operator.add(GTE);
		operator.add(EQ);
		operator.add(LTE);
		operator.add(LT);
		operator.add(NOT_EQ);
		operator.select(2);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd.widthHint = 120;
		
		control = new DateTime(pane, DateTimeSetting.dateTime());
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		control.addListener(SWT.Modify, e -> {
			try {
				value = ((DateTimeEvent) e).getDate();
				writeToInput(false);
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
		String op = operator.getText();
		if(value == null) {
			return null;
		}
		if (EQ.equals(op)) {
			return new BasicDBObject("$eq", value);
		} else if (GT.equals(op)) {
			return new BasicDBObject("$gt", value);
		} else if (GTE.equals(op)) {
			return new BasicDBObject("$gte", value);
		} else if (LT.equals(op)) {
			return new BasicDBObject("$lt", value);
		} else if (LTE.equals(op)) {
			return new BasicDBObject("$lte", value);
		} else if (NOT_EQ.equals(op)) {
			return new BasicDBObject("$ne", value);
		}
		return value;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
	}

}
