package com.bizvisionsoft.bruiengine.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.widgets.MarkupValidator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import com.bizivisionsoft.widgets.tools.WidgetHandler;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.BruiActionEngine;
import com.bizvisionsoft.bruiengine.BruiEntryPoint;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.service.PermissionUtil;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.bruiengine.ui.ActionMenu;
import com.bizvisionsoft.service.ServicesLoader;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Check;

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

	public static final int MARGIN = 0;//8;

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
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
		}
		return null;
	}

	/**
	 * 使用Controls替代<br>
	 * 
	 * Composite content = Controls.contentPanel(parent).mLoc().mTop(bar).get();<br>
	 * 替代以下的语句：<br>
	 * Composite content = UserSession.bruiToolkit().newContentPanel(parent);<br>
	 * fd = new FormData();<br>
	 * content.setLayoutData(fd);<br>
	 * fd.left = new FormAttachment(0, 8);<br>
	 * fd.top = new FormAttachment(bar, 8);<br>
	 * fd.right = new FormAttachment(100, -8);<br>
	 * fd.bottom = new FormAttachment(100, -8);<br>
	 * content.setLayout(new FormLayout());<br>
	 * 
	 * 
	 * @param parent
	 * @return
	 */
	@Deprecated
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
			return "<" + element + " class='cellbutton " + getStyle(a) + "' href='" + a.getId() + "' target='_rwt'>" + getImage(a, 16)
					+ "</" + element + ">";
	}

	public String getActionTextImageButtonHtml(Action a, String element, boolean placeHolder) {
		if (placeHolder)
			return "<div style='margin-right:12px;height:28px;width:" + actionForceBtnTextWidth + "px;' ></div>";
		else
			return "<" + element + " class='cellbutton " + getStyle(a) + "' style='width:" + actionForceBtnTextWidth + "px;' href='"
					+ a.getId() + "'  target='_rwt'>" + getImage(a, 16) + getText(a) + "</" + element + ">";

	}

	public String getActionTextButtonHtml(Action a, String element, boolean placeHolder) {
		if (placeHolder)
			return "<div style='margin-right:12px;height:28px;width:" + actionTextBtnWidth + "px;' ></div>";
		else
			return "<" + element + " class='cellbutton " + getStyle(a) + "' style='width:" + actionTextBtnWidth + "px;' href='" + a.getId()
					+ "'  target='_rwt'>" + getText(a) + "</" + element + ">";

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
			return "<img alter='" + a.getName() + "' src='" + BruiToolkit.getResourceURL(image) + "' style='cursor:pointer;' width='" + size
					+ "px' height='" + size + "px'></img>";
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
				return "/bvs/fs?id=" + URLEncoder.encode(path, "utf-8");
			} catch (Exception e) {
				throw new RuntimeException("资源路径解码错误：" + e.getMessage());
			}
		} else {
			throw new RuntimeException("当前用户进程不允许创建要求资源的下载链接");
		}
	}

	public Button createButton(Composite parent, Action a, String layoutStyle) {
		Button btn = new Button(parent, SWT.PUSH);
		enableMarkup(btn);
		String imageUrl = a.getImage();

		String text = "";
		if (imageUrl != null && !imageUrl.isEmpty()) {
			String resourceURL = getResourceURL(imageUrl);
			int size;
			if ("block".equals(layoutStyle)) {// 块状布局
				size = 32;
			} else if ("compact".equals(layoutStyle)) {
				size = 16;
			} else {// 行状布局
				size = 20;
			}
			text += "<img src='" + resourceURL + "' style='cursor:pointer;' width='" + size + "px' height='" + size + "px'/>";
		}

		String buttonText = Check.isNotAssigned(a.getText()) ? "" : a.getText();
		if (a.isForceText()) {
			if (imageUrl != null) {
				if ("block".equals(layoutStyle)) {// 块状布局
					text += "<div style='font-size:16px;font-weight:lighter;margin-top:8px;'>" + buttonText + "</div>";
				} else if ("compact".equals(layoutStyle)) {
					text += "<div style='font-size:16px;font-weight:lighter;margin-top:2px;'>" + buttonText + "</div>";
				} else {
					text += "<div style='display:inline-block;'>" + buttonText + "</div>";
				}
			} else {
				if ("block".equals(layoutStyle)) {// 块状布局
					text += "<div style='font-size:16px;font-weight:lighter;margin-top:8px;'>" + buttonText + "</div>";
				} else if ("compact".equals(layoutStyle)) {
					text += "<div style='font-size:16px;font-weight:lighter;margin-top:2px;'>" + buttonText + "</div>";
				} else {
					text += "<div style='margin-left:4px;display:inline-block;'>" + buttonText + "</div>";
				}
			}
		} else {
			if ("block".equals(layoutStyle)) {// 块状布局
				text += "<div style='font-size:16px;font-weight:lighter;margin-top:8px;'>" + buttonText + "</div>";
			} else if ("compact".equals(layoutStyle)) {
				text += "<div style='font-size:16px;font-weight:lighter;margin-top:2px;'>" + buttonText + "</div>";
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

	public void runAction(Action action, Event event, IBruiService service, IBruiContext context) {
		Assembly assembly = context.getAssembly();
		List<Action> ca = action.getChildren();
		if (Check.isNotAssigned(ca)) {
			BruiActionEngine.execute(action, event, context, service);
		} else {
			// 显示菜单
			new ActionMenu(service).setAssembly(assembly).setInput(context.getInput()).setContext(context).setActions(ca).setEvent(event)
					.open();
		}
	}

	public boolean isAcceptAction(Object element, IBruiContext context, Assembly assembly, Action action) {
		String cName = assembly.getName();
		return isAcceptAction(element, context, action, cName);
	}

	public boolean isAcceptAction(Object element, IBruiContext context, Action action, String cName) {
		String[] paramemterNames = new String[] { MethodParam.CONTEXT_INPUT_OBJECT, MethodParam.CONTEXT_INPUT_OBJECT_ID,
				MethodParam.ROOT_CONTEXT_INPUT_OBJECT, MethodParam.ROOT_CONTEXT_INPUT_OBJECT_ID, MethodParam.CURRENT_USER,
				MethodParam.CURRENT_USER_ID };
		Object[] parameterValues = context.getContextParameters(paramemterNames);
		return AUtil.readBehavior(element, cName, action.getName(), parameterValues, paramemterNames);
	}

	public List<Action> getAcceptedActions(Assembly assembly, User user, IBruiContext context) {
		List<Action> list = assembly.getActions();
		String cName = assembly.getName();
		return getAcceptedActions(user, context, list, cName);
	}

	public List<Action> getAcceptedActions(User user, IBruiContext context, List<Action> list, String cName) {
		final List<Action> actions = new ArrayList<Action>();
		// 权限控制
		list = PermissionUtil.getPermitActions(user, list, context.getRootInput());

		if (list != null) {
			final Object input = context.getInput();
			final Object root = context.getRootInput();
			list.forEach(action -> {
				if (isAcceptAction(context, cName, input, root, action))
					actions.add(action);
			});
		}
		return actions;
	}

	private boolean isAcceptAction(IBruiContext context, String cName, Object input, Object root, Action action) {
		///////////////////////////////////
		// 先判断action是否设置了对象行为控制
		if (action.isObjectBehavier()) {
			if (input != null) {
				if (isAcceptAction(input, context, action, cName)) {
					return true;
				}
			} else if (root != null) {
				if (isAcceptAction(root, context, action, cName)) {
					return true;
				}
			}
			return false;
		}
		////////////////////////////////
		// 再判断action实现是否具有行为控制
		if (action.isActionBehavier()) {
			try {
				Object target = BruiActionEngine.create(action, context).getTarget();
				if (target != null) {
					if (isAcceptAction(target, context, action, cName)) {
						return true;
					} else {
						return false;
					}
				}
			} catch (Exception e) {
			}
		}
		return true;
	}

	private void checkLocalFileDownloadSession(String path) {
		// 判断path的session是否与当前一致
		File file = new File(path);
		String sessionId = file.getParentFile().getName().split("_")[2].toUpperCase();
		if (RWT.getRequest().getSession().getId().toUpperCase().equals(sessionId)) {
			try {
				return;
			} catch (Exception e) {
				throw new RuntimeException("资源路径解码错误：" + e.getMessage());
			}
		} else {
			throw new RuntimeException("当前用户进程不允许创建要求资源的下载链接");
		}
	}

	public void downloadLocalFile(String filePath) {
		// RWT.getClient().getService(UrlLauncher.class).openURL(url);
		checkLocalFileDownloadSession(filePath);
		WidgetHandler.getHandler().download("/bvs/fs", new JsonObject().set("id", filePath));
	}

	public void downloadServerFile(String path, JsonObject param) {
		String url = ServicesLoader.url + "/" + path + "/";
		WidgetHandler.getHandler().download(url, param);
	}

	public void bindingShortcutKey(Control control, Action a, Listener listener) {
		Check.isAssigned(a.getShortcutKey(), k -> {
			BruiEntryPoint p = UserSession.current().getEntryPoint();
			p.keybinding(k, listener);
			control.addListener(SWT.Dispose, e -> p.keyunbinding(k, listener));
		});
	}

}
