package com.bizvisionsoft.bruiengine.service;

public interface IBruiEditorContext extends IBruiContext{

	Object getInput();

	boolean isEditable();

	void setInput(Object input);

	void setEditable(boolean editable);

	boolean isIgnoreNull();

	boolean isWrapList();

}
