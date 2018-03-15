package com.bizvisionsoft.bruiengine.ui;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ContentArea;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.BruiColors;
import com.bizvisionsoft.bruiengine.BruiColors.BruiColor;
import com.bizvisionsoft.bruiengine.service.BruiService;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;

public class ContentWidget {

	private ContentArea contentArea;

	private BruiService service;

	private AssemblyContainer assemblyContainer;

	private Composite contentContainer;

	// private BruiEngine brui;

	public ContentWidget(ContentArea contentArea, BruiService service) {
		this.contentArea = contentArea;
		this.service = service;
		service.setContentWidget(this);
	}

	public ContentWidget createUI(Composite parent) {
		contentContainer = new Composite(parent, SWT.NONE);
		contentContainer.setLayout(new FillLayout());
		Assembly assembly = Brui.site.getAssembly(contentArea.getAssemblyLinks().stream()
				.filter(al -> al.isDefaultAssembly()).findFirst().orElseThrow(NoSuchElementException::new).getId());
		switchAssembly(assembly);
		contentContainer.setBackground(BruiColors.getColor(BruiColor.Grey_200));
		return this;
	}

	final public void switchAssembly(Assembly assembly) {
		if (contentContainer == null || contentContainer.isDisposed())
			return;
		Arrays.asList(contentContainer.getChildren()).stream().filter(c -> !c.isDisposed())
				.forEach(ctl -> ctl.dispose());

		assemblyContainer = new AssemblyContainer(contentContainer,new BruiAssemblyContext());
		assemblyContainer.setAssembly(assembly).setServices(service).create();
		contentContainer.layout(true, true);
	}

	public Composite getControl() {
		return contentContainer;
	}
}
