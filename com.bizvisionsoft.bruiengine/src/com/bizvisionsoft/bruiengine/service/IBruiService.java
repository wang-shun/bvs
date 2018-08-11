package com.bizvisionsoft.bruiengine.service;

import java.util.Date;

import org.bson.types.ObjectId;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Page;
import com.bizvisionsoft.service.model.Command;
import com.bizvisionsoft.service.model.OperationInfo;
import com.bizvisionsoft.service.model.User;

public interface IBruiService extends IServiceWithId {

	public static String Id = "com.bizvisionsoft.service.bruiService";

	public boolean closeCurrentPart();

	public User getCurrentUserInfo();
	
	public String getCurrentUserId();

	public void loginUser(User user);

	public String getResourceURL(String resPath);

	public Shell getCurrentShell();

	public default String getServiceId() {
		return Id;
	}

	public void switchContent(Assembly assembly, Object input);

	public void switchContent(String assemblyName, Object input);

	public void openContent(Assembly assembly, Object input);

	public void switchPage(String pageName, String inputUid);

	public void switchPage(Page page, String inputUid);

	public default Assembly getAssembly(String name) {
		return ModelLoader.site.getAssemblyByName(name);
	}

	public void closeCurrentContent();

	OperationInfo operationInfo();
	
	public boolean confirm(String title, String message);

	Command command(ObjectId target_id,Date date,String name);

	public void switchMnt(boolean selection);

	public void backup();
}
