package com.bizvisionsoft.bruiengine.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

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

public class ListMenu extends Part {

	private List<Action> actions;
	private IBruiContext context;
	private Composite parent;
	private Composite page;
	private Event event;
	private Object input;
	private Assembly assembly;
	private IBruiService service;

	private Map<String, Function<Action, Boolean>> listener = new HashMap<String, Function<Action, Boolean>>();
	private Function<Point, Point> locFunc;
	private Function<Point, Point> sizeFunc;

	public ListMenu(IBruiService service) {
		super(service.getCurrentShell());
		this.service = service;
		setShellStyle(SWT.ON_TOP);
	}

	public ListMenu setInput(Object input) {
		this.input = input;
		return this;
	}

	public ListMenu setAssembly(Assembly assembly) {
		this.assembly = assembly;
		return this;
	}

	public ListMenu setActions(List<Action> actions) {
		this.actions = getActions(actions);
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

	@Override
	protected void createContents(Composite parent) {
		this.parent = parent;
		parent.setBackground(BruiColors.getColor(BruiColor.Teal_500));
		parent.setLayout(new FillLayout());
		createPage();
	}

	private void createPage() {
		page = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(1, true);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		page.setLayout(layout);

		actions.forEach(a -> createMenuItem(a));

	}

	private void createMenuItem(Action a) {

		Label button = new Label(page, SWT.NONE);
		button.setCursor(page.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
		UserSession.bruiToolkit().enableMarkup(button);
		button.setText(getButtonText(a));
		GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gridData.heightHint = 32;
		button.setLayoutData(gridData);
		button.addListener(SWT.MouseUp, e -> {
			close();
			Function<Action, Boolean> lis = listener.get(a.getName());
			if (lis != null && !Boolean.TRUE.equals(lis.apply(a))) {
				return;
			}
			BruiActionEngine.execute(a, event, context, service);
		});

	}

	private String getButtonText(Action a) {
		String buttonText = Util.isEmptyOrNull(a.getText()) ? "" : a.getText();
		String text = "<div style='display:block;width:300px'>";
		String margin;
		String image = a.getImage();
		if (!Util.isEmptyOrNull(image)) {
			String url = BruiToolkit.getResourceURL(image);
			text += "<img src='" + url
					+ "' style='float:left; cursor:pointer;margin:4px;' width='24px' height='24px'></img>";
			margin = "6px";
		} else {
			margin = "38px";
		}
		text += "<div style='float:left;color:#fff;font-size:14px;font-weight:lighter;margin-top:6px;margin-left:"
				+ margin + ";'>" + buttonText + "</div></div>";
		return text;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setData(RWT.CUSTOM_VARIANT, "menu");
		newShell.addListener(SWT.Deactivate, e -> close());
		super.configureShell(newShell);
	}

	public ListMenu setContext(IBruiContext context) {
		this.context = context;
		return this;
	}

	public ListMenu setEvent(Event event) {
		this.event = event;
		return this;
	}

	public ListMenu handleActionExecute(String actionName, Function<Action, Boolean> func) {
		listener.put(actionName, func);
		return this;
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {
		if (locFunc != null) {
			return locFunc.apply(initialSize);
		}
		return super.getInitialLocation(initialSize);
	}

	@Override
	protected Point getInitialSize() {
		Point initSize = super.getInitialSize();
		if (sizeFunc != null) {
			return sizeFunc.apply(initSize);
		} else {
			return initSize;
		}
	}

	public ListMenu setLocation(Function<Point, Point> locFunc) {
		this.locFunc = locFunc;
		return this;
	}

	public ListMenu setSize(Function<Point, Point> sizeFunc) {
		this.sizeFunc = sizeFunc;
		return this;
	}

}