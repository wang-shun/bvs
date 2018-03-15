package com.bizvisionsoft.bruiengine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;

public class BruiAssemblyContext implements IBruiContext {

	private List<IBruiContext> children;

	private BruiAssemblyEngine engine;

	private Assembly assembly;

	private IPostSelectionProvider selectionProvider;

	private IBruiContext parentContext;

	public BruiAssemblyContext() {
		children = new ArrayList<IBruiContext>();
	}

	public BruiAssemblyContext setParent(IBruiContext parentContext) {
		this.parentContext = parentContext;
		return this;
	}

	public IBruiContext add(IBruiContext context) {
		children.add(context);
		return this;
	}

	public BruiAssemblyContext setAssembly(Assembly assembly) {
		this.assembly = assembly;
		return this;
	}

	public void dispose() {
		children.forEach(c -> c.dispose());
		children = null;
	}

	public IBruiContext setEngine(BruiAssemblyEngine engine) {
		this.engine = engine;
		return this;
	}

	public Object getContent(String name) {
		return engine.getContent(name);
	}

	public IBruiContext getContext(String name) {
		if (assembly == null) {
			return null;
		}
		if (assembly.getName().equals(name)) {
			return this;
		}
		for (int i = 0; i < children.size(); i++) {
			IBruiContext c = children.get(i).getContext(name);
			if (c != null)
				return c;
		}
		return null;
	}

	public Object getContent() {
		return getContent("this");
	}

	public void setSelectionProvider(IPostSelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
	}

	public StructuredSelection getSelection() {
		return Optional.ofNullable(selectionProvider).map(sp -> (StructuredSelection) sp.getSelection())
				.orElse(StructuredSelection.EMPTY);
	}

	public Object getFristElement() {
		return getSelection().getFirstElement();
	}

	public void ifFristElementSelected(Consumer<? super Object> consumer) {
		Optional.ofNullable(selectionProvider).map(sp -> (StructuredSelection) sp.getSelection())
				.map(sel -> sel.getFirstElement()).ifPresent(consumer);
	}

	public IBruiContext getParentContext() {
		return parentContext;
	}
	
	@Override
	public void remove(IBruiContext childContext) {
		if(childContext != null) {
			children.remove(childContext);
		}
	}

}
