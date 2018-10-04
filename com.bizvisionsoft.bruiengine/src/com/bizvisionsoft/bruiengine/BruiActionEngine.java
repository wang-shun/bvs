package com.bizvisionsoft.bruiengine;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Event;
import org.osgi.framework.Bundle;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.md.service.ImageURL;
import com.bizvisionsoft.annotations.md.service.Label;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Tooltips;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.action.ClientSetting;
import com.bizvisionsoft.bruiengine.action.CreateItem;
import com.bizvisionsoft.bruiengine.action.CreateSelectedSubItem;
import com.bizvisionsoft.bruiengine.action.DeleteSelected;
import com.bizvisionsoft.bruiengine.action.Export;
import com.bizvisionsoft.bruiengine.action.OpenPage;
import com.bizvisionsoft.bruiengine.action.OpenSelected;
import com.bizvisionsoft.bruiengine.action.QueryInGrid;
import com.bizvisionsoft.bruiengine.action.SwitchContentToAssembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.service.PermissionUtil;
import com.bizvisionsoft.bruiengine.service.TraceUserUtil;
import com.bizvisionsoft.service.tools.Check;

public class BruiActionEngine extends BruiEngine {

	private Action action;

	public static void execute(Action action, Event event, IBruiContext context, IServiceWithId... services) {
		try {
			BruiActionEngine eng = create(action, services);
			eng.invokeExecute(event, context);
		} catch (Exception e) {
			String message = e.getMessage();
			logger.error("运行错误，Action:" + action.getName() + "[" + action.getId() + "]。错误信息:" + message, e);
			Layer.message("运行错误<br/>操作:" + action.getName() + "<br/>" + "错误信息:" + message, Layer.ICON_CANCEL);
		}
	}

	public static BruiActionEngine create(Action action, IServiceWithId... services) {
		BruiActionEngine brui;
		String type = action.getType();

		if (Action.TYPE_INSERT.equals(type)) {
			String editorId = action.getEditorAssemblyId();
			String bid = action.getCreateActionNewInstanceBundleId();
			String cla = action.getCreateActionNewInstanceClassName();
			brui = new BruiActionEngine(new CreateItem(ModelLoader.site.getAssembly(editorId), bid, cla));
		} else if (Action.TYPE_INSERT_SUBITEM.equals(type)) {
			String editorId = action.getEditorAssemblyId();
			String bid = action.getCreateActionNewInstanceBundleId();
			String cla = action.getCreateActionNewInstanceClassName();
			brui = new BruiActionEngine(new CreateSelectedSubItem(ModelLoader.site.getAssembly(editorId), bid, cla));
		} else if (Action.TYPE_EDIT.equals(type)) {
			brui = new BruiActionEngine(new OpenSelected());
		} else if (Action.TYPE_DELETE.equals(type)) {
			brui = new BruiActionEngine(new DeleteSelected());
		} else if (Action.TYPE_QUERY.equals(type)) {
			brui = new BruiActionEngine(new QueryInGrid());
		} else if (Action.TYPE_EXPORT.equals(type)) {
			brui = new BruiActionEngine(new Export());
		} else if (Action.TYPE_SETTING.equals(type)) {
			brui = new BruiActionEngine(new ClientSetting());

		} else if (Action.TYPE_CUSTOMIZED.equals(type)) {
			brui = (BruiActionEngine) load(action.getBundleId(), action.getClassName())// load
					.newInstance();

		} else if (Action.TYPE_SWITCHCONTENT.equals(type)) {
			String staId = action.getSwitchContentToAssemblyId();
			brui = new BruiActionEngine(
					new SwitchContentToAssembly(ModelLoader.site.getAssembly(staId), action.isOpenContent()));
		} else if (Action.TYPE_OPENPAGE.equals(type)) {
			String pageName = action.getOpenPageName();
			brui = new BruiActionEngine(new OpenPage(pageName));
		} else {
			brui = (BruiActionEngine) load(action.getBundleId(), action.getClassName())// load
					.newInstance();

		}
		brui.action = action;
		return (BruiActionEngine) brui.init(services);
	}

	private static BruiEngine load(String bundleId, String className) {
		if (!Check.isAssigned(bundleId, className))
			throw new RuntimeException("插件Id或ClassName为空");

		Bundle bundle = Platform.getBundle(bundleId);

		if (bundle == null)
			throw new RuntimeException("无法获得插件" + bundleId);

		Class<?> loadClass;
		try {
			loadClass = bundle.loadClass(className);
		} catch (Exception e) {
			throw new RuntimeException("无法加载插件:" + bundleId + ", 类：" + className);
		}
		return new BruiActionEngine(loadClass);
	}

	public BruiActionEngine(Class<?> clazz) {
		super(clazz);
	}

	public BruiActionEngine(Object instance) {
		super(instance);
	}

	/**
	 * 调用Action执行的方法
	 * 
	 * @param event
	 * @param context
	 */
	public void invokeExecute(Event event, IBruiContext context) {
		if (!PermissionUtil.checkAction(Brui.sessionManager.getUser(), action, context)) {
			Layer.message("用户" + Brui.sessionManager.getUser() + "没有<span style='color:red;'>" + action.getName()
					+ "</span>的权限。", Layer.ICON_LOCK);
			return;
		}
		TraceUserUtil.traceAction(action, context);
		Object[] parameters = new Object[] { action, event, context, context.getInput(), context.getContentPageInput(),
				context.getRootInput(), Brui.sessionManager.getUser() };
		String[] paramAnnotations = new String[] { Execute.PARAM_ACTION, Execute.PARAM_EVENT, Execute.PARAM_CONTEXT,
				Execute.CONTEXT_INPUT_OBJECT, Execute.PAGE_CONTEXT_INPUT_OBJECT, Execute.ROOT_CONTEXT_INPUT_OBJECT };
		invokeMethodInjectParams(Execute.class, parameters, paramAnnotations, null);
	}

	public String getImageURL(IBruiContext context) {
		Object[] parameters = new Object[] { action, context, context.getInput(), context.getContentPageInput(),
				context.getRootInput(), Brui.sessionManager.getUser() };
		String[] paramAnnotations = new String[] { Execute.PARAM_ACTION, Execute.PARAM_EVENT, Execute.PARAM_CONTEXT,
				Execute.CONTEXT_INPUT_OBJECT, Execute.PAGE_CONTEXT_INPUT_OBJECT, Execute.ROOT_CONTEXT_INPUT_OBJECT };
		return (String) invokeMethodInjectParams(ImageURL.class, parameters, paramAnnotations, null);
	}

	public String getText(IBruiContext context) {
		Object[] parameters = new Object[] { action, context, context.getInput(), context.getContentPageInput(),
				context.getRootInput(), Brui.sessionManager.getUser() };
		String[] paramAnnotations = new String[] { Execute.PARAM_ACTION, Execute.PARAM_EVENT, Execute.PARAM_CONTEXT,
				Execute.CONTEXT_INPUT_OBJECT, Execute.PAGE_CONTEXT_INPUT_OBJECT, Execute.ROOT_CONTEXT_INPUT_OBJECT };
		return (String) invokeMethodInjectParams(Label.class, parameters, paramAnnotations, null);
	}

	public String getTooltips(IBruiContext context) {
		Object[] parameters = new Object[] { action, context, context.getInput(), context.getContentPageInput(),
				context.getRootInput(), Brui.sessionManager.getUser() };
		String[] paramAnnotations = new String[] { Execute.PARAM_ACTION, Execute.PARAM_EVENT, Execute.PARAM_CONTEXT,
				Execute.CONTEXT_INPUT_OBJECT, Execute.PAGE_CONTEXT_INPUT_OBJECT, Execute.ROOT_CONTEXT_INPUT_OBJECT };
		return (String) invokeMethodInjectParams(Tooltips.class, parameters, paramAnnotations, null);
	}

}
