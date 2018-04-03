package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.session.UserSession;

public class GridPartActionColumnLabelProvider extends ColumnLabelProvider {

	private List<Action> actions;
	private Assembly config;

	public GridPartActionColumnLabelProvider(Assembly config, List<Action> actions) {
		this.config = config;
		this.actions = actions;
	}

	@Override
	public String getText(Object element) {
		String html = "";
		for (Action action : actions) {
			boolean add = true;
			if (action.isObjectBehavier()) {
				Object value = AUtil.read(element.getClass(), Behavior.class, element, config.getName(),
						action.getName(), false, a -> a.value());
				add = Boolean.TRUE.equals(value);
			}
			if (add)
				html += UserSession.bruiToolkit().getActionHtml(action, "a");
		}
		return html;

	}

}
