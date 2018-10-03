package com.bizvisionsoft.bruiengine.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.ListMenu;
import com.bizvisionsoft.service.tools.Check;

public class ExpandNCollapseGridTree {

	@Inject
	private IBruiService bruiService;

	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context,
			@MethodParam(Execute.PARAM_EVENT) Event event) {
		Check.instanceThen(context.getContent(), GridPart.class, p -> showMenu(p, event));
	}

	private void showMenu(GridPart part, Event event) {
		ListMenu listMenu = new ListMenu(bruiService);
		List<Action> actions = new ArrayList<>();
		Action 
		
		action = new Action();
		action.setName("expand1");
		action.setText("展开1层");
		actions.add(action);
		listMenu.handleActionExecute("expand1", a -> {
			part.collapse();
			part.expand(1);
			return false;
		});

		action = new Action();
		action.setName("expand2");
		action.setText("展开2层");
		actions.add(action);
		listMenu.handleActionExecute("expand2", a -> {
			part.collapse();
			part.expand(2);
			return false;
		});

		action = new Action();
		action.setName("expand3");
		action.setText("展开3层");
		actions.add(action);
		listMenu.handleActionExecute("expand3", a -> {
			part.collapse();
			part.expand(3);
			return false;
		});

		action = new Action();
		action.setName("expand");
		action.setText("展开全部");
		actions.add(action);
		listMenu.handleActionExecute("expand", a -> {
			part.expand(-1);
			return false;
		});
		
		
		listMenu.setActions(actions).setLocation(p -> {
			Control item = (Control) event.widget;
			return item.toDisplay(0, item.getBounds().height + 1);
		}).setSize(p -> new Point(120, p.y)).open();

	}

}
