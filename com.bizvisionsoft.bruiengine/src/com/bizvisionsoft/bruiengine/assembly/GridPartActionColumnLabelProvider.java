package com.bizvisionsoft.bruiengine.assembly;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.session.UserSession;

public class GridPartActionColumnLabelProvider extends ColumnLabelProvider {

	private List<Action> actions;

	public GridPartActionColumnLabelProvider(List<Action> actions) {
		this.actions = actions;
	}

	@Override
	public String getText(Object element) {

		String html = "";
		for (Action action : actions) {
			html += UserSession.bruiToolkit().getActionHtml(action, "a");
		}
		return html;

	}

}
