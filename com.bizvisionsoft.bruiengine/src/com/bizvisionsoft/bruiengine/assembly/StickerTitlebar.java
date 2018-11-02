package com.bizvisionsoft.bruiengine.assembly;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiColors;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;

public class StickerTitlebar extends Composite {

	private Label label;
	private Composite toolbar;
	private BruiToolkit toolkit;
	private boolean compact;

	public StickerTitlebar(Composite parent, Action leftAction, List<Action> rightActions) {
		this(parent, leftAction, rightActions, false);
	}

	public StickerTitlebar(Composite parent, Action leftAction, List<Action> rightActions, boolean compact) {
		super(parent, SWT.NONE);
		this.compact = compact;
		toolkit = UserSession.bruiToolkit();
		GridLayout layout = new GridLayout(leftAction == null ? 2 : 3, false);
		setLayout(layout);
		setBackground(BruiColors.getColor(BruiColor.white));
		if (compact) {
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 0;
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			layout.marginLeft = 8;
		} else {
			setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_BAR_TITLE);
			layout.horizontalSpacing = 8;
			layout.verticalSpacing = 16;
			layout.marginWidth = 8;
			layout.marginHeight = 4;
		}

		if (leftAction != null) {
			createLeftButton(leftAction);
		}

		label = new Label(this, SWT.NONE);
		if (compact) {
			label.setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_TEXT_MENU);
		} else {
			label.setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_TEXT_HEADLINE);
		}
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		toolbar = new Composite(this, SWT.NONE);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginHeight = compact?0:2;
		rl.spacing = compact?0:8;
		rl.marginWidth = 0;
		rl.wrap = false;
		rl.fill = true;
		rl.marginBottom = 0;
		rl.marginTop = 0;
		rl.marginLeft = 0;
		rl.marginRight = 0;
		toolbar.setLayout(rl);

		if (rightActions != null) {
			setActions(rightActions);
		}
	}

	private void createLeftButton(Action leftAction) {
		Label button = new Label(this, SWT.NONE);
		toolkit.enableMarkup(button);
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		if (compact) {
			String text = "<img alter='" + leftAction.getName() + "' src='" + BruiToolkit.getResourceURL(leftAction.getImage())
					+ "' style='cursor:pointer;' width='16px' height='16px'></img>";
			button.setText(text);
			gd.widthHint = 16;
			gd.heightHint = 16;
		} else {
			String text = "<img alter='" + leftAction.getName() + "' src='" + BruiToolkit.getResourceURL(leftAction.getImage())
					+ "' style='cursor:pointer;' width='24px' height='24px'></img>";
			button.setText(text);
			gd.widthHint = 24;
			gd.heightHint = 24;
		}
		button.setLayoutData(gd);
		button.addListener(SWT.MouseDown, e -> handleAction(e, leftAction));

	}

	public StickerTitlebar setText(String text) {
		label.setText(Optional.ofNullable(text).orElse(""));
		return this;
	}

	public StickerTitlebar setActions(List<Action> actions) {
		Optional.ofNullable(actions).ifPresent(as -> as.forEach(this::createActionUI));
		return this;
	}

	private void createActionUI(Action a) {
		Button btn;
		if (compact) {
			btn = toolkit.createButton(toolbar, a, "compact");
			btn.setLayoutData(new RowData(32, 32));
		} else {
			btn = toolkit.createButton(toolbar, a, "line");
		}
		Listener listener = e -> handleAction(e, a);
		toolkit.bindingShortcutKey(btn, a, listener);
		btn.addListener(SWT.Selection, listener);
	}

	private void handleAction(Event e, Action action) {
		e.data = action;
		Arrays.asList(StickerTitlebar.this.getListeners(SWT.Selection)).forEach(aa -> aa.handleEvent(e));
	}

}
