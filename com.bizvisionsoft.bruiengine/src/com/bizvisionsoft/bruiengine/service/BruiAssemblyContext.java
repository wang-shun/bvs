package com.bizvisionsoft.bruiengine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.BruiAssemblyEngine;
import com.bizvisionsoft.mongocodex.tools.BsonTools;
import com.bizvisionsoft.service.model.User;

public class BruiAssemblyContext implements IBruiContext {

	private List<IBruiContext> children;

	private BruiAssemblyEngine engine;

	private Assembly assembly;

	private IPostSelectionProvider selectionProvider;

	private IBruiContext parentContext;

	private String name;

	protected Object input;

	private boolean closeable;

	private boolean contentPage;

	private boolean disposed;

	BruiAssemblyContext() {
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
		if (!disposed) {
			if (children != null) {
				for (int i = 0; i < children.size(); i++) {
					children.get(i).dispose();
				}
				children = null;
				if (parentContext != null) {
					parentContext.remove(this);
				}
			}
			input = null;
			disposed = true;
		}
	}

	public IBruiContext setEngine(BruiAssemblyEngine engine) {
		this.engine = engine;
		return this;
	}

	public Object getContent(String name) {
		return engine == null ? null : engine.getContent(name);
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
		if (childContext != null && children != null) {
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

	public IBruiContext getContentPageContext() {
		if (isContentPage()) {
			return this;
		} else {
			return Optional.ofNullable(parentContext).map(p -> p.getContentPageContext()).orElse(null);
		}
	}

	public Object getContentPageInput() {
		return Optional.ofNullable(getContentPageContext()).map(p -> p.getInput()).orElse(null);
	}

	@Override
	public Object getRootInput() {
		return getRoot().getInput();
	}

	public <T> T getRootInput(Class<T> clas, boolean nullAble) {
		return getRoot().getInput(clas, nullAble);
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	public boolean isCloseable() {
		return closeable;
	}

	@Override
	public Assembly getAssembly() {
		return assembly;
	}

	public BruiAssemblyContext setContentPage(boolean contentPage) {
		this.contentPage = contentPage;
		return this;
	}

	public boolean isContentPage() {
		return contentPage;
	}

	@SuppressWarnings("unchecked")
	public <T> T getInput(Class<T> clas, boolean nullAble) {
		if (nullAble && input == null) {
			return null;
		} else if (!nullAble && input == null) {
			throw new RuntimeException("组件" + assembly.getName() + "的输入为空。");
		} else if (clas.isAssignableFrom(input.getClass())) {
			return (T) input;
		} else {
			throw new RuntimeException("组件" + assembly.getName() + "的输入类型不匹配，要求是" + AUtil.readType(input));
		}
	}

	@Override
	public Object[] getContextParameters(String[] paramemterNames) {
		Object contextInput = getInput();
		Object rootInput = getRootInput();
		Object pageInput = getContentPageInput();
		User user = Brui.sessionManager.getUser();

		Object[] result = new Object[paramemterNames.length];

		for (int i = 0; i < result.length; i++) {
			if (MethodParam.CONTEXT_INPUT_OBJECT.equals(paramemterNames[i])) {
				result[i] = contextInput;
			} else if (MethodParam.CONTEXT_INPUT_OBJECT_ID.equals(paramemterNames[i])) {
				result[i] = Optional.ofNullable(contextInput).map(m -> BsonTools.getBson(m).get("_id")).orElse(null);
			} else if (MethodParam.ROOT_CONTEXT_INPUT_OBJECT.equals(paramemterNames[i])) {
				result[i] = rootInput;
			} else if (MethodParam.ROOT_CONTEXT_INPUT_OBJECT_ID.equals(paramemterNames[i])) {
				result[i] = Optional.ofNullable(rootInput).map(m -> BsonTools.getBson(m).get("_id")).orElse(null);
			} else if (MethodParam.CURRENT_USER.equals(paramemterNames[i])) {
				result[i] = user;
			} else if (MethodParam.CURRENT_USER_ID.equals(paramemterNames[i])) {
				result[i] = user.getUserId();
			} else if (Execute.PARAM_CONTEXT.equals(paramemterNames[i])) {
				result[i] = this;
			} else if (Execute.PAGE_CONTEXT_INPUT_OBJECT.equals(paramemterNames[i])) {
				result[i] = pageInput;
			} else if (MethodParam.PAGE_CONTEXT_INPUT_OBJECT_ID.equals(paramemterNames[i])) {
				result[i] = Optional.ofNullable(pageInput).map(m -> BsonTools.getBson(m).get("_id")).orElse(null);
			}

		}

		return result;
	}

	@Override
	public <T> T search_sele_root(Class<T> clas) {
		return search(clas, SEARCH_NO_HIERARCHY, SEARCH_STRATEGY_SELECTED, SEARCH_STRATEGY_ROOT_INPUT);
	}

	@Override
	public <T> T search(Class<T> clas, int dir, int... strategy) {
		T result = null;
		for (int i = 0; i < strategy.length; i++) {
			switch (strategy[i]) {
			case SEARCH_STRATEGY_ROOT_INPUT:
				result = convertResult(clas, getRootInput());
				if (result != null)
					return result;
				break;
			case SEARCH_STRATEGY_SELECTED:
				result = convertResult(clas,
						Optional.ofNullable(getSelection()).map(s -> s.getFirstElement()).orElse(null));
				if (result != null)
					return result;
				break;
			case SEARCH_STRATEGY_INPUT:
				result = convertResult(clas, getInput());
				if (result != null)
					return result;
				break;
			default:
				break;
			}
		}

		switch (dir) {
		case SEARCH_UP:
			IBruiContext parent = getParentContext();
			while (parent != null) {
				result = parent.search(clas, SEARCH_UP, strategy);
				if (result != null) {
					return result;
				}
				parent = getParentContext();
			}
			break;
		case SEARCH_DOWN:
			for (int i = 0; i < children.size(); i++) {
				result = children.get(i).search(clas, SEARCH_UP, strategy);
				if (result != null) {
					return result;
				}
			}
			break;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private <T> T convertResult(Class<T> clas, Object data) {
		if (data != null && clas.isAssignableFrom(data.getClass()))
			return (T) data;
		return null;
	}

}
