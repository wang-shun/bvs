package com.bizvisionsoft.bruiengine;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetContainer;
import com.bizvisionsoft.bruicommons.annotation.GetContent;
import com.bizvisionsoft.bruicommons.annotation.GetReturnCode;
import com.bizvisionsoft.bruicommons.annotation.GetReturnResult;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.assembly.EditorPart;
import com.bizvisionsoft.bruiengine.assembly.GanttPart;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.assembly.StickerPart;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class BruiAssemblyEngine extends BruiEngine {

	private BruiAssemblyEngine(Class<?> clazz) {
		super(clazz);
	}

	private BruiAssemblyEngine(Object instance) {
		super(instance);
	}

	public static BruiAssemblyEngine create(String assemblyId, IServiceWithId... services) {
		return create(Brui.site.getAssembly(assemblyId), services);
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
			brui = new BruiAssemblyEngine(new GanttPart(assembly));
		} else if (Assembly.TYPE_EDITOR.equals(type)) {
			brui = new BruiAssemblyEngine(new EditorPart(assembly));
		} else {
			brui = load(assembly.getBundleId(), assembly.getClassName())// load
					.newInstance();
		}
		return (BruiAssemblyEngine) brui;
	}

	private static BruiAssemblyEngine load(String bundleId, String className) {
		if (bundleId == null || bundleId.isEmpty())
			throw new RuntimeException("插件Id为空");
		Bundle bundle = Platform.getBundle(bundleId);

		if (bundle == null)
			throw new RuntimeException("无法获得插件" + bundleId);
		try {
			return new BruiAssemblyEngine(bundle.loadClass(className));
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
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
		Object value = getValue(GetContent.class);
		return value;
	}

	@Override
	public BruiAssemblyEngine init(IServiceWithId[] services) {
		return (BruiAssemblyEngine) super.init(services);
	}

}
