package com.bizvisionsoft.bruiengine.assembly;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;

public class StickerTitlebar extends Composite {

	private Label label;
	private Composite toolbar;
	private BruiToolkit toolkit;

	public StickerTitlebar(Composite parent, Action leftAction, List<Action> rightActions) {
		super(parent, SWT.NONE);
		toolkit = UserSession.bruiToolkit();
		setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_BAR_TITLE);
		GridLayout layout = new GridLayout(leftAction == null ? 2 : 3, false);
		setLayout(layout);
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 16;
		layout.marginWidth = 16;
		layout.marginHeight = 4;

		if (leftAction != null) {
			createLeftButton(leftAction);
		}

		label = new Label(this, SWT.NONE);
		label.setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_TEXT_HEADLINE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		toolbar = new Composite(this, SWT.NONE);
		toolbar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		FillLayout rl = new FillLayout(SWT.HORIZONTAL);
		rl.marginHeight = 4;
		rl.spacing = 8;
		rl.marginWidth = 0;

		toolbar.setLayout(rl);
		
		if(rightActions!=null) {
			setActions(rightActions);
		}
	}

	private void createLeftButton(Action leftAction) {
		Label button = new Label(this, SWT.NONE);
		toolkit.enableMarkup(button);
		String text = "<img alter='" + leftAction.getName() + "' src='"
				+ BruiToolkit.getResourceURL(leftAction.getImage())
				+ "' style='cursor:pointer;' width='24px' height='24px'></img>";
		button.setText(text);

		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 24;
		gd.heightHint = 24;
		button.setLayoutData(gd);
		button.addListener(SWT.MouseDown, e -> {
			e.data = leftAction;
			Arrays.asList(StickerTitlebar.this.getListeners(SWT.Selection)).forEach(aa -> aa.handleEvent(e));
		});
	}

	public StickerTitlebar setText(String text) {
		label.setText(Optional.ofNullable(text).orElse(""));
		return this;
	}

	public StickerTitlebar setActions(List<Action> actions) {
		Optional.ofNullable(actions).ifPresent(as -> as.forEach(a -> {
			Button btn = toolkit.createButton(toolbar, a, "line");
			btn.addListener(SWT.Selection, e -> {
				e.data = a;
				Arrays.asList(StickerTitlebar.this.getListeners(SWT.Selection)).forEach(aa -> aa.handleEvent(e));
			});
		}));
		return this;
	}

}
