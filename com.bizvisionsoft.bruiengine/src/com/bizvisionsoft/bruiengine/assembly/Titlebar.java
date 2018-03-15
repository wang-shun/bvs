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
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;

public class Titlebar extends Composite {

	private Label label;
	private Composite toolbar;
	private BruiToolkit toolkit;
	private IBruiService bruiService;

	public Titlebar(Composite parent) {
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

	public Titlebar setText(String text) {
		label.setText(text);
		return this;
	}

	public Titlebar setActions(List<Action> actions) {
		Optional.ofNullable(actions).ifPresent(as -> as.forEach(a -> {
			Button btn = new Button(toolbar, SWT.PUSH);
			toolkit.enableMarkup(btn);
			String text = "";
			String imageUrl = a.getImage();
			if (imageUrl != null) {
				text += "<img alter='" + a.getName() + "' src='" + bruiService.getResourceURL(a.getImage())
						+ "' style='cursor:pointer;' width='20px' height='20px'></img>";
			}
			if (a.isForceText()) {
				if (text.isEmpty()) {
					text += "<div style='display:inline-block;'>" + a.getText() + "</div>";
				} else {
					text += "<div style='margin-left:4px;display:inline-block;'>" + a.getText() + "</div>";
				}
			}
			btn.setText(text);
			btn.setToolTipText(a.getTooltips());
			String style = a.getStyle();
			if (style != null && !style.isEmpty()) {
				btn.setData(RWT.CUSTOM_VARIANT, style);
			}
			btn.addListener(SWT.Selection, e -> {
				e.data = a;
				Arrays.asList(Titlebar.this.getListeners(SWT.Selection)).forEach(aa -> aa.handleEvent(e));
			});
		}));
		return this;
	}

	public Titlebar setServices(IBruiService bruiService) {
		this.bruiService = bruiService;
		return this;
	}

}
