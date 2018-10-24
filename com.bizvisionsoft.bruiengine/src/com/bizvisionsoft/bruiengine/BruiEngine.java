package com.bizvisionsoft.bruiengine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.UniversalCommand;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.ui.common.Init;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;
import com.bizvisionsoft.mongocodex.tools.BsonTools;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Check;

public class BruiEngine {

	@ReadValue
	protected Class<?> clazz;
	protected Object target;
	protected IServiceWithId[] services;
	protected static Logger logger = LoggerFactory.getLogger(BruiEngine.class);

	protected BruiEngine(Class<?> clazz) {
		this.clazz = clazz;
	}

	protected BruiEngine(Object instance) {

		this.clazz = instance.getClass();
		target = instance;
	}

	protected BruiEngine(Class<?> clazz, Object instance) {
		this.clazz = clazz;
		target = instance;
	}

	protected BruiEngine() {
	}

	final protected <T extends Annotation> Object invokeMethod(Class<T> methodAnnotation, Object... arg) {
		return invokeMethodInjectParams(methodAnnotation, arg, null, null);
	}

	final protected <T extends Annotation> Object invokeMethodInjectParams(Class<T> methodAnnotation,
			Object[] parameters, String[] paramAnnotations, Object defaultValueForNoMethod) {
		return AUtil.getMethod(clazz, methodAnnotation).map(method -> {
			return invokeMethodInjectParams(method, parameters, paramAnnotations, MethodParam.class, m -> m.value());
		}).orElse(defaultValueForNoMethod);
	}

	protected <T extends Annotation> Object invokeMethodInjectParams(Method method, Object[] parameters,
			String[] paramAnnotations, Class<T> parameterAnnotationClass,
			Function<T, String> howToGetParameterNameFromAnnotation) {
		return AUtil.invokeMethodInjectParams(getTarget(), method, parameters, paramAnnotations,
				parameterAnnotationClass, howToGetParameterNameFromAnnotation);
	}

	// final protected <T extends Annotation> Object getFieldValue(Class<T>
	// annoClass) {
	// try {
	// Field field = AUtil.getField(clazz, annoClass).orElse(null);
	// if (field != null) {
	// field.setAccessible(true);
	// return field.get(target);
	// } else {
	// return null;
	// }
	// } catch (IllegalAccessException e) {
	// throw new RuntimeException("注解为" + annoClass + "的字段或方法无法访问。", e);
	// } catch (IllegalArgumentException e1) {
	// throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e1);
	// }
	// }

	final protected <T extends Annotation> Object getValue(Class<T> annoClass) {
		return AUtil.getValue(clazz, annoClass, target);
	}

	final protected <T extends Annotation> void setFieldValue(Class<T> annoClass, Object value) {
		try {
			Field field = AUtil.getField(clazz, annoClass).orElse(null);
			if (field != null) {
				field.setAccessible(true);
				field.set(target, value);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("注解为" + annoClass + "的字段或方法无法访问。", e);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException("注解为" + annoClass + "的方法参数错误。", e1);
		}
	}

	public BruiEngine newInstance() {
		try {
			if (clazz != null)
				target = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			new RuntimeException(clazz + "无法实例化。", e);
		}
		return this;
	}

	public BruiEngine init(IServiceWithId[] services) {
		this.services = services;
		if (clazz == null)
			return this;
		for (int i = 0; i < services.length; i++) {
			injectService(services[i]);
		}
		invokeMethod(Init.class);
		return this;
	}

	/**
	 * 注入服务
	 * 
	 * @param service
	 * @return
	 */
	private BruiEngine injectService(IServiceWithId service) {
		return injectField(service.getServiceId(), service, false);
	}

	private BruiEngine injectField(String serviceName, Object srcValue, boolean byFieldName) {
		final Class<? extends Object> clas = srcValue.getClass();
		Arrays.asList(clazz.getDeclaredFields()).forEach(f -> {
			Inject anno = f.getAnnotation(Inject.class);
			if (anno != null) {
				Object value = null;
				String name = anno.name();
				if (name.isEmpty()) {
					if (byFieldName) {
						if (f.getName().equalsIgnoreCase(serviceName)) {
							value = srcValue;
						}
					} else {
						Class<?> type = f.getType();
						if (type.isAssignableFrom(clas) || type.equals(clas)) {
							value = srcValue;
						}
					}
				} else if (serviceName.equals(name)) {
					value = srcValue;
				}

				if (value != null) {
					try {
						f.setAccessible(true);
						f.set(target, value);
					} catch (NullPointerException e) {
						throw new RuntimeException("注入字段" + f.getName() + "时目标对象空指针异常。", e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("注入字段" + f.getName() + "时目标对象无法访问。", e);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException("注入字段" + f.getName() + "时目标对象参数错误。", e);
					}
				}
			}
		});
		return this;
	}

	public Object getTarget() {
		return target;
	}

	protected void injectCommonParameters(IBruiContext context, List<String> names, List<Object> values,
			String modelClassName) {
		if (context != null) {
			injectContextInputParameters(context, names, values);

			injectPageContextInputParameters(context, names, values);

			injectRootContextInputParameters(context, names, values);
		}

		injectUniversalCommandParameters(names, values, modelClassName);

		injectUserParameters(names, values);
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

	private void injectUniversalCommandParameters(List<String> names, List<Object> values, String modelClassName) {
		if (modelClassName != null && !modelClassName.isEmpty()) {
			names.add(UniversalCommand.PARAM_TARGET_CLASS);
			values.add(modelClassName);
		}
	}

	public BruiEngine injectModelParameters(String jsonString) {
		if (Check.isAssigned(jsonString)) {
			try {
				Document document = Document.parse(jsonString);
				document.entrySet().forEach(e -> injectField(e.getKey(), e.getValue(), true));
			} catch (Exception e) {
				logger.warn("获取模型参数错误", e);
			}
		}
		return this;
	}

}
