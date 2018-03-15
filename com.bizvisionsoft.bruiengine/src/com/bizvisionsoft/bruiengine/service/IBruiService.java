package com.bizvisionsoft.bruiengine.service;

import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.model.User;

public interface IBruiService extends IServiceWithId {

	public static String Id = "com.bizvisionsoft.service.bruiService";

	public boolean closeCurrentPart();

	public User getCurrentUserInfo();

	public void setCurrentUserInfo(User user);

	public String getResourceURL(String resPath);

	public Shell getCurrentShell();

	// TODO ªÒµ√Webserviceµÿ÷∑

	public default String getServiceId() {
		return Id;
	}

	public void switchContent(Assembly assembly, Object input);

	public void switchContentByName(String assemblyName, Object input);

	public Editor open(Assembly assembly, Object input, boolean editable, IBruiContext context);

	public Editor openByName(String assemblyName, Object input, boolean editable, IBruiContext context);

}
