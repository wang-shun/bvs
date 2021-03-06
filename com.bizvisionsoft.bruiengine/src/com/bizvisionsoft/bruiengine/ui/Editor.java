package com.bizvisionsoft.bruiengine.ui;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.BruiEditorContext;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.mongodb.BasicDBObject;

public class Editor<T> extends Popup {

	private T input;

	private static Logger logger = LoggerFactory.getLogger(Editor.class);

	public static <M> Editor<M> open(String name, IBruiContext parentContext, M input, boolean modifyInput,
			BiConsumer<BasicDBObject, M> doit) {
		return Optional.ofNullable(create(name, parentContext, input, modifyInput)).map(e -> e.ok(doit)).orElse(null);
	}

	public static <M> Editor<M> open(String name, IBruiContext parentContext, M input,
			BiConsumer<BasicDBObject, M> doit) {
		return open(name, parentContext, input, false, doit);
	}

	public static <M> Editor<M> create(String name, IBruiContext parentContext, M input, boolean modifyInput) {
		Assembly editorConfig = ModelLoader.site.getAssemblyByName(name);
		if (editorConfig == null) {
			String msg = "名称：" + name + ",编辑器不存在";
			Layer.message(msg, Layer.ICON_CANCEL);
			logger.error(msg);
			return null;
		}
		return new Editor<M>(editorConfig, parentContext).setInput(modifyInput, input);
	}

	public Editor(Assembly assembly, IBruiContext parentContext) {
		super(assembly, parentContext, assembly.isAddToParentContext());
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
		BruiEditorContext c = UserSession.newEditorContext();// 编辑器考虑可在各种不同的上下文中使用，默认不应连接到父上下文
		if (assembly.isAddToParentContext()) {
			c.setParent(parentContext);
		}
		return c;
	}

	public Editor<T> setEditable(boolean editable) {
		getContext().setEditable(editable);
		return this;
	}

	// public Editor<T> setIgnoreNull(boolean ignoreNull) {
	// getContext().setIgnoreNull(ignoreNull);
	// return this;
	// }

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

		int minWidth = 1024;
		int minHeight = 768;

		Rectangle disb = Display.getCurrent().getBounds();
		if (disb.width < minWidth || disb.height < minHeight) {
			getShell().setMaximized(true);
			return new Point(disb.width, disb.height);
		}

		int perfWidth = 2 * disb.width / 3;
		int perfHeight = disb.height - 120;

		int w = Math.max(minWidth, perfWidth);
		int h = Math.max(minHeight, perfHeight);

		if (getAssembly().isSmallEditor()) {
			w = 2 * w / 3;
		}

		if (getAssembly().isTinyEditor()) {
			h = 2 * h / 3;
		}
		return new Point(w, h);
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
