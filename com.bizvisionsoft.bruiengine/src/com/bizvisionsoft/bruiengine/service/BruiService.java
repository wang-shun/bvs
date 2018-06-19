package com.bizvisionsoft.bruiengine.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.ui.Part;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.service.model.Command;
import com.bizvisionsoft.service.model.CreationInfo;
import com.bizvisionsoft.service.model.IAuthControled;
import com.bizvisionsoft.service.model.User;

public class BruiService implements IBruiService {

	final private Part part;

	public BruiService(Part part) {
		this.part = part;
	}

	@Override
	public boolean closeCurrentPart() {
		return part.close();
	}

	@Override
	public User getCurrentUserInfo() {
		return Brui.sessionManager.getSessionUserInfo();
	}

	public CreationInfo creationInfo() {
		CreationInfo info = new CreationInfo();
		User user = getCurrentUserInfo();
		info.userId = user.getUserId();
		info.userName = user.getName();
		info.date = new Date();
		return info;
	}

	@Override
	public void loginUser(User user) {
		Brui.sessionManager.setSessionUserInfo(user);
	}

	public void loginUser() {
		Brui.sessionManager.updateSessionUserInfo();
	}

	@Override
	public String getResourceURL(String resPath) {
		if (!resPath.startsWith("/")) {
			resPath = "/" + resPath;
		}
		String aliasOfResFolder = ModelLoader.site.getAliasOfResFolder();
		return "rwt-resources/" + aliasOfResFolder + resPath;
	}

	@Override
	public Shell getCurrentShell() {
		return part.getShell();
	}

	@Override
	public void switchContent(Assembly assembly, Object input) {
		if (part instanceof View) {
			((View) part).switchAssemblyInContentArea(assembly, input);
		}
	}

	@Override
	public void openContent(Assembly assembly, Object input) {
		if (part instanceof View) {
			((View) part).openAssemblyInContentArea(assembly, input);
		}
	}

	@Override
	public void closeCurrentContent() {
		if (part instanceof View) {
			((View) part).closeCurrentContent();
		}
	}

	@Override
	public void switchContent(String assemblyName, Object input) {
		switchContent(getAssembly(assemblyName), input);
	}

	@Override
	public void switchPage(String pageName, String inputUid) {
		Page page = ModelLoader.site.getPageByName(pageName);
		if (page == null) {
			throw new RuntimeException("缺少页面定义。" + pageName);
		}
		switchPage(page, inputUid);
	}

	@Override
	public void switchPage(Page page, String inputUid) {
		UserSession.current().getEntryPoint().switchPage(page, inputUid, true);
	}

	@Override
	public String getCurrentUserId() {
		return getCurrentUserInfo().getUserId();
	}

	@Override
	public boolean confirm(String title, String message) {
		return MessageDialog.openConfirm(getCurrentShell(), title, message);
	}

	@Override
	public Command command(ObjectId target_id, Date date, String name) {
		return Command.newInstance(name, getCurrentUserId(), date, target_id);
	}

	@Override
	public Command command(ObjectId target_id, Date date) {
		return Command.newInstance(null, getCurrentUserId(), date, target_id);
	}

	@Override
	public Command command(ObjectId target_id) {
		return Command.newInstance(null, getCurrentUserId(), new Date(), target_id);
	}

	public Assembly getRolebasedPageContent(Page page) {
		List<AssemblyLink> links = page.getContentArea().getAssemblyLinks();
		Assert.isTrue(links != null && links.size() > 0, "缺少内容区组件。");

		AssemblyLink matchedLink = null;
		AssemblyLink defaultLink = null;

		List<String> userRoles = getCurrentUserRoles();

		for (int i = 0; i < links.size(); i++) {
			AssemblyLink link = links.get(i);
			List<String> linkRoles = readRoles(link.getRole());

			if (checkRole(userRoles, linkRoles)) {
				matchedLink = link;
				break;
			}
			if (link.isDefaultAssembly()) {
				defaultLink = link;
			}
		}

		if (matchedLink == null) {
			matchedLink = defaultLink;
		}

		Assert.isNotNull(matchedLink, "缺少对应角色的内容区组件。");
		Assembly assembly = ModelLoader.site.getAssembly(matchedLink.getId());
		Assert.isNotNull(assembly, "内容区组件id对应组件不存在。");
		return assembly;
	}

	private List<String> getCurrentUserRoles() {
		List<String> roles = getCurrentUserInfo().getRoles();
		if (roles == null) {
			return new ArrayList<>();
		} else {
			return roles;
		}
	}

	private boolean checkRole(List<String> userRoles, List<String> reqRoles) {
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

	private List<String> readRoles(String role) {
		if (role == null || role.trim().isEmpty())
			return new ArrayList<>();
		List<String> result = new ArrayList<>();
		Arrays.asList(role.split("#")).forEach(s -> result.add(s.trim()));
		return result;
	}

	public List<Action> getPermitActions(List<Action> actions, IAuthControled iac) {
		User user = getCurrentUserInfo();
		boolean administrator = user.isSA();
		List<String> roles = Optional.ofNullable(iac).map(i -> i.getRoles(user.getUserId())).orElse(user.getRoles());
		boolean buzAdmin = user.isBuzAdmin();

		List<Action> result = new ArrayList<>();
		if (actions != null) {
			actions.forEach(action -> {
				List<String> actionRoles = readRoles(action.getRole());
				if (administrator || buzAdmin || checkRole(roles, actionRoles)) {
					result.add(action);
				}
			});
		}
		return result;
	}


}
