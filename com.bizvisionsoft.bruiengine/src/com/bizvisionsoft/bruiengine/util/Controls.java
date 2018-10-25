package com.bizvisionsoft.bruiengine.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

import com.bizivisionsoft.widgets.tools.WidgetHandler;
import com.bizvisionsoft.bruiengine.util.BruiColors.BruiColor;
import com.bizvisionsoft.service.tools.Check;

public class Controls<T extends Control> {

	private T control;
	private FormData fd;
	private int margin = BruiToolkit.MARGIN;

	public static <K extends Control> Controls<K> create(Class<K> type, Composite parent, int style, String id,
			String cssCalss) {
		return new Controls<K>(type, parent, style, id, cssCalss);
	}

	public static <K extends Control> Controls<K> create(Class<K> type, Composite parent, int style) {
		return new Controls<K>(type, parent, style, null, null);
	}

	public static Controls<Composite> contentPanel(Composite parent) {
		return new Controls<Composite>(Composite.class, parent, SWT.BORDER, null, null)
				.background(BruiColors.getColor(BruiColor.white));
	}

	public static <K extends Control> Controls<K> handle(K ctl) {
		return new Controls<K>(ctl);
	}

	private Controls(Class<T> type, Composite parent, int style, String id, String cssClass) {
		try {
			Constructor<T> c = type.getConstructor(Composite.class, int.class);
			control = c.newInstance(parent, style);
			rwtStyle(id);
			cssStyle(cssClass);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
		}
	}

	public Controls<T> cssStyle(String cssClass) {
		if (cssClass != null) {
			WidgetHandler.getHandler(control).setClass(cssClass);
		}
		return this;
	}

	public Controls<T> margin(int m) {
		this.margin = m;
		return this;
	}

	public Controls<T> rwtStyle(String id) {
		if (id != null) {
			control.setData(RWT.CUSTOM_VARIANT, id);
		}
		return this;
	}

	private Controls(T control) {
		this.control = control;
	}

	public Controls<T> background(Color color) {
		control.setBackground(color);
		return this;
	}

	public Controls<T> background(BruiColor color) {
		control.setBackground(BruiColors.getColor(color));
		return this;
	}

	public Controls<T> layout(Layout layout) {
		if (control instanceof Composite) {
			((Composite) control).setLayout(layout);
		}
		return this;
	}

	public Controls<T> width(int width) {
		createLayoutData();
		fd.width = width;
		return this;
	}

	public Controls<T> height(int height) {
		createLayoutData();
		fd.height = height;
		return this;
	}
	
	public Controls<T> size(int width, int height) {
		createLayoutData();
		fd.width = width;
		fd.height = height;
		return this;
	}

	public Controls<T> left(Control ctl, int off) {
		createLayoutData();
		fd.left = new FormAttachment(ctl, off);
		return this;
	}

	public Controls<T> left(int numerator, int off) {
		createLayoutData();
		fd.left = new FormAttachment(numerator, off);
		return this;
	}

	public Controls<T> left(int numerator) {
		createLayoutData();
		fd.left = new FormAttachment(numerator);
		return this;
	}

	public Controls<T> left(Control ctl) {
		createLayoutData();
		fd.left = new FormAttachment(ctl);
		return this;
	}

	public Controls<T> left() {
		createLayoutData();
		fd.left = new FormAttachment();
		return this;
	}

	public Controls<T> mLeft(int numerator) {
		createLayoutData();
		fd.left = new FormAttachment(numerator, margin);
		return this;
	}

	public Controls<T> mLeft(Control ctl) {
		createLayoutData();
		fd.left = new FormAttachment(ctl, margin);
		return this;
	}

	public Controls<T> mLeft() {
		createLayoutData();
		fd.left = new FormAttachment(0, margin);
		return this;
	}

	public Controls<T> right(Control ctl, int off) {
		createLayoutData();
		fd.right = new FormAttachment(ctl, off);
		return this;
	}

	public Controls<T> right(int numerator, int off) {
		createLayoutData();
		fd.right = new FormAttachment(numerator, off);
		return this;
	}

	public Controls<T> right(Control ctl) {
		createLayoutData();
		fd.right = new FormAttachment(ctl);
		return this;
	}

	public Controls<T> right() {
		createLayoutData();
		fd.right = new FormAttachment(100);
		return this;
	}

	public Controls<T> mRight(Control ctl) {
		createLayoutData();
		fd.right = new FormAttachment(ctl, -margin);
		return this;
	}

	public Controls<T> right(int numerator) {
		createLayoutData();
		fd.right = new FormAttachment(numerator, -margin);
		return this;
	}

	public Controls<T> mRight() {
		createLayoutData();
		fd.right = new FormAttachment(100, -margin);
		return this;
	}

	public Controls<T> top(Control ctl, int off) {
		createLayoutData();
		fd.top = new FormAttachment(ctl, off);
		return this;
	}

	public Controls<T> top(int numerator, int off) {
		createLayoutData();
		fd.top = new FormAttachment(numerator, off);
		return this;
	}

	public Controls<T> top(Control ctl) {
		createLayoutData();
		fd.top = new FormAttachment(ctl);
		return this;
	}

	public Controls<T> top() {
		createLayoutData();
		fd.top = new FormAttachment();
		return this;
	}

	public Controls<T> mTop(Control ctl) {
		createLayoutData();
		fd.top = new FormAttachment(ctl, margin);
		return this;
	}

	public Controls<T> mTop(int numerator) {
		createLayoutData();
		fd.top = new FormAttachment(numerator, margin);
		return this;
	}

	public Controls<T> mTop() {
		createLayoutData();
		fd.top = new FormAttachment(0, margin);
		return this;
	}

	public Controls<T> bottom(Control ctl, int off) {
		createLayoutData();
		fd.bottom = new FormAttachment(ctl, off);
		return this;
	}

	public Controls<T> bottom(int numerator, int off) {
		createLayoutData();
		fd.bottom = new FormAttachment(numerator, off);
		return this;
	}

	public Controls<T> bottom(Control ctl) {
		createLayoutData();
		fd.bottom = new FormAttachment(ctl);
		return this;
	}

	public Controls<T> bottom() {
		createLayoutData();
		fd.bottom = new FormAttachment(100);
		return this;
	}

	public Controls<T> mBottom(Control ctl) {
		createLayoutData();
		fd.bottom = new FormAttachment(ctl, -margin);
		return this;
	}

	public Controls<T> mBottom(int numerator) {
		createLayoutData();
		fd.bottom = new FormAttachment(numerator, -margin);
		return this;
	}

	public Controls<T> mBottom() {
		createLayoutData();
		fd.bottom = new FormAttachment(100, -margin);
		return this;
	}

	private void createLayoutData() {
		if (fd == null) {
			fd = new FormData();
			control.setLayoutData(fd);
		}
	}

	public Controls<T> mLoc() {
		return mLeft().mRight().mBottom().mTop();
	}

	public Controls<T> loc() {
		return left().right().bottom().top();
	}

	public Controls<T> centerH() {
		Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		left(50, -size.x / 2);
		return this;
	}

	public Controls<T> centerV() {
		Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		top(50, -size.y / 2);
		return this;
	}

	public Controls<T> center() {
		Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		left(50, -size.x / 2);
		top(50, -size.y / 2);
		return this;
	}

	public T get() {
		return control;
	}

	public Controls<T> setText(String text) {
		boolean b = false;
		if (!b)
			b = Check.instanceThen(control, Text.class, t -> t.setText(text));
		if (!b)
			b = Check.instanceThen(control, Label.class, t -> t.setText(text));
		if (!b)
			b = Check.instanceThen(control, Button.class, t -> t.setText(text));
		return this;
	}

	public Controls<T> setHTML(String text) {
		control.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		control.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
		boolean b = false;
		if (!b)
			b = Check.instanceThen(control, Label.class, t -> t.setText(text));
		if (!b)
			b = Check.instanceThen(control, Button.class, t -> t.setText(text));
		return this;
	}

	public Controls<T> above(Control ctl) {
		control.moveAbove(ctl);
		return this;
	}

	public Controls<T> below(Control ctl) {
		control.moveBelow(ctl);
		return this;
	}

	public Controls<T> put(Consumer<T> children) {
		children.accept(control);
		return this;
	}

	public Controls<T> setImage(String url) {
		if (control instanceof Label) {
			control.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
			control.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
			control.setStyleAttribute("backgroundImage", url);
			control.setStyleAttribute("background-repeat", "no-repeat");
			control.setStyleAttribute("background-size", "cover");
		}else if(control instanceof Composite) {
			control.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
			control.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);
			control.setStyleAttribute("backgroundImage", url);
			control.setStyleAttribute("background-repeat", "no-repeat");
			control.setStyleAttribute("background-size", "cover");
		}
		return this;
	}



}
