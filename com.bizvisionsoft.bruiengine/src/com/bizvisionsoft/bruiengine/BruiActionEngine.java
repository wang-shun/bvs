package com.bizvisionsoft.bruiengine;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Event;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.action.CreateRoot;
import com.bizvisionsoft.bruiengine.action.DeleteSelected;
import com.bizvisionsoft.bruiengine.action.OpenPage;
import com.bizvisionsoft.bruiengine.action.OpenSelected;
import com.bizvisionsoft.bruiengine.action.QueryInGrid;
import com.bizvisionsoft.bruiengine.action.SwitchContentToAssembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.bruiengine.util.Util;

public class BruiActionEngine extends BruiEngine {

	public static BruiActionEngine create(Action action, IServiceWithId... services) {
		BruiEngine brui;
		String type = action.getType();

		if (Action.TYPE_INSERT.equals(type)) {
			String editorId = action.getEditorAssemblyId();
			String bid = action.getCreateActionNewInstanceBundleId();
			String cla = action.getCreateActionNewInstanceClassName();
			brui = new BruiActionEngine(new CreateRoot(ModelLoader.site.getAssembly(editorId), bid, cla));

		} else if (Action.TYPE_EDIT.equals(type)) {
			String editorId = action.getEditorAssemblyId();
			brui = new BruiActionEngine(
					new OpenSelected(ModelLoader.site.getAssembly(editorId), action.isEditorAssemblyEditable()));

		} else if (Action.TYPE_DELETE.equals(type)) {
			brui = new BruiActionEngine(new DeleteSelected());

		} else if (Action.TYPE_QUERY.equals(type)) {
			brui = new BruiActionEngine(new QueryInGrid());

		} else if (Action.TYPE_CUSTOMIZED.equals(type)) {
			brui = load(action.getBundleId(), action.getClassName())// load
					.newInstance();

		} else if (Action.TYPE_SWITCHCONTENT.equals(type)) {
			String staId = action.getSwitchContentToAssemblyId();
			brui = new BruiActionEngine(
					new SwitchContentToAssembly(ModelLoader.site.getAssembly(staId), action.isOpenContent()));
		} else if (Action.TYPE_OPENPAGE.equals(type)) {
			String pageName = action.getOpenPageName();
			brui = new BruiActionEngine(new OpenPage(pageName));
		} else {
			brui = load(action.getBundleId(), action.getClassName())// load
					.newInstance();

		}

		return (BruiActionEngine) brui.init(services);
	}

	private static BruiEngine load(String bundleId, String className) {
		if (Util.isEmptyOrNull(bundleId) || Util.isEmptyOrNull(className))
			throw new RuntimeException("���Id��ClassNameΪ��");

		Bundle bundle = Platform.getBundle(bundleId);

		if (bundle == null)
			throw new RuntimeException("�޷���ò��" + bundleId);
		try {
			return new BruiActionEngine(bundle.loadClass(className));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public BruiActionEngine(Class<?> clazz) {
		super(clazz);
	}

	public BruiActionEngine(Object instance) {
		super(instance);
	}

	/**
	 * ����Actionִ�еķ���
	 * 
	 * @param event
	 * @param context
	 */
	public void invokeExecute(Event event, IBruiContext context) {
		invokeMethodInjectParams(Execute.class, new Object[] { event, context },
				new String[] { Execute.PARAM_EVENT, Execute.PARAM_CONTEXT }, null);
	}

}
