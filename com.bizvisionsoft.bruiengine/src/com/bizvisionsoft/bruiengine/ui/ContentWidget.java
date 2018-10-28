package com.bizvisionsoft.bruiengine.ui;

import java.util.Arrays;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.BruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;

public class ContentWidget {

	private BruiService service;

	private AssemblyContainer assemblyContainer;

	private Composite contentContainer;

	private BruiAssemblyContext context;

	private Assembly assembly;

	private Consumer<BruiAssemblyContext> callback;

	// private BruiEngine brui;

	public ContentWidget(Assembly assembly, BruiService service, BruiAssemblyContext parentContext) {
		this.assembly = assembly;
		this.service = service;
		parentContext.add(context = UserSession.newAssemblyContext().setParent(parentContext));
	}

	public ContentWidget createUI(Composite parent, Object input, String parameter, boolean closeable) {
		contentContainer = new Composite(parent, SWT.NONE);
		contentContainer.setLayout(new FillLayout());
		switchAssembly(assembly, input, parameter, closeable);
		contentContainer.setBackground(BruiColors.getColor(BruiColor.Grey_200));
		return this;
	}

	final public void switchAssembly(Assembly assembly, Object input, String parameter, boolean closeable) {
		if (contentContainer == null || contentContainer.isDisposed())
			return;
		Arrays.asList(contentContainer.getChildren()).stream().filter(c -> !c.isDisposed())
				.forEach(ctl -> ctl.dispose());

		assemblyContainer = new AssemblyContainer(contentContainer, context);
		assemblyContainer.setCloseable(closeable);
		assemblyContainer.setInput(input);
		assemblyContainer.setParameter(parameter);
		assemblyContainer.setAssembly(assembly).setServices(service).create();
		contentContainer.layout(true, true);

		if (callback != null) {
			contentContainer.addListener(SWT.Dispose, e -> callback.accept(assemblyContainer.getContext()));
		}
	}

	public Composite getControl() {
		return contentContainer;
	}

	public void setCloseCallback(Consumer<BruiAssemblyContext> callback) {
		this.callback = callback;
	}

}
