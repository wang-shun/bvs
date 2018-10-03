package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.bizivisionsoft.widgets.tools.WidgetHandler;
import com.bizvisionsoft.service.tools.Checker;
import com.bizvisionsoft.service.tools.Formatter;

public class BannerField extends EditorField {

	private Composite control;
	private WidgetHandler handler;

	public BannerField() {
	}

	@Override
	protected Control createControl(Composite parent) {

		control = new Composite(parent, SWT.NONE);
		handler = WidgetHandler.getHandler(control);

		String sh = fieldConfig.getText();
		if (!Checker.isNotAssigned(sh)) {
			setText(sh);
		}

		return control;
	}

	private void setText(String text) {
		handler.setHtmlContent("<blockquote class=\"layui-elem-quote\">"+text+"</blockquote>");
	}

	@Override
	protected Control createTitleLabel(Composite parent) {
		return null;
	}

	@Override
	protected Object getControlLayoutData() {
		GridData gd = (GridData) super.getControlLayoutData();
		gd.horizontalSpan = 2;
		int bh = fieldConfig.getHeight();
		if (bh == 0) {
			gd.heightHint = 64;
		} else {
			gd.heightHint = bh;
		}
		return gd;
	}

	@Override
	public void setValue(Object value) {
		if (fieldConfig.isStaticContent()) {
			return;
		}
		String text = "";
		if (value != null) {
			String format = fieldConfig.getFormat();
			if (format != null && !format.isEmpty()) {
				text = Formatter.getString(value, format, locale);
			} else {
				text = value.toString();
			}
		}

		setText(text);
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	protected void check(boolean saveCheck) throws Exception {
	}

}
