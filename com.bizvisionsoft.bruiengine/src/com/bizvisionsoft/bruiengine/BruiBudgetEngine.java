package com.bizvisionsoft.bruiengine;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.BruiService;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.mongocodex.tools.BsonTools;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Check;
import com.bizvisionsoft.serviceconsumer.Services;

public class BruiBudgetEngine extends BruiEngine {

	private String cName;

	public BruiBudgetEngine(Class<?> clazz) {
		super(clazz);
	}

	public BruiBudgetEngine(Object serivce) {
		super(serivce);
	}

	public BruiBudgetEngine(Class<?> clazz, Object serivce) {
		super(clazz, serivce);
	}

	public static BruiBudgetEngine create(String cName, String bundleId, String className, String serivceName,
			IServiceWithId... services) {
		BruiBudgetEngine eng = (BruiBudgetEngine) load(cName, bundleId, className, serivceName);// load
		if (eng != null)
			return (BruiBudgetEngine) eng.newInstance().init(services);
		return null;
	}

	private static BruiBudgetEngine load(String cName, String bundleId, String className, String serivceName) {
		if (bundleId != null && !bundleId.isEmpty() && className != null && !className.isEmpty()) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiBudgetEngine(bundle.loadClass(className)).setContainerName(cName);
			} catch (Exception e) {
				throw new RuntimeException(cName + "数据源定义错误。");
			}
		}

		if (Check.isAssigned(serivceName)) {
			Object[] service = Services.getService(serivceName);
			if (service != null) {
				return new BruiBudgetEngine((Class<?>) service[0], service[1]).setContainerName(cName);
			}
		}
		
		return null;
	}

	private BruiBudgetEngine setContainerName(String cName) {
		this.cName = cName;
		return this;
	}

	private void injectContextInputParameters(IBruiContext context, List<String> names, List<Object> values) {
		if (context != null) {
			Object input = context.getInput();
			if (input != null) {
				names.add(MethodParam.CONTEXT_INPUT_OBJECT);
				values.add(input);

				Object _id = BsonTools.getBson(input).get("_id");
				if (_id != null) {
					names.add(MethodParam.CONTEXT_INPUT_OBJECT_ID);
					values.add(_id);
				}
			}
		}
	}

	private void injectPageContextInputParameters(IBruiContext context, List<String> names, List<Object> values) {
		if (context != null) {
			Object input = context.getContentPageInput();
			if (input != null) {
				names.add(MethodParam.PAGE_CONTEXT_INPUT_OBJECT);
				values.add(input);

				Object _id = BsonTools.getBson(input).get("_id");
				if (_id != null) {
					names.add(MethodParam.PAGE_CONTEXT_INPUT_OBJECT_ID);
					values.add(_id);
				}
			}
		}
	}

	private void injectRootContextInputParameters(IBruiContext context, List<String> names, List<Object> values) {
		if (context != null) {
			Object input = context.getRootInput();
			if (input != null) {
				names.add(MethodParam.ROOT_CONTEXT_INPUT_OBJECT);
				values.add(input);

				Object _id = BsonTools.getBson(input).get("_id");
				if (_id != null) {
					names.add(MethodParam.ROOT_CONTEXT_INPUT_OBJECT_ID);
					values.add(_id);
				}
			}
		}
	}

	private void injectUserParameters(List<String> names, List<Object> values) {
		try {
			User user = Brui.sessionManager.getUser();
			if (user != null) {
				names.add(MethodParam.CURRENT_USER);
				values.add(user);

				names.add(MethodParam.CURRENT_USER_ID);
				values.add(user.getUserId());
			}
		} catch (Exception e) {
		}
	}

	public Object query(IBruiContext context) {
		Method method = AUtil.getContainerMethod(clazz, DataSet.class, cName, "budget", a -> a.value()).orElse(null);
		if (method != null) {
			List<String> names = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();

			injectContextInputParameters(context, names, values);

			injectPageContextInputParameters(context, names, values);

			injectRootContextInputParameters(context, names, values);

			injectUserParameters(names, values);

			return invokeMethodInjectParams(method, values.toArray(), names.toArray(new String[0]), MethodParam.class,
					t -> t.value());

		}
		throw new RuntimeException(cName + " 数据源没有注解DataSet值为 budget 的方法。");
	}

	public BruiBudgetEngine newInstance() {
		return (BruiBudgetEngine) super.newInstance();
	}

	public static Object getBudgetValue(Action action, BruiAssemblyContext context, BruiService service) {
		String budgetBundleId = action.getBudgetBundleId();
		String budgetClassName = action.getBudgetClassName();
		String budgetService = action.getBudgetServiceName();
		BruiBudgetEngine engine = BruiBudgetEngine.create(action.getName(), budgetBundleId, budgetClassName,
				budgetService, context, service);
		if (engine != null) {
			engine.newInstance().init(new IServiceWithId[] { context, service });
			Object value = engine.query(context);
			if (value instanceof Number) {
				return ((Number) value).intValue();
			} else if (value instanceof Boolean) {
				return Boolean.TRUE.equals(value);
			}
		}
		return null;
	}

}
