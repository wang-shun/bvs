package com.bizvisionsoft.bruiengine.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.bruiengine.util.Util;

public class ActionMenu extends Part {

	private class PreviousPageAction extends Action {

		public PreviousPageAction() {
			super();
			setName("<");
			setStyle(BruiToolkit.CSS_INFO);
			setImage("/img/left_w.svg");
		}

	}

	private class NextPageAction extends Action {
		public NextPageAction() {
			super();
			setName(">");
			setStyle(BruiToolkit.CSS_INFO);
			setImage("/img/right_w.svg");
		}

	}

	private List<Action> actions;
	private List<List<Action>> pagedAction;
	private IBruiContext context;
	private int colCount = 3;
	private int rowCount = 3;
	private int unitSize = 120;
	private int currentPage = 0;
	private Composite parent;
	private Composite page;
	private Event event;
	private Object input;
	private Assembly assembly;
	private IBruiService service;

	private Map<String, Function<Action, Boolean>> listener = new HashMap<String, Function<Action, Boolean>>();

	public ActionMenu(IBruiService service) {
		super(service.getCurrentShell());
		this.service = service;
		setShellStyle(SWT.ON_TOP);
	}

	public ActionMenu setInput(Object input) {
		this.input = input;
		return this;
	}

	public ActionMenu setAssembly(Assembly assembly) {
		this.assembly = assembly;
		return this;
	}

	public ActionMenu setActions(List<Action> actions) {
		this.actions = getActions(actions);
		arrangeActions();
		return this;
	}

	private List<Action> getActions(List<Action> actions) {
		ArrayList<Action> result = new ArrayList<Action>();
		for (int i = 0; i < actions.size(); i++) {
			Action action = actions.get(i);
			if (action.isObjectBehavier() && input != null && assembly != null) {

				String[] paramemterNames = { MethodParam.CONTEXT_INPUT_OBJECT, MethodParam.CONTEXT_INPUT_OBJECT_ID,
						MethodParam.ROOT_CONTEXT_INPUT_OBJECT, MethodParam.ROOT_CONTEXT_INPUT_OBJECT_ID,
						MethodParam.CURRENT_USER, MethodParam.CURRENT_USER_ID };
				Object[] parameterValues = context.getContextParameters(paramemterNames);

				if (AUtil.readBehavior(input, assembly.getName(), action.getName(), parameterValues, paramemterNames)) {
					result.add(action);
				}
			} else {
				result.add(action);
			}
		}
		return result;
	}

	private void arrangeActions() {
		int count = actions.size();
		if (count <= colCount) {
			colCount = count;
		} else if (count < colCount * rowCount) {
			// 总数小于行列数的时候，尝试矩形布局
			colCount = (int) Math.ceil(Math.sqrt(count));
		}

		int units = colCount * rowCount;

		ArrayList<Action> _actions = new ArrayList<Action>();
		for (int i = 1; i <= count; i++) {
			_actions.add(actions.get(i - 1));
			if (i == units - 1 && i != count || i > units - 1 && (i - units + 1) % (units - 2) == 0 && i != count) {
				_actions.add(new NextPageAction());
				_actions.add(new PreviousPageAction());
			}
		}

		pagedAction = Util.splitArray(_actions, units);
	}

	@Override
	protected void createContents(Composite parent) {
		this.parent = parent;
		parent.setBackground(BruiColors.getColor(BruiColor.Grey_50));

		parent.setLayout(new FillLayout());
		createPage();
	}

	private void createPage() {
		page = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(colCount, true);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		page.setLayout(layout);

		if (pagedAction.size() <= currentPage) {
			Layer.message("没有可执行的操作。");
			return;
		}
		pagedAction.get(currentPage).forEach(a -> {
			Button button = UserSession.bruiToolkit().createButton(page, a, "block");
			button.setLayoutData(new GridData(unitSize, unitSize));
			if (a instanceof NextPageAction) {
				button.addListener(SWT.Selection, e -> nextPage());
			} else if (a instanceof PreviousPageAction) {
				button.addListener(SWT.Selection, e -> perviuosPage());
			} else {
				button.addListener(SWT.Selection, e -> {
					close();
					Function<Action, Boolean> lis = listener.get(a.getName());
					if (lis != null && !Boolean.TRUE.equals(lis.apply(a))) {
						return;
					}
					BruiActionEngine.execute(a, event, context, service);
				});
			}
		});

	}

	private void perviuosPage() {
		currentPage--;
		page.dispose();
		createPage();
		parent.layout();
	}

	private void nextPage() {
		currentPage++;
		page.dispose();
		createPage();
		parent.layout();
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setData(RWT.CUSTOM_VARIANT, "menu");
		newShell.addListener(SWT.Deactivate, e -> close());
		super.configureShell(newShell);
	}

	public ActionMenu setContext(IBruiContext context) {
		this.context = context;
		return this;
	}

	public ActionMenu setEvent(Event event) {
		this.event = event;
		return this;
	}

	public ActionMenu handleActionExecute(String actionName, Function<Action, Boolean> func) {
		listener.put(actionName, func);
		return this;
	}

}