package com.bizvisionsoft.bruiengine.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.Behavior;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
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
	private int xUnit = 3;
	private int yUnit = 3;
	private int unitSize = 120;
	private int currentPage = 0;
	private Composite parent;
	private Composite page;
	private Event event;
	private Object input;
	private Assembly assembly;
	private IBruiService service;

	public ActionMenu(IBruiService service) {
		super(UserSession.current().getShell());
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
				//TODO ע�����
				Object value = AUtil.read(input.getClass(), Behavior.class, input, assembly.getName(), action.getName(),
						false, a -> a.value());
				if (Boolean.TRUE.equals(value)) {
					result.add(action);
				}
			} else {
				result.add(action);
			}
		}
		return result;
	}

	private void arrangeActions() {
		ArrayList<Action> _actions = new ArrayList<Action>();
		int units = xUnit * yUnit;
		int count = actions.size();
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
		GridLayout layout = new GridLayout(xUnit, true);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		page.setLayout(layout);
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
					try {
						BruiActionEngine.create(a, service).invokeExecute(event, context);
					} catch (Exception e2) {
						e2.printStackTrace();
						MessageDialog.openError(service.getCurrentShell(), "ϵͳ����", e2.getMessage());
					}
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

}