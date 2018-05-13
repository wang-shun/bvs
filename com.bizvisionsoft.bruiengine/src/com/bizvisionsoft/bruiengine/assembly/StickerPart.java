package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.ServiceParam;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContainer;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.util.Util;
import com.bizvisionsoft.service.model.User;

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
		bar = new StickerTitlebar(parent, closeAction, rightActions);
		bar.setText(text).setActions(assembly.getActions());
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
				if (action.isObjectBehavier() && !isAcceptableBehavior(context.getInput(), action)) {
					return;
				}
				try {
					BruiActionEngine.create(action, service).invokeExecute(e, context);
				} catch (Exception e2) {
					e2.printStackTrace();
					MessageDialog.openError(service.getCurrentShell(), "ÏµÍ³´íÎó", e2.getMessage());
				}
			}
		});
	}

	private boolean isAcceptableBehavior(Object element, Action action) {
		String[] paramemterNames = new String[] { ServiceParam.CONTEXT_INPUT_OBJECT,
				ServiceParam.CONTEXT_INPUT_OBJECT_ID, ServiceParam.ROOT_CONTEXT_INPUT_OBJECT,
				ServiceParam.ROOT_CONTEXT_INPUT_OBJECT_ID, ServiceParam.CURRENT_USER, ServiceParam.CURRENT_USER_ID };
		Object input = context.getInput();
		Object rootInput = context.getRootInput();
		User user = Brui.sessionManager.getSessionUserInfo();
		Object inputid = Optional.ofNullable(input).map(i -> Util.getBson(i).get("_id")).orElse(null);
		Object rootInputId = Optional.ofNullable(rootInput).map(i -> Util.getBson(i).get("_id")).orElse(null);
		Object[] parameterValues = new Object[] { input, inputid, rootInput, rootInputId, user, user.getUserId() };

		return AUtil.readBehavior(element, assembly.getName(), action.getName(), parameterValues, paramemterNames);
	}

}
