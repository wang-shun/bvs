package com.bizvisionsoft.bruiengine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.bruicommons.annotation.CreateUI;
import com.bizvisionsoft.bruicommons.annotation.GetContainer;
import com.bizvisionsoft.bruicommons.annotation.GetContent;
import com.bizvisionsoft.bruicommons.annotation.GetReturnCode;
import com.bizvisionsoft.bruicommons.annotation.GetReturnResult;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.assembly.DataEditor;
import com.bizvisionsoft.bruiengine.assembly.DataGrid;
import com.bizvisionsoft.bruiengine.assembly.Sticker;
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
			brui = new BruiAssemblyEngine(new Sticker(assembly));
		} else if (Assembly.TYPE_GRID.equals(type)) {
			brui = new BruiAssemblyEngine(new DataGrid(assembly));
		} else if (Assembly.TYPE_EDITOR.equals(type)) {
			brui = new BruiAssemblyEngine(new DataEditor(assembly));
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
		Field field = Arrays.asList(clazz.getDeclaredFields()).stream()
				.filter(f -> f.getAnnotation(GetContainer.class) != null).findFirst().orElse(null);
		Object result;
		if (field != null) {
			try {
				field.setAccessible(true);
				result = field.get(target);
				if (result instanceof Composite) {
					return (Composite) result;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				new RuntimeException(e.getCause());
			}
		}
		Method method = Arrays.asList(clazz.getDeclaredMethods()).stream()
				.filter(f -> f.getAnnotation(GetContainer.class) != null).findFirst().orElse(null);
		if (method != null) {
			try {
				method.setAccessible(true);
				result = method.invoke(target);
				if (result instanceof Composite) {
					return (Composite) result;
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				new RuntimeException(e.getCause());
			}
		}
		return null;

	}

	public Object getContent(String name) {
		if (name.equals("this")) {
			return target;
		}
		return getField(clazz, e -> {
			GetContent anno = e.getAnnotation(GetContent.class);
			return anno != null && anno.value().equals(name);
		}).map(f -> {
			try {
				f.setAccessible(true);
				return f.get(target);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				return null;
			}
		}).orElse(getMethod(clazz, e -> {
			GetContent anno = e.getAnnotation(GetContent.class);
			return anno != null && anno.value().equals(name);
		}).map(f -> {
			try {
				f.setAccessible(true);
				return f.invoke(target);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				return null;
			}
		}).orElse(null));
	}

	@Override
	public BruiAssemblyEngine init(IServiceWithId[] services) {
		return (BruiAssemblyEngine) super.init(services);
	}

}
