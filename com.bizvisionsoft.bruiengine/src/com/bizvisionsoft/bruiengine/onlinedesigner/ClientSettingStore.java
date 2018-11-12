package com.bizvisionsoft.bruiengine.onlinedesigner;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Layout;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.tools.Check;
import com.google.gson.GsonBuilder;

public class ClientSettingStore {

	public static Layout getLayout(Assembly assembly, Layout layout) {
		String name = "assembly@" + assembly.getName() + "@layout" + layout.getName();
		String setting = UserSession.current().getClientSetting(name);
		if (Check.isAssigned(setting)) {
			return new GsonBuilder().create().fromJson(setting, Layout.class);
		}
		return null;
	}

	public static void saveLayout(Assembly assembly, Layout layout) {
		String name = "assembly@" + assembly.getName() + "@layout" + layout.getName();
		UserSession.current().saveClientSetting(name, new GsonBuilder().create().toJson(layout));
	}

}
