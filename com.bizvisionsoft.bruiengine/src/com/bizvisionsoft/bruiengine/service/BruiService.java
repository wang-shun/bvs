package com.bizvisionsoft.bruiengine.service;

import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.session.UserSession;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.bruiengine.ui.Part;
import com.bizvisionsoft.bruiengine.ui.View;
import com.bizvisionsoft.service.model.User;

public class BruiService implements IBruiService {

	private Part part;

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

	@Override
	public void setCurrentUserInfo(User usrinfo) {
		Brui.sessionManager.setSessionUserInfo(usrinfo);
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
		if(part instanceof View) {
			((View)part).switchAssemblyInContentArea(assembly, input);
		}
	}

	@Override
	public void openContent(Assembly assembly, Object input) {
		if(part instanceof View) {
			((View)part).openAssemblyInContentArea(assembly, input);
		}
	}
	
	@Override
	public void closeCurrentContent() {
		if(part instanceof View) {
			((View)part).closeCurrentContent();
		}		
	}

	@Override
	public void switchContent(String assemblyName, Object input) {
		switchContent(getAssembly(assemblyName), input);
	}

	@Override
	public void switchPage(String pageName, String inputUid) {
		Page page = ModelLoader.site.getPageByName(pageName);
		switchPage(page, inputUid);
	}

	@Override
	public void switchPage(Page page, String inputUid) {
		UserSession.current().getEntryPoint().switchPage(page, inputUid, true);
	}

	@Override
	@Deprecated
	public <T> Editor<T> createEditor(Assembly assembly, T input, boolean editable, boolean ignoreNull,
			IBruiContext context) {
		return new Editor<T>(assembly, context).setEditable(editable).setInput(input).setIgnoreNull(ignoreNull);
	}

	@Override
	@Deprecated
	public <T> Editor<T> createEditorByName(String assemblyName, T input, boolean editable, boolean ignoreNull,
			IBruiContext context) {
		return createEditor(getAssembly(assemblyName), input, editable, ignoreNull, context);
	}


}
