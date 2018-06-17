package com.bizvisionsoft.bruiengine.app.sysman;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.bizvisionsoft.annotations.md.service.DataSet;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.service.SessionManager;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.model.User;

public class LoginUserDS {

	@DataSet(DataSet.LIST)
	private List<ConnectionInfo> listLicenseItem() {
		List<UserSession> us = Brui.sessionManager.getUserSessions();
		final ArrayList<ConnectionInfo> result = new ArrayList<ConnectionInfo>();
		us.forEach(u -> {
			HttpSession hs = u.getHttpSession();
			User userInfo = (User) hs.getAttribute(SessionManager.ATT_USRINFO);
			ConnectionInfo ci = new ConnectionInfo();
			ci.setLoginTime(u.getLoginTime());
			ci.setUserId(userInfo.getUserId() );
			ci.setUserName(userInfo.getName());
			ci.setSessionId(hs.getId());
			ci.setRemoteIP(u.getRemoteAddr());
			result.add(ci);
		});
		return result;
	}

}
