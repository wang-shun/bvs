package com.bizvisionsoft.bruiengine.ui;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.session.UserSession;

public class Popup extends Part {

	protected Assembly assembly;

	protected BruiAssemblyEngine brui;

	private String title;

	private BruiAssemblyContext context;

	public Popup(Assembly assembly, IBruiContext parentContext) {
		super(UserSession.current().getShell());
		this.assembly = assembly;
		setBlockOnOpen(true);
		setShellStyle(SWT.TITLE | SWT.ON_TOP);
		parentContext.add(context = createContext(parentContext).setAssembly(assembly));
		brui = initEngine(assembly);
	}

	private BruiAssemblyEngine initEngine(Assembly assembly) {
		BruiAssemblyEngine brui = BruiAssemblyEngine.newInstance(assembly);
		context.setEngine(brui);
		return brui.init(new IServiceWithId[] { service, context });
	}

	protected BruiAssemblyContext createContext(IBruiContext parentContext) {
		return new BruiAssemblyContext().setParent(parentContext);
	}

	public Assembly getAssembly() {
		return assembly;
	}

	@Override
	protected void createContents(Composite parent) {
		// 4. create
		brui.createUI(parent);

	}

	@Override
	public int open() {
		// 5. before open
		super.open();
		// 6. after open

		int code = Optional.ofNullable(brui.getReturnCode()).orElse(-1);
		setReturnCode(code);
		return code;
	}

	@Override
	public boolean close() {
		// 6. before close
		boolean result = super.close();
		// 7. after close
		return result;
	}

	public Popup setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(title);
		super.configureShell(newShell);
	}

	public BruiAssemblyContext getContext() {
		return context;
	}

}
