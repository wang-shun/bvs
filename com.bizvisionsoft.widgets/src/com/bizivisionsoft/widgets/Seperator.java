package com.bizivisionsoft.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class Seperator extends Composite {

	public Seperator(Composite parent, int style) {
		super(parent, SWT.NONE);
		if((SWT.HORIZONTAL & style) != 0) {
			WidgetHandler.getHandler(this)
			.setHtmlContent("<div style='" + "background: -moz-linear-gradient("
					+ "left" 
					+ ", transparent, #d4d4d4,transparent);width: 100%;height: 1px;" + "'/>");
		}else {
			WidgetHandler.getHandler(this)
			.setHtmlContent("<div style='" + "background: -moz-linear-gradient("
					+ "top"
					+ ", transparent, #d4d4d4,transparent);width: 1px;height: 100%;" + "'/>");
		}
	}

}
