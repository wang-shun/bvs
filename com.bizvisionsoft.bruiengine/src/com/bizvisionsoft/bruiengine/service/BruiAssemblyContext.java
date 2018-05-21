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

	private String name;

	protected Object input;

	private boolean closeable;

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
		for (int i = 0; i < children.size(); i++)
			children.get(i).dispose();
		children = null;
		if (parentContext != null) {
			parentContext.remove(this);
		}
	}

	public IBruiContext setEngine(BruiAssemblyEngine engine) {
		this.engine = engine;
		return this;
	}

	public Object getContent(String name) {
		return engine.getContent(name);
	}

	public IBruiContext getChildContextByAssemblyName(String assemblyName) {
		if (assembly == null) {
			return null;
		}
		if (assembly.getName().equals(assemblyName)) {
			return this;
		}
		for (int i = 0; i < children.size(); i++) {
			IBruiContext c = children.get(i).getChildContextByAssemblyName(assemblyName);
			if (c != null)
				return c;
		}
		return null;
	}

	public IBruiContext getChildContextByName(String name) {
		if (name == null) {
			return null;
		}
		if (name.equals(this.name)) {
			return this;
		}
		for (int i = 0; i < children.size(); i++) {
			IBruiContext c = children.get(i).getChildContextByName(name);
			if (c != null)
				return c;
		}
		return null;
	}

	public Object getContent() {
		return getContent("this");
	}

	public BruiAssemblyContext setSelectionProvider(IPostSelectionProvider selectionProvider) {
		this.selectionProvider = selectionProvider;
		return this;
	}

	public StructuredSelection getSelection() {
		return Optional.ofNullable(selectionProvider).map(sp -> (StructuredSelection) sp.getSelection())
				.orElse(StructuredSelection.EMPTY);
	}

	public Object getFirstElement() {
		return getSelection().getFirstElement();
	}

	@SuppressWarnings("unchecked")
	public <T> void selected(Consumer<T> consumer) {
		Optional.ofNullable(selectionProvider).map(sp -> (StructuredSelection) sp.getSelection())
				.map(sel -> sel.getFirstElement()).map(m -> (T) m).ifPresent((Consumer<T>) consumer);
	}

	public IBruiContext getParentContext() {
		return parentContext;
	}

	@Override
	public void remove(IBruiContext childContext) {
		if (childContext != null) {
			children.remove(childContext);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IBruiContext setInput(Object input) {
		this.input = input;
		return this;
	}

	public Object getInput() {
		return input;
	}

	public IBruiContext getRoot() {
		return Optional.ofNullable(parentContext).map(p -> p.getRoot()).orElse(this);
	}

	@Override
	public Object getRootInput() {
		return getRoot().getInput();
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public Assembly getAssembly() {
		return assembly;
	}

}
