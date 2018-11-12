package com.bizvisionsoft.bruiengine;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.GetContainer;
import com.bizvisionsoft.annotations.ui.common.GetContent;
import com.bizvisionsoft.annotations.ui.common.GetReturnCode;
import com.bizvisionsoft.annotations.ui.common.GetReturnResult;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.assembly.ActionPanelPart;
import com.bizvisionsoft.bruiengine.assembly.ChartPart;
import com.bizvisionsoft.bruiengine.assembly.EditorPart;
import com.bizvisionsoft.bruiengine.assembly.FlowPart;
import com.bizvisionsoft.bruiengine.assembly.GanttPart;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.assembly.InfopadPart;
import com.bizvisionsoft.bruiengine.assembly.MessengerInboxPart;
import com.bizvisionsoft.bruiengine.assembly.SchedulerPart;
import com.bizvisionsoft.bruiengine.assembly.SelectorPart;
import com.bizvisionsoft.bruiengine.assembly.StickerPart;
import com.bizvisionsoft.bruiengine.assembly.TreePart;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class BruiAssemblyEngine extends BruiEngine {

	private BruiAssemblyEngine(Class<?> clazz) {
		super(clazz);
	}

	private BruiAssemblyEngine(Object instance) {
		super(instance);
	}

	public static BruiAssemblyEngine create(String assemblyId, IServiceWithId... services) {
		return create(ModelLoader.site.getAssembly(assemblyId), services);
	}

	public static BruiAssemblyEngine create(Assembly assembly, IServiceWithId... services) {
		return (BruiAssemblyEngine) newInstance(assembly).init(services);
	}

	public static BruiAssemblyEngine newInstance(Assembly assembly) {
		String type = assembly.getType();
		BruiEngine brui;
		if (Assembly.TYPE_STICKER.equals(type)) {
			brui = new BruiAssemblyEngine(new StickerPart(assembly));
		} else if (Assembly.TYPE_GRID.equals(type)) {
			brui = new BruiAssemblyEngine(new GridPart(assembly));
		} else if (Assembly.TYPE_GANTT.equals(type)) {
			brui = new BruiAssemblyEngine(new GanttPart());
		} else if (Assembly.TYPE_SCHEDULER.equals(type)) {
			brui = new BruiAssemblyEngine(new SchedulerPart());
		} else if (Assembly.TYPE_TREE.equals(type)) {
			brui = new BruiAssemblyEngine(new TreePart());
		} else if (Assembly.TYPE_EDITOR.equals(type)) {
			brui = new BruiAssemblyEngine(new EditorPart(assembly));
		} else if (Assembly.TYPE_INFOPAD.equals(type)) {
			brui = new BruiAssemblyEngine(new InfopadPart(assembly));
		} else if (Assembly.TYPE_SELECTOR.equals(type)) {
			brui = new BruiAssemblyEngine(new SelectorPart(assembly));
		} else if (Assembly.TYPE_ACTION_PANEL.equals(type)) {
			brui = new BruiAssemblyEngine(new ActionPanelPart(assembly));
		} else if (Assembly.TYPE_MESSENGER.equals(type)) {
			brui = new BruiAssemblyEngine(new MessengerInboxPart(assembly));
		} else if (Assembly.TYPE_CHART.equals(type)) {
			brui = new BruiAssemblyEngine(new ChartPart(assembly));
		} else if (Assembly.TYPE_FLOW.equals(type)) {
			brui = new BruiAssemblyEngine(new FlowPart(assembly));
			// } else if (Assembly.TYPE_BOARD.equals(type)) {
			// 暂时不支持
			// brui = new BruiAssemblyEngine(new BoardPart(assembly));
		} else {
			brui = load(assembly.getBundleId(), assembly.getClassName())// load
					.newInstance();
		}

		brui.injectModelParameters(assembly.getParameters());
		return (BruiAssemblyEngine) brui;
	}

	private static BruiAssemblyEngine load(String bundleId, String className) {
		if (bundleId == null || bundleId.isEmpty())
			throw new RuntimeException("插件Id为空");
		Bundle bundle = Platform.getBundle(bundleId);

		if (bundle == null)
			throw new RuntimeException("无法获得插件" + bundleId);
		Class<?> claz;
		try {
			claz = bundle.loadClass(className);
		} catch (Exception e) {
			throw new RuntimeException("无法加载" + className);
		}
		return new BruiAssemblyEngine(claz);
	}

	/**
	 * 组件创建UI的方法
	 * 
	 * @param parent
	 * @return
	 */
	public BruiAssemblyEngine createUI(Composite parent) {
		invokeMethod(CreateUI.class, parent);
		return this;
	}

	/**
	 * 获得popup返回值
	 * 
	 * @return
	 */
	public Integer getReturnCode() {
		return (Integer) getValue(GetReturnCode.class);
	}

	public Object getReturnObject() {
		return getValue(GetReturnResult.class);
	}

	/**
	 * 获得组件返回的容器
	 * 
	 * @return
	 */
	public Composite getContainer() {
		Object value = getValue(GetContainer.class);
		if (value instanceof Composite) {
			return (Composite) value;
		}
		return null;
	}

	public Object getContent(String name) {
		if (name.equals("this")) {
			return target;
		}
		Field f = AUtil.getField(target.getClass(), GetContent.class, name, t -> t.value()).orElse(null);
		if (f != null) {
			f.setAccessible(true);
			try {
				return f.get(target);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	public BruiAssemblyEngine init(IServiceWithId[] services) {
		return (BruiAssemblyEngine) super.init(services);
	}

	public BruiAssemblyEngine injectModelParameters(String jsonString) {
		return (BruiAssemblyEngine) super.injectModelParameters(jsonString);
	}

}
