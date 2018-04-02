package com.bizvisionsoft.bruiengine.service;

import java.util.function.Consumer;

import org.eclipse.jface.viewers.StructuredSelection;

public interface IBruiContext extends IServiceWithId{

	public static String Id = "com.bizvisionsoft.service.contextService";

	public default String getServiceId() {
		return Id;
	}

	void dispose();

	IBruiContext getChildContextByAssemblyName(String name);

	Object getContent();
	
	StructuredSelection getSelection();

	Object getFristElement();

	void selected(Consumer<? super Object> consumer);

	IBruiContext getParentContext();

	IBruiContext add(IBruiContext iBruiContext);

	void remove(IBruiContext iBruiContext);

	IBruiContext getChildContextByName(String name);

}
