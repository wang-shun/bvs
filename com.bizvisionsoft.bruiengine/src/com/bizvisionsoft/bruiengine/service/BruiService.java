package com.bizvisionsoft.bruiengine.service;

import java.util.Date;

import org.bson.types.ObjectId;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
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
		return Brui.sessionManager.getUser();
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
	public void loginUser(User user) throws Exception {
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
			throw new RuntimeException("ȱ��ҳ�涨�塣" + pageName);
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


}
