package com.bizvisionsoft.bruiengine.ui;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.util.Util;

public class BruiToolkit {

	public static final String Id = "com.bizvisionsoft.service.bruiToolkit";

	public static final String CSS_NORMAL = "normal";

	public static final String CSS_INFO = "info";

	public static final String CSS_WARNING = "warning";

	public static final String CSS_SERIOUS = "serious";

	public static final String CSS_TEXT_BUTTON = "button";

	public static final String CSS_TEXT_MENU = "menu";

	public static final String CSS_TEXT_CAPTION = "caption";

	public static final String CSS_TEXT_BODY1 = "body1";

	public static final String CSS_TEXT_BODY2 = "body2";

	public static final String CSS_TEXT_SUBHEAD = "subhead";

	public static final String CSS_TEXT_TITLE = "title";

	/**
	 * 用于大的标题栏
	 */
	public static final String CSS_TEXT_HEADLINE = "headline";

	public static final String CSS_TEXT_DISPLAY1 = "display1";

	public static final String CSS_TEXT_DISPLAY2 = "display2";

	public static final String CSS_TEXT_DISPLAY3 = "display3";

	public static final String CSS_TEXT_DISPLAY4 = "display4";

	public static final String CSS_BAR_TITLE = "titlebar";

	public static final String CSS_PANEL_CONTENT = "content";

	public Widget enableMarkup(Widget control) {

		control.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		control.setData(MarkupValidator.MARKUP_VALIDATION_DISABLED, Boolean.TRUE);

		return control;

	}

	public <T extends Control> T newStyledControl(Class<T> type, Composite parent, int style, String id) {
		try {
			Constructor<T> c = type.getConstructor(Composite.class, int.class);
			T control = c.newInstance(parent, style);
			if (id != null) {
				control.setData(RWT.CUSTOM_VARIANT, id);
			}
			return control;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Composite newContentPanel(Composite parent) {
		Composite composite = newStyledControl(Composite.class, parent, SWT.BORDER, null);
		composite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		return composite;
	}

	public static String getResourceURL(String resPath) {
		if (!resPath.startsWith("/")) {
			resPath = "/" + resPath;
		}
		String aliasOfResFolder = ModelLoader.site.getAliasOfResFolder();
		return "rwt-resources/" + aliasOfResFolder + resPath;
	}

	public String getActionHtml(Action action, String element) {
		return getActionHtml(action, element, false);
	}

	private String getActionHtml(Action action, String element, boolean placeHolder) {
		String imgUrl = action.getImage();
		boolean forceText = action.isForceText();
		if (imgUrl == null || imgUrl.isEmpty()) {
			return getActionTextButtonHtml(action, element, placeHolder);
		} else if (forceText) {
			return getActionTextImageButtonHtml(action, element, placeHolder);
		} else {
			return getActionImageButtonHtml(action, element, placeHolder);
		}
	}

	public String getActionPlaceholderHtml(Action action) {
		return getActionHtml(action, "div", true);
	}

	public static int actionMargin = 12;

	public static int actionImgBtnWidth = 28;

	public static int actionTextBtnWidth = 64;

	public static int actionForceBtnTextWidth = 64;

	public String getActionImageButtonHtml(Action a, String element, boolean placeHolder) {
		if (placeHolder)
			return "<div style='margin-right:12px;width:28px;height:28px;'></div>";
		else
			return "<" + element + " class='cellbutton " + getStyle(a) + "' href='" + a.getId() + "' target='_rwt'>"
					+ getImage(a, 16) + "</" + element + ">";
	}

	public String getActionTextImageButtonHtml(Action a, String element, boolean placeHolder) {
		if (placeHolder)
			return "<div style='margin-right:12px;height:28px;width:" + actionForceBtnTextWidth + "px;' ></div>";
		else
			return "<" + element + " class='cellbutton " + getStyle(a) + "' style='width:" + actionForceBtnTextWidth
					+ "px;' href='" + a.getId() + "'  target='_rwt'>" + getImage(a, 16) + getText(a) + "</" + element
					+ ">";

	}

	public String getActionTextButtonHtml(Action a, String element, boolean placeHolder) {
		if (placeHolder)
			return "<div style='margin-right:12px;height:28px;width:" + actionTextBtnWidth + "px;' ></div>";
		else
			return "<" + element + " class='cellbutton " + getStyle(a) + "' style='width:" + actionTextBtnWidth
					+ "px;' href='" + a.getId() + "'  target='_rwt'>" + getText(a) + "</" + element + ">";

	}

	public String getActionMenuItemHtml(Action a) {

		String text = getImage(a, 20);
		if (text.isEmpty()) {
			text += "<div style='margin-left:24px;display:inline-block;'>" + getText(a) + "</div>";
		} else {
			text += "<div style='margin-left:4px;display:inline-block;'>" + getText(a) + "</div>";
		}
		return text;
	}

	private String getImage(Action a, int size) {
		String image = a.getImage();
		if (image != null)
			return "<img alter='" + a.getName() + "' src='" + BruiToolkit.getResourceURL(image)
					+ "' style='cursor:pointer;' width='" + size + "px' height='" + size + "px'></img>";
		return "";
	}

	private String getText(Action a) {
		String text = a.getText();
		return (text == null || text.isEmpty()) ? a.getName() : text;
	}

	private String getStyle(Action a) {
		return (a.getStyle() == null || a.getStyle().isEmpty()) ? "default" : a.getStyle();
	}

	public String createLocalFileDownloadURL(String path) {
		// 判断path的session是否与当前一致
		File file = new File(path);
		String sessionId = file.getParentFile().getName().split("_")[2].toUpperCase();
		if (RWT.getRequest().getSession().getId().toUpperCase().equals(sessionId)) {
			try {
				return "/bvs/fs?id=" + URLEncoder.encode(Util.compress(path), "utf-8");
			} catch (Exception e) {
			}
		}
		return null;
	}

	public Button createButton(Composite parent, Action a, String layoutStyle) {
		Button btn = new Button(parent, SWT.PUSH);
		enableMarkup(btn);
		String imageUrl = a.getImage();
		String buttonText = Util.isEmptyOrNull(a.getText()) ? "" : a.getText();

		String text = "";
		if (imageUrl != null && !imageUrl.isEmpty()) {
			if ("block".equals(layoutStyle)) {// 块状布局
				text += "<img alter='" + a.getName() + "' src='" + getResourceURL(a.getImage())
						+ "' style='cursor:pointer;' width='32px' height='32px'></img>";
			} else {// 行状布局
				text += "<img alter='" + a.getName() + "' src='" + getResourceURL(a.getImage())
						+ "' style='cursor:pointer;' width='20px' height='20px'></img>";
			}
		}
		if (a.isForceText()) {
			if (imageUrl != null) {
				if ("block".equals(layoutStyle)) {// 块状布局
					text += "<div style='font-size:16px;font-weight:lighter;margin-top:8px;'>" + buttonText + "</div>";
				} else {
					text += "<div style='display:inline-block;'>" + buttonText + "</div>";
				}
			} else {
				if ("block".equals(layoutStyle)) {// 块状布局
					text += "<div style='font-size:16px;font-weight:lighter;margin-top:8px;'>" + buttonText + "</div>";
				} else {
					text += "<div style='margin-left:4px;display:inline-block;'>" + buttonText + "</div>";
				}
			}
		} else {
			if ("block".equals(layoutStyle)) {// 块状布局
				text += "<div style='font-size:16px;font-weight:lighter;margin-top:8px;'>" + buttonText + "</div>";
			}
		}

		btn.setText(text);
		btn.setToolTipText(a.getTooltips());
		String style = a.getStyle();
		if (style != null && !style.isEmpty()) {
			btn.setData(RWT.CUSTOM_VARIANT, style);
		}
		return btn;
	}

}
