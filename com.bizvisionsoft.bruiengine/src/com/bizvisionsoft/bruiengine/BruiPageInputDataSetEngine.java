package com.bizvisionsoft.bruiengine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.serviceconsumer.Services;

public class BruiPageInputDataSetEngine extends BruiEngine {

	private Page page;

	public BruiPageInputDataSetEngine(Class<?> clazz) {
		super(clazz);
	}

	public BruiPageInputDataSetEngine(Object serivce) {
		super(serivce);
	}

	public BruiPageInputDataSetEngine(Class<?> clazz, Object serivce) {
		super(clazz, serivce);
	}

	public static BruiPageInputDataSetEngine create(Page page) {
		return (BruiPageInputDataSetEngine) load(page)// load
				.newInstance();
	}

	private static BruiPageInputDataSetEngine load(Page page) {
		String bundleId = page.getInputDataSetBundleId();
		String className = page.getInputDataSetClassName();

		if (bundleId != null && !bundleId.isEmpty() && className != null && !className.isEmpty()) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiPageInputDataSetEngine(bundle.loadClass(className)).setPage(page);
			} catch (Exception e) {
				throw new RuntimeException(e.getCause());
			}
		}

		String serivceName = page.getInputDataSetService();
		if (serivceName != null) {
			Object[] service = Services.getService(serivceName);
			if (service != null) {
				return new BruiPageInputDataSetEngine((Class<?>) service[0], service[1]).setPage(page);
			}
		}
		throw new RuntimeException(page.getName() + "缺少数据源定义。请在BruiDesigner页面中定义基于插件的数据源或直接指定服务。");
	}

	private BruiPageInputDataSetEngine setPage(Page page) {
		this.page = page;
		return this;
	}

	public Object getInput(String inputUid) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, page.getName(), DataSet.INPUT, a -> a.value())
				.orElse(null);
		if (method != null) {
			Object[] args = new Object[method.getParameterCount()];
			Parameter[] para = method.getParameters();
			for (int i = 0; i < para.length; i++) {
				MethodParam sp = para[i].getAnnotation(MethodParam.class);
				if (sp != null) {
					String paramName = sp.value();
					if (paramName.equals("_id")) {
						args[i] = new ObjectId(inputUid);
					} else {
						args[i] = null;
					}
				} else {
					args[i] = null;
				}
			}

			try {
				method.setAccessible(true);
				return method.invoke(getTarget(), args);
			} catch (IllegalAccessException | IllegalArgumentException e) {// 访问错误，参数错误视作没有定义该方法。
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			} catch (InvocationTargetException e) {
				e.getTargetException().printStackTrace();
				throw new RuntimeException(e.getTargetException().getMessage());
			}
		}else {
			throw new RuntimeException(page.getName() + "的数据源没有注解DataSet值为 Input的方法。");
		}
	}

}
