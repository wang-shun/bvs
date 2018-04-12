package com.bizvisionsoft.bruiengine.ui;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.BruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;

public class Selector extends Popup {


	public static  Selector open(String name, IBruiContext parentContext, Object input,
			Consumer<List<Object>> doit) {
		return create(name, parentContext, input).open(doit);
	}

	public static  Selector create(String name, IBruiContext parentContext,Object input) {
		Assembly config = Brui.site.getAssemblyByName(name);
		return new Selector(config, parentContext).setInput(input);
	}

	public Selector(Assembly assembly, IBruiContext parentContext) {
		super(assembly, parentContext);
		setShellStyle(SWT.TITLE | SWT.RESIZE | SWT.ON_TOP | SWT.APPLICATION_MODAL);
		String title = assembly.getTitle();
		setTitle(title == null ? assembly.getName() : title);
	}

	@Override
	public Selector setTitle(String title) {
		super.setTitle(title);
		return this;
	}

	@Override
	protected BruiEditorContext createContext(IBruiContext parentContext) {
		return new BruiEditorContext();
	}

	public Selector setEditable(boolean editable) {
		getContext().setEditable(editable);
		return this;
	}

	public Selector setInput(Object input) {
		getContext().setInput(input);
		return this;
	}

	@Override
	public BruiEditorContext getContext() {
		return (BruiEditorContext) super.getContext();
	}

	@Override
	protected Point getInitialSize() {
		// int minWidth = 300;
		// int minHeight = 200;
		//
		// Rectangle disb = Display.getCurrent().getBounds();
		// if(disb.x<minWidth || disb.y<minHeight) {
		// getShell().setMaximized(true);
		// }
		//
		// Point size = super.getInitialSize();
		// //避免过小
		// size.x = size.x<minWidth?minWidth:size.x;
		// size.y = size.y<minWidth?minHeight:size.y;
		// //避免过大
		// int maxWidth = 2*disb.width/3;
		// int maxHeight = 4*disb.height/5;
		// size.x = size.x>maxWidth?maxWidth:size.x;
		// size.y = size.y>maxHeight?maxHeight:size.y;

		int minWidth = 960;
		int minHeight = 600;

		Rectangle disb = Display.getCurrent().getBounds();
		if (disb.x < minWidth || disb.y < minHeight) {
			getShell().setMaximized(true);
		}

		int perfWidth = 2 * disb.width / 3;
		int perfHeight = disb.height - 120;

		return new Point(Math.max(minWidth, perfWidth), Math.max(minHeight, perfHeight));
	}

	public Object getResult() {
		return brui.getReturnObject();
	}

	@SuppressWarnings("unchecked")
	public Selector open(Consumer<List<Object>> doit) {
		if (Window.OK == open()) {
			doit.accept((List<Object>) getResult());
		}
		return this;
	}

}
