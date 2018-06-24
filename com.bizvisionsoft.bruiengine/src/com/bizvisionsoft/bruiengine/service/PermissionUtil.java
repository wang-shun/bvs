package com.bizvisionsoft.bruiengine.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.md.service.RoleBased;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.service.model.User;

public class PermissionUtil {

	public static List<Action> getPermitActions(User user, List<Action> actions, Object iac) {
		boolean administrator = user.isSU();
		boolean buzAdmin = user.isBuzAdmin();

		List<String> roles = readUserRoles(user,iac);

		List<Action> result = new ArrayList<>();
		if (actions != null) {
			actions.forEach(action -> {
				List<String> actionRoles = readRoles(action.getRole());
				List<String> actionExcludeRoles = readRoles(action.getExcludeRole());
				if (administrator || buzAdmin) {
					result.add(action);
				} else if (matchedRole(roles, actionRoles) && !exculeRole(roles, actionExcludeRoles)) {
					result.add(action);
				}
			});
		}
		return result;
	}
	
	public static Assembly getRolebasedPageContent(User user,Page page, Object iac) {
		List<AssemblyLink> links = page.getContentArea().getAssemblyLinks();
		Assert.isTrue(links != null && links.size() > 0, "缺少内容区组件。");

		List<AssemblyLink> matched = new ArrayList<>();
		List<AssemblyLink> excluded = new ArrayList<>();
		List<AssemblyLink> defaultLink = new ArrayList<>();

		List<String> userRoles = readUserRoles(user,iac);

		for (int i = 0; i < links.size(); i++) {
			AssemblyLink link = links.get(i);

			if (link.isDefaultAssembly()) {
				defaultLink.add(link);
			}

			List<String> linkRoles = readRoles(link.getRole());
			if (matchedRole(userRoles, linkRoles)) {
				matched.add(link);
			}

			List<String> linkExcludeRoles = readRoles(link.getExcludeRole());
			if (exculeRole(userRoles, linkExcludeRoles)) {
				excluded.add(link);
			}

		}

		matched.removeAll(excluded);
		if(matched.isEmpty()) {
			matched.addAll(defaultLink);
		}
		Assert.isLegal(!matched.isEmpty(), "缺少对应角色的内容区组件。");
		Assembly assembly = ModelLoader.site.getAssembly(matched.get(0).getId());
		Assert.isNotNull(assembly, "内容区组件id对应组件不存在。");
		return assembly;
	}
	
	public static boolean checkAction(User user,Action action,IBruiContext context) {
		List<Action> a = PermissionUtil.getPermitActions(user, Arrays.asList(action), context.getRootInput());
		if (a.isEmpty()) {
			Layer.message("用户" + user + "没有<span style='color:red;'>" + action.getName() + "</span>的权限。",
					Layer.ICON_LOCK);
			return false;
		}
		return true;
	}
	
	private static List<String> readRoles(String role) {
		if (role == null || role.trim().isEmpty())
			return new ArrayList<>();
		List<String> result = new ArrayList<>();
		Arrays.asList(role.split("#")).forEach(s -> result.add(s.trim()));
		return result;
	}
	
	private static boolean matchedRole(List<String> userRoles, List<String> reqRoles) {
		if (reqRoles.isEmpty()) {// 没有需求的角色
			return true;
		}
		if (userRoles.isEmpty()) {// 用户没有定义角色
			return false;
		}
		for (int i = 0; i < reqRoles.size(); i++) {
			if (userRoles.contains(reqRoles.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean exculeRole(List<String> userRoles, List<String> reqRoles) {
		if (reqRoles.isEmpty()) {// 没有需求的角色
			return false;
		}
		if (userRoles.isEmpty()) {// 用户没有定义角色
			return false;
		}
		for (int i = 0; i < reqRoles.size(); i++) {
			if (userRoles.contains(reqRoles.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private static List<String> readUserRoles(User user,Object iac) {
		List<String> roles = null;
		if (iac != null) {// 带有输入对象控制权限的
			Method method = AUtil.getMethod(iac.getClass(), RoleBased.class).orElse(null);
			if (method != null) {
				String[] paramemterNames = new String[] { MethodParam.CURRENT_USER_ID };
				Object[] parameterValues = new Object[] { user.getUserId() };
				Object value = AUtil.invokeMethodInjectParams(iac, method, parameterValues, paramemterNames,
						MethodParam.class, f -> f.value());
				if (value instanceof List) {
					roles = (List<String>) value;
				} else if (value instanceof String[]) {
					roles = Arrays.asList((String[]) value);
				} else if (value != null) {
					throw new RuntimeException("注解为RoleBased的方法，必须返回null（交由用户角色判断）, List<String> 或者 String[]。");
				}
			}
		}

		if (roles == null) {
			roles = user.getRoles();
		}

		if (roles == null) {
			roles = new ArrayList<>();
		}

		return roles;
	}
}
