package com.bizvisionsoft.bruiengine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.eclipse.core.runtime.Assert;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.SettingStore;

import com.bizvisionsoft.bruiengine.util.Coder;
import com.bizvisionsoft.service.model.User;

public class SessionManager {

	public static final String ATT_USRINFO = "usrinfo";

	public static final String ATT_LOGINTIME = "logintime";

	public List<UserSession> userSessions = new ArrayList<UserSession>();

	public List<UserSession> getUserSessions() {
		return userSessions;
	}

	/**
	 * 获得当前http进程中用户信息
	 */
	public User getUser() {
		HttpSession hs = RWT.getRequest().getSession();
		return (User) hs.getAttribute(ATT_USRINFO);
	}

	public String getSessionId() {
		HttpSession hs = RWT.getRequest().getSession();
		return hs.getId();
	}

	/**
	 * 保存当前http进程中用户信息
	 * @throws Exception 
	 */
	public void setSessionUserInfo(User user) throws Exception {
		HttpSession hs = RWT.getRequest().getSession();
		Object usrinfo = hs.getAttribute(ATT_USRINFO);
		if (usrinfo != null) {
			if (!user.equals(usrinfo)) {
				throw new Exception();
			}
		} else {
			hs.setAttribute(ATT_USRINFO, user);
			hs.setAttribute(ATT_LOGINTIME, new Date());

			final UserSession session = UserSession.current();
			session.setLoginUser(user);
			session.setLoginTime(new Date());
		}
	}

	public void updateSessionUserInfo() {
		HttpSession hs = RWT.getRequest().getSession();
		User user = (User) hs.getAttribute(ATT_USRINFO);
		Date date = (Date) hs.getAttribute(ATT_LOGINTIME);

		UserSession session = UserSession.current();
		session.setLoginUser(user);
		session.setLoginTime(date);
	}

	public String[] loadClientLogin() {
		SettingStore store = RWT.getSettingStore();
		String usr = store.getAttribute("usr"); //$NON-NLS-1$
		String psw = store.getAttribute("psw"); //$NON-NLS-1$
		if (usr == null || usr.isEmpty() || psw == null || psw.isEmpty()) {
			return null;
		}
		try {
			byte[] bytes = Coder.decryptBASE64(psw);
			psw = new String(bytes);
			return new String[] { usr, psw };
		} catch (Exception e) {
			return null;
		}
	}

	public void cleanClientLogin() {
		SettingStore store = RWT.getSettingStore();
		try {
			store.removeAttribute("usr");
			store.removeAttribute("psw");
		} catch (IOException e) {
		}
	}

	public void saveClientLogin(String usr, String psw) throws Exception {
		Assert.isTrue(usr != null && !usr.isEmpty(), "用户ID不可为空");
		Assert.isTrue(psw != null && !psw.isEmpty(), "密码不可为空");
		psw = Coder.encryptBASE64(psw.getBytes());
		SettingStore store = RWT.getSettingStore();
		store.setAttribute("usr", usr);
		store.setAttribute("psw", psw);
	}

	public SessionManager start() {
		return this;
	}

	public SessionManager stop() {

		return this;
	}

}
