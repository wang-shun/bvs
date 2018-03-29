package com.bizvisionsoft.bruiengine.service;

public interface IBruiEditorContext extends IBruiContext{

	Object getInput();

	boolean isEditable();

	IBruiEditorContext setInput(Object input);

	IBruiEditorContext setEditable(boolean editable);

	boolean isIgnoreNull();

	IBruiEditorContext setIgnoreNull(boolean ignoreNull);

	IBruiEditorContext setEmbeded(boolean compact);

	boolean isEmbedded();

}
