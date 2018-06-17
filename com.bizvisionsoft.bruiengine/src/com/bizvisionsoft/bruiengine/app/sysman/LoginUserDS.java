package com.bizvisionsoft.bruiengine.app.sysman;

import java.util.List;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.UserSession;

public class LoginUserDS {

	@DataSet(DataSet.LIST)
	private List<UserSession> listLicenseItem() {
		return Brui.sessionManager.getUserSessions();
	}

}
