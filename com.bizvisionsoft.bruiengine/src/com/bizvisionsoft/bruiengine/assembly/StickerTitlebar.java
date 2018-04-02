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
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;

public class StickerTitlebar extends Composite {

	private Label label;
	private Composite toolbar;
	private BruiToolkit toolkit;

	public StickerTitlebar(Composite parent) {
		super(parent, SWT.NONE);
		toolkit = UserSession.bruiToolkit();
		setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		setData(RWT.CUSTOM_VARIANT, BruiToolkit.CSS_BAR_TITLE);
		GridLayout layout = new GridLayout(2, false);
		setLayout(layout);
		layout.horizontalSpacing = 16;
		layout.verticalSpacing = 16;
		layout.marginWidth = 16;
		layout.marginHeight = 4;

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
