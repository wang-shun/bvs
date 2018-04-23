package com.bizvisionsoft.bruiengine.ui;

import java.util.function.BiConsumer;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.BruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.mongodb.BasicDBObject;

public class Editor<T> extends Popup {

	private T input;

	public static <M> Editor<M> open(String name, IBruiContext parentContext, M input, boolean modifyInput,
			BiConsumer<BasicDBObject, M> doit) {
		return create(name, parentContext, input, modifyInput).ok(doit);
	}

	public static <M> Editor<M> open(String name, IBruiContext parentContext, M input,
			BiConsumer<BasicDBObject, M> doit) {
		return open(name, parentContext, input, false, doit);
	}

	public static <M> Editor<M> create(String name, IBruiContext parentContext, M input, boolean modifyInput) {
		Assembly editorConfig = ModelLoader.site.getAssemblyByName(name);
		return new Editor<M>(editorConfig, parentContext).setInput(modifyInput, input);
	}

	public Editor(Assembly assembly, IBruiContext parentContext) {
		super(assembly, parentContext);
		setShellStyle(SWT.TITLE | SWT.RESIZE | SWT.ON_TOP | SWT.APPLICATION_MODAL);
		String title = assembly.getTitle();
		setTitle(title == null ? assembly.getName() : title);
	}

	@Override
	public Editor<T> setTitle(String title) {
		super.setTitle(title);
		return this;
	}

	@Override
	protected BruiEditorContext createContext(IBruiContext parentContext) {
		return new BruiEditorContext();
	}

	public Editor<T> setEditable(boolean editable) {
		getContext().setEditable(editable);
		return this;
	}

//	public Editor<T> setIgnoreNull(boolean ignoreNull) {
//		getContext().setIgnoreNull(ignoreNull);
//		return this;
//	}

	public Editor<T> setInput(boolean modifyInput, T input) {
		if (!modifyInput) {
			this.input = AUtil.deepCopy(input);
		} else {
			this.input = input;
		}
		getContext().setInput(this.input);
		return this;
	}

	public Editor<T> setInput(T input) {
		return setInput(false, input);
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

		
		int w = Math.max(minWidth, perfWidth);
		int h = Math.max(minHeight, perfHeight);
		
		if(getAssembly().isSmallEditor()) {
			return new Point(2*w/3, 2*h/3);
		}else {
			return new Point(w, h);
		}
	}

	public Object getResult() {
		return brui.getReturnObject();
	}

	public Editor<T> ok(BiConsumer<BasicDBObject, T> doit) {
		if (Window.OK == open()) {
			doit.accept((BasicDBObject) getResult(), input);
		}
		return this;
	}

}
