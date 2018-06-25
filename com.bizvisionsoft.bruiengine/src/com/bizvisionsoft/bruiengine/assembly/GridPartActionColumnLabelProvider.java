package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;
import java.util.function.Function;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.UserSession;

public class GridPartActionColumnLabelProvider extends ColumnLabelProvider {

	private List<Action> actions;
	private Assembly config;
	private IBruiContext context;
	private Function<Object, Boolean> enablement;

	public GridPartActionColumnLabelProvider(Assembly config, List<Action> actions, IBruiContext context) {
		this.config = config;
		this.actions = actions;
		this.context = context;
	}

	@Override
	public String getText(Object element) {
		if (enablement != null && !Boolean.TRUE.equals(enablement.apply(element))) {
			return "";
		}

		String html = "";
		for (Action action : actions) {
			boolean add = true;
			if (action.isObjectBehavier()) {
				add = isAcceptableBehavior(element, action);
			}
			if (add)
				html += UserSession.bruiToolkit().getActionHtml(action, "a");
			else
				html += UserSession.bruiToolkit().getActionPlaceholderHtml(action);
		}
		return html;

	}

	private boolean isAcceptableBehavior(Object element, Action action) {
		String[] paramemterNames = new String[] { MethodParam.CONTEXT_INPUT_OBJECT,
				MethodParam.CONTEXT_INPUT_OBJECT_ID, MethodParam.ROOT_CONTEXT_INPUT_OBJECT,
				MethodParam.ROOT_CONTEXT_INPUT_OBJECT_ID, MethodParam.CURRENT_USER, MethodParam.CURRENT_USER_ID };
		Object[] parameterValues = context.getContextParameters(paramemterNames);

		return AUtil.readBehavior(element, config.getName(), action.getName(), parameterValues, paramemterNames);
	}

	public CellLabelProvider setEnablement(Function<Object, Boolean> enablement) {
		this.enablement = enablement;
		return this;
	}

}
