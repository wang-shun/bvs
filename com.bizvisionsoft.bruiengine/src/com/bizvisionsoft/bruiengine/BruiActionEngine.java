package com.bizvisionsoft.bruiengine;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Event;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.action.OpenSelected;
import com.bizvisionsoft.bruiengine.action.SwitchContentToAssembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class BruiActionEngine extends BruiEngine {

	public static BruiActionEngine create(Action action, IServiceWithId... services) {
		BruiEngine brui;
		String staId = action.getSwitchContentToAssemblyId();
		String editorId = action.getEditorAssemblyId();

		if (staId != null && !staId.isEmpty()) {// �����л�������������Action
			brui = new BruiActionEngine(new SwitchContentToAssembly(Brui.site.getAssembly(staId),action.isOpenContent()));
		} else if (editorId != null && !editorId.isEmpty()) {// ���ڴ򿪱༭����Action
			brui = new BruiActionEngine(
					new OpenSelected(Brui.site.getAssembly(editorId), action.isEditorAssemblyEditable()));
		} else {
			brui = load(action.getBundleId(), action.getClassName())// load
					.newInstance();
		}
		return (BruiActionEngine) brui.init(services);
	}

	private static BruiEngine load(String bundleId, String className) {
		if (bundleId == null || bundleId.isEmpty())
			throw new RuntimeException("���IdΪ��");
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
