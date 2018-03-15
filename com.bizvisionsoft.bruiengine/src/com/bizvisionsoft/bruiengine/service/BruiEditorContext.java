package com.bizvisionsoft.bruiengine.service;

public class BruiEditorContext extends BruiAssemblyContext implements IBruiEditorContext{

	private boolean editable;
	private Object input;

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setInput(Object input) {
		this.input = input;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public Object getInput() {
		return input;
	}

}
