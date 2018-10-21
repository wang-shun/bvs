package com.bizvisionsoft.bruiengine.service;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

	Object[] getContextParameters(String[] paramemterNames);

	public final int SEARCH_STRATEGY_SELECTED = 0;

	public final int SEARCH_STRATEGY_ROOT_INPUT = 1;

	public final int SEARCH_STRATEGY_INPUT = 2;

	public final int SEARCH_UP = 1;

	public final int SEARCH_NO_HIERARCHY = 0;

	public final int SEARCH_DOWN = 2;

	/**
	 * 
	 * @param clas
	 *            查找需要返回的类
	 * @param dir
	 *            查找方向，SEARCH_UP 向上级上下文, SEARCH_DOWN 向下级上下文查找,SEARCH_NO_HIERARCHY
	 *            只查找本层。主要用于查找input。
	 * @param strategy
	 *            查找的顺序，SEARCH_STRATEGY_SELECTED 选中对象，SEARCH_STRATEGY_ROOT_INPUT
	 *            查找根，SEARCH_STRATEGY_INPUT 查找本层input
	 * @return
	 */
	<T> T search(Class<T> clas, int dir, int... strategy);

	<T> T search_sele_root(Class<T> clas);

	Optional<Object> searchContent(Predicate<IBruiContext> predicate, int dir);

	void traversalContext(int dir, Consumer<IBruiContext> consumer);

	Stream<IBruiContext> stream(int dir);

	Stream<IBruiContext> parallelStream(int dir);
}
