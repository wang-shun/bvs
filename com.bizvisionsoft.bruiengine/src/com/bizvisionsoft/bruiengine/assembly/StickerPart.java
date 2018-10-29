package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContainer;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.Controls;
import com.bizvisionsoft.service.tools.Check;

public class StickerPart {

	public Logger logger = LoggerFactory.getLogger(getClass());

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
			text += Optional.ofNullable(context.getInput()).map(o -> AUtil.readLabel(o, "")).map(l -> " - " + l).orElse("");
		} else if (assembly.isDisplayRootInputLabelInTitlebar()) {
			text += Optional.ofNullable(context.getRootInput()).map(o -> AUtil.readLabel(o, "")).map(l -> " - " + l).orElse("");
		}

		Action closeAction = null;
		if (context.isCloseable()) {
			closeAction = new Action();
			closeAction.setName("close");
			closeAction.setImage("/img/close.svg");
		}


		bar = Controls.handle(new StickerTitlebar(parent, closeAction, rightActions,assembly.isCompactTitleBar())).height(assembly.isCompactTitleBar()?32:48).left().top().right().get()
				.setText(text);

		setToolbarActions();

		if (assembly.isCompactTitleBar()) {
			content = Controls.contentPanel(parent).loc().top(bar,-1).get();
		}else {
			content = Controls.contentPanel(parent).mLoc().mTop(bar).get();
		}

		bar.addListener(SWT.Selection, e -> {
			Action action = ((Action) e.data);
			if ("close".equals(action.getName())) {
				service.closeCurrentContent();
			} else if (rightActions != null && rightActions.indexOf(action) != -1) {
				int idx = rightActions.indexOf(action);
				rightConsumers.get(idx).accept(context);
			} else {
				UserSession.bruiToolkit().runAction(action, e, service, context);
			}
		});
	}

	private void setToolbarActions() {
		List<Action> actions = UserSession.bruiToolkit().getAcceptedActions(assembly, service.getCurrentUserInfo(), context);
		bar.setActions(actions);
	}

	public void createDefaultActions(Object part) {
		Action action;
		//////////////////////////////////////////////////////////////////////////////////
		// 创建默认的action
		// if (logger.isDebugEnabled()) {
		// action = new Action();
		// action.setType(Action.TYPE_CUSTOMIZED);
		// action.setImage("/img/info_w.svg");
		// action.setStyle("serious");
		// addAction(action, e -> service.displaySiteModel(assembly));
		// }

		// 设置
		if (part instanceof IClientCustomizable && !Boolean.TRUE.equals(assembly.isDisableCustomized())) {
			action = new Action();
			action.setType(Action.TYPE_CUSTOMIZED);
			action.setImage("/img/setting_w.svg");
			action.setStyle("info");
			addAction(action, e -> ((IClientCustomizable) part).customize());
		}

		// 导出
		if (part instanceof IExportable && !Boolean.TRUE.equals(assembly.isDisableStandardExport())) {
			action = new Action();
			action.setType(Action.TYPE_CUSTOMIZED);
			action.setImage("/img/excel_w.svg");
			action.setStyle("info");
			addAction(action, e -> ((IExportable) part).export());
		}

		// 查询
		if (part instanceof IQueryEnable && !Boolean.TRUE.equals(assembly.isDisableStdQuery()) && Check.isAssigned(assembly.getFields())) {
			action = new Action();
			action.setType(Action.TYPE_CUSTOMIZED);
			action.setImage("/img/search_w.svg");
			action.setStyle("info");
			addAction(action, e -> ((IQueryEnable) part).openQueryEditor());
		}

	}

}
