package com.bizivisionsoft.widgets.datetime;

import java.util.Date;
import java.util.function.Function;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class DateTimeSelector extends Shell {

	private static final int HEIGHT = 321;
	private static final int WIDTH = 274;
	private DateTimeSetting setting;
	private Date value;
	private Function<Date, Boolean> callback;

	public DateTimeSelector() {
		this(null);
	}

	public DateTimeSelector(DateTimeSetting setting) {
		super(Display.getCurrent().getActiveShell(), SWT.ON_TOP);
		this.setting = setting == null ? DateTimeSetting.dateTime() : setting;
	}

	public DateTimeSetting getSetting() {
		return setting;
	}

	private void select(Event e) {
		value = (Date) e.data;
		if (callback != null) {
			Boolean result = callback.apply(value);
			if (Boolean.TRUE.equals(result)) {
				close();
			}
		}
	}

	@Override
	public void open() {
		setData(RWT.CUSTOM_VARIANT, "menu");
		setSize(WIDTH, HEIGHT);
		FillLayout layout = new FillLayout();
		setLayout(layout);
		DateTime dt = new DateTime(this, this.setting.setPosition(DateTimeSetting.POSITION_STATIC), true);
		dt.setData(RWT.CUSTOM_VARIANT, "panel");
		dt.addListener(SWT.Modify, this::select);
		addListener(SWT.Deactivate, e -> {
			close();
		});
		super.open();
	}

	public void open(Function<Date, Boolean> callback) {
		this.callback = callback;
		open();
	}

	public DateTimeSelector setCallback(Function<Date, Boolean> callback) {
		this.callback = callback;
		return this;
	}

	public DateTimeSelector bind(Widget widget,int location) {
		if (widget != null && !widget.isDisposed() && widget instanceof Control) {
			Control control = ((Control) widget);
			if(location == SWT.LEFT) {
				setLocation(control.toDisplay(-WIDTH+control.getSize().x, control.getSize().y));
			}else  {
				setLocation(control.toDisplay(0, control.getSize().y));
			}
		}
		return this;
	}

}
