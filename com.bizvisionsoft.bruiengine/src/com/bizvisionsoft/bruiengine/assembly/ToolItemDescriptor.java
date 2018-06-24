package com.bizvisionsoft.bruiengine.assembly;

import java.util.function.Function;

import org.eclipse.swt.widgets.Listener;

public class ToolItemDescriptor {

	public ToolItemDescriptor(String label, String style, Listener listener) {
		this.label = label;
		this.style = style;
		this.listener = listener;
	}

	public ToolItemDescriptor(String label, Listener listener) {
		this.label = label;
		this.listener = listener;
	}

	public String label;

	public String style;

	public Listener listener;

	public Function<Object, Boolean> enablement;

	public ToolItemDescriptor setEnablement(Function<Object, Boolean> enablement) {
		this.enablement = enablement;
		return this;
	}
}
