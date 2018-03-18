package com.bizvisionsoft.bruiengine.service;

import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.ui.ContentWidget;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.bruiengine.ui.Part;
import com.bizvisionsoft.service.model.User;

public class BruiService implements IBruiService {

	private Part part;

	private ContentWidget contentWidget;

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
		String aliasOfResFolder = Brui.site.getAliasOfResFolder();
		return "rwt-resources/" + aliasOfResFolder + resPath;
	}

	@Override
	public Shell getCurrentShell() {
		return part.getShell();
	}

	public void setContentWidget(ContentWidget contentWidget) {
		this.contentWidget = contentWidget;
	}

	@Override
	public void switchContent(Assembly assembly, Object input) {
		contentWidget.switchAssembly(assembly);
	}

	@Override
	public void switchContentByName(String assemblyName, Object input) {
		switchContent(Brui.site.getAssemblyByName(assemblyName), input);
	}

	@Override
	public Editor createEditor(Assembly assembly, Object input, boolean editable, boolean ignoreNull, IBruiContext context) {
		return new Editor(assembly, context).setEditable(editable).setInput(input).setIgnoreNull(ignoreNull);
	}

	@Override
	public Editor createEditorByName(String assemblyName, Object input, boolean editable, boolean ignoreNull,
			IBruiContext context) {
		return createEditor(Brui.site.getAssemblyByName(assemblyName), input, editable, ignoreNull, context);
	}

}
