package com.bizvisionsoft.bruiengine.service;

import java.util.Date;
import java.util.function.Consumer;

import org.bson.types.ObjectId;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruicommons.ModelLoader;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.ModelObject;
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

	public void switchContent(Assembly assembly, Object input, String parameters);

	public default void switchContent(Assembly assembly, Object input) {
		switchContent(assembly, input, null);
	}

	public default void switchContent(String assemblyName, Object input) {
		switchContent(assemblyName, input, null);
	}

	public default void switchContent(String assemblyName, Object input, String parameters) {
		switchContent(getAssembly(assemblyName), input,null);
	}
	
	public default void openContent(String assemblyName, Object input) {
		openContent(getAssembly(assemblyName), input, null, null);
	}

	public default void openContent(Assembly assembly, Object input) {
		openContent(assembly, input, null, null);
	}

	public default void openContent(Assembly assembly, Object input, String parameters) {
		openContent(assembly, input, parameters, null);
	}

	public default void openContent(Assembly assembly, Object input, Consumer<BruiAssemblyContext> callback) {
		openContent(assembly, input, null, callback);
	}

	public void openContent(Assembly assembly, Object input, String parameters, Consumer<BruiAssemblyContext> callback);

	public void switchPage(String pageName, String inputUid);

	public void switchPage(Page page, String inputUid);

	public default Assembly getAssembly(String name) {
		return ModelLoader.site.getAssemblyByName(name);
	}

	public void closeCurrentContent();

	OperationInfo operationInfo();

	public boolean confirm(String title, String message);

	Command command(ObjectId target_id, Date date, String name);

	public boolean switchMnt(boolean selection);

	public void backup();

	public void updateSidebarActionBudget(String actionName);

	public void sendMessage(User sender, User receiver, String subject, String content);

	public User getCurrentConsignerInfo();

	public void displaySiteModel(ModelObject config);

	public void error(String title, String message);
}
