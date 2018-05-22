package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContainer;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;

public class StickerPart {

	@Inject
	IBruiService service;

	private Assembly assembly;

	@GetContainer
	Composite content;

	@Inject
	IBruiContext context;

	private StickerTitlebar bar;

	private List<Action> rightActions;

	private List<Consumer<IBruiContext>> rightConsumers;

	public StickerPart(Assembly assembly) {
		this.assembly = assembly;
	}

	public StickerPart addAction(Action action, Consumer<IBruiContext> e) {
		if (rightActions == null) {
			rightActions = new ArrayList<Action>();
			rightConsumers = new ArrayList<>();
		}
		rightActions.add(action);
		rightConsumers.add(e);
		return this;
	}

	@CreateUI
	public void createUI(Composite parent) {
		parent.setLayout(new FormLayout());

		String cssClass = "";
		if (assembly.isBorderTop()) {
			cssClass += " brui_borderTop";
		}
		if (assembly.isBorderRight()) {
			cssClass += " brui_borderRight";
		}
		if (assembly.isBorderBottom()) {
			cssClass += " brui_borderBottom";
		}
		if (assembly.isBorderLeft()) {
			cssClass += " brui_borderLeft";
		}

		parent.setHtmlAttribute("class", cssClass);

		String text = assembly.getStickerTitle();
		if (assembly.isDisplayInputLabelInTitlebar()) {
			text += Optional.ofNullable(context.getInput()).map(o -> AUtil.readLabel(o, "")).map(l -> " - " + l)
					.orElse("");
		} else if (assembly.isDisplayRootInputLabelInTitlebar()) {
			text += Optional.ofNullable(context.getRootInput()).map(o -> AUtil.readLabel(o, "")).map(l -> " - " + l)
					.orElse("");
		}

		Action closeAction = null;
		if (context.isCloseable()) {
			closeAction = new Action();
			closeAction.setName("close");
			closeAction.setImage("/img/close.svg");
		}
		bar = new StickerTitlebar(parent, closeAction, rightActions).setText(text);
		setToolbarActions();
		FormData fd = new FormData();
		bar.setLayoutData(fd);
		fd.left = new FormAttachment(0);
		fd.top = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.height = 48;

		content = UserSession.bruiToolkit().newContentPanel(parent);
		fd = new FormData();
		content.setLayoutData(fd);
		fd.left = new FormAttachment(0, 12);
		fd.top = new FormAttachment(bar, 12);
		fd.right = new FormAttachment(100, -12);
		fd.bottom = new FormAttachment(100, -12);

		bar.addListener(SWT.Selection, e -> {
			Action action = ((Action) e.data);
			if ("close".equals(action.getName())) {
				service.closeCurrentContent();
			} else if (rightActions != null && rightActions.indexOf(action) != -1) {
				int idx = rightActions.indexOf(action);
				rightConsumers.get(idx).accept(context);
			} else {
				UserSession.bruiToolkit().runAction(action, service, context);
			}
		});
	}

	private void setToolbarActions() {
		final List<Action> actions = new ArrayList<Action>();
		List<Action> list = assembly.getActions();
		if (list != null)
			list.forEach(action -> {
				if (UserSession.bruiToolkit().isAcceptableBehavior(context.getInput(), context, assembly, action)) {
					actions.add(action);
				}
			});
		bar.setActions(actions);
	}

}
