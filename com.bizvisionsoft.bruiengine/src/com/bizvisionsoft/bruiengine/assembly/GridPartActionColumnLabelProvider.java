package com.bizvisionsoft.bruiengine.assembly;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.model.User;

public class GridPartActionColumnLabelProvider extends ColumnLabelProvider {

	private List<Action> actions;
	private Assembly config;
	private IBruiContext context;

	public GridPartActionColumnLabelProvider(Assembly config, List<Action> actions, IBruiContext context) {
		this.config = config;
		this.actions = actions;
		this.context = context;
	}

	@Override
	public String getText(Object element) {
		String html = "";
		for (Action action : actions) {
			boolean add = true;
			if (action.isObjectBehavier()) {
				Field f = AUtil.getContainerField(element.getClass(), Behavior.class, config.getName(),
						action.getName(), a -> a.value()).orElse(null);
				if (f != null) {
					f.setAccessible(true);
					try {
						add = Boolean.TRUE.equals(f.get(element));
					} catch (IllegalArgumentException | IllegalAccessException e) {
					}
				} else {
					Method m = AUtil.getContainerMethod(element.getClass(), Behavior.class, config.getName(),
							action.getName(), a -> a.value()).orElse(null);
					if (m != null) {
						String[] paramemterNames = new String[] { ServiceParam.CONTEXT_INPUT_OBJECT,
								ServiceParam.CONTEXT_INPUT_OBJECT_ID, ServiceParam.ROOT_CONTEXT_INPUT_OBJECT,
								ServiceParam.ROOT_CONTEXT_INPUT_OBJECT_ID, ServiceParam.CURRENT_USER,
								ServiceParam.CURRENT_USER_ID };
						Object input = context.getInput();
						Object rootInput = context.getRootInput();
						User user = Brui.sessionManager.getSessionUserInfo();
						Object[] parameterValues = new Object[] { input, Util.getBson(input).get("_id"), rootInput,
								Util.getBson(rootInput).get("_id"), user, user.getUserId() };
						Object value = AUtil.invokeMethodInjectParams(element, m, parameterValues, paramemterNames,
								ServiceParam.class, f1 -> f1.value());
						add = Boolean.TRUE.equals(value);
					}
				}
			}
			if (add)
				html += UserSession.bruiToolkit().getActionHtml(action, "a");
			else
				html += UserSession.bruiToolkit().getActionPlaceholderHtml(action);
		}
		return html;

	}

}
