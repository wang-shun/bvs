package com.bizvisionsoft.bruiengine.service;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.AssemblyLink;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.ui.Part;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.service.model.Command;
import com.bizvisionsoft.service.model.CreationInfo;
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
	public void setCurrentUserInfo(User user) {
		Brui.sessionManager.setSessionUserInfo(user);
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
		List<String> roles = getCurrentUserInfo().getRoles();
		List<AssemblyLink> links = page.getContentArea().getAssemblyLinks();
		Assert.isTrue(links != null && links.size() > 0, "缺少内容区组件。");

		AssemblyLink matchedLink = null;
		AssemblyLink defaultLink = null;

		for (int i = 0; i < links.size(); i++) {
			AssemblyLink link = links.get(i);
			if (roles != null && roles.size() > 0 && roles.contains(link.getRole())) {
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

}
