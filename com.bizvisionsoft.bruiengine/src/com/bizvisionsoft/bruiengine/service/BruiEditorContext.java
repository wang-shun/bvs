package com.bizvisionsoft.bruiengine.service;

public class BruiEditorContext extends BruiAssemblyContext implements IBruiEditorContext {

	private boolean editable;
	private Object input;
	private boolean ignoreNull;
	private boolean embedded;

	public BruiEditorContext() {
		super();
		editable = true;
		ignoreNull = true;
	}

	public BruiEditorContext setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}

	public BruiEditorContext setInput(Object input) {
		this.input = input;
		return this;
	}

	public boolean isEditable() {
		return editable;
	}

	public Object getInput() {
		return input;
	}

	public BruiEditorContext setIgnoreNull(boolean ignoreNull) {
		this.ignoreNull = ignoreNull;
		return this;
	}

	public boolean isIgnoreNull() {
		return ignoreNull;
	}

	public BruiEditorContext setEmbeded(boolean compact) {
		this.embedded = compact;
		return this;
	}

	public boolean isEmbedded() {
		return embedded;
	}

}
