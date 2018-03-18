package com.bizvisionsoft.demo.rsclient;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.bizivisionsoft.widgets.Carousel;
import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetContainer;
import com.bizvisionsoft.bruicommons.annotation.Inject;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class CarouselDemo {

	@Inject
	private IBruiService bruiService;

	@GetContainer
	private Composite content;

	@CreateUI
	private void createUI(Composite parent) {
		parent.setLayout(new FillLayout());
		Carousel carousel = new Carousel(parent);

		// 放一个Label
		carousel.addPage(c -> new Label(c, SWT.NONE)).setText("page1");
		carousel.addPage(c -> new Composite(c, SWT.NONE))
				.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));

		carousel.addPage(c -> {
			TableViewer viewer = new TableViewer(c, SWT.BORDER);
			viewer.setContentProvider(ArrayContentProvider.getInstance());
			viewer.setLabelProvider(new LabelProvider());
			viewer.setInput(new String[] { "page3-1", "表格行2", "表格行3", "表格行4" });
			return viewer.getControl();
		});

	}

}
