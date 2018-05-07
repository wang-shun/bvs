package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;
import java.util.Optional;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.bizvisionsoft.annotations.AUtil;
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
				String[] paramemterNames = new String[] { ServiceParam.CONTEXT_INPUT_OBJECT,
						ServiceParam.CONTEXT_INPUT_OBJECT_ID, ServiceParam.ROOT_CONTEXT_INPUT_OBJECT,
						ServiceParam.ROOT_CONTEXT_INPUT_OBJECT_ID, ServiceParam.CURRENT_USER,
						ServiceParam.CURRENT_USER_ID };
				Object input = context.getInput();
				Object rootInput = context.getRootInput();
				User user = Brui.sessionManager.getSessionUserInfo();
				Object inputid = Optional.ofNullable(input).map(i -> Util.getBson(i).get("_id")).orElse(null);
				Object rootInputId = Optional.ofNullable(rootInput).map(i -> Util.getBson(i).get("_id")).orElse(null);
				Object[] parameterValues = new Object[] { input, inputid, rootInput, rootInputId, user,
						user.getUserId() };

				add = AUtil.readBehavior(element, config.getName(), action.getName(), parameterValues, paramemterNames);
			}
			if (add)
				html += UserSession.bruiToolkit().getActionHtml(action, "a");
			else
				html += UserSession.bruiToolkit().getActionPlaceholderHtml(action);
		}
		return html;

	}

}
