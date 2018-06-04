package com.bizvisionsoft.bruiengine.service;

import java.util.function.Consumer;

import org.eclipse.jface.viewers.StructuredSelection;

import com.bizvisionsoft.bruicommons.model.Assembly;

public interface IBruiContext extends IServiceWithId {

	public static String Id = "com.bizvisionsoft.service.contextService";

	public default String getServiceId() {
		return Id;
	}

	void dispose();

	IBruiContext getChildContextByAssemblyName(String name);

	Object getContent();

	StructuredSelection getSelection();

	Object getFirstElement();

	<T> void selected(Consumer<T> consumer);

	IBruiContext getParentContext();

	IBruiContext add(IBruiContext iBruiContext);

	void remove(IBruiContext iBruiContext);

	IBruiContext getChildContextByName(String name);

	IBruiContext setInput(Object input);

	Object getInput();

	IBruiContext getRoot();

	Object getRootInput();

	void setCloseable(boolean closeable);

	boolean isCloseable();

	Assembly getAssembly();

	BruiAssemblyContext setContentPage(boolean contentPage);

	boolean isContentPage();

	IBruiContext getContentPageContext();

	Object getContentPageInput();

	<T> T getInput(Class<T> checkClass, boolean nullAble);
	
	<T> T getRootInput(Class<T> checkClass, boolean nullAble);


}
