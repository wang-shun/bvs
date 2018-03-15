package com.bizvisionsoft.bruiengine.assembly.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class InLineWrapper {
	
	public Composite getContainer(Composite parent,int columnCount) {
		Composite container = new Composite(parent,SWT.NONE);
		GridLayout layout = new GridLayout(columnCount,true);
		// layout.fill = true;
		// layout.justify = false;

		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;

		layout.marginWidth = 0;
		layout.marginHeight = 0;
		// layout.spacing = 16;
		// layout.type = SWT.VERTICAL;
		// layout.wrap = false;
		// layout.pack = false;

		layout.horizontalSpacing = 16;
		container.setLayout(layout);
		return container;
	}

}
