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
	 *            ������Ҫ���ص���
	 * @param dir
	 *            ���ҷ���SEARCH_UP ���ϼ�������, SEARCH_DOWN ���¼������Ĳ���,SEARCH_NO_HIERARCHY
	 *            ֻ���ұ��㡣��Ҫ���ڲ���input��
	 * @param strategy
	 *            ���ҵ�˳��SEARCH_STRATEGY_SELECTED ѡ�ж���SEARCH_STRATEGY_ROOT_INPUT
	 *            ���Ҹ���SEARCH_STRATEGY_INPUT ���ұ���input
	 * @return
	 */
	<T> T search(Class<T> clas, int dir, int... strategy);

	<T> T search_sele_root(Class<T> clas);

	Optional<Object> searchContent(Predicate<IBruiContext> predicate, int dir);

	void traversalContext(int dir, Consumer<IBruiContext> consumer);

	Stream<IBruiContext> stream(int dir);

	Stream<IBruiContext> parallelStream(int dir);
}
