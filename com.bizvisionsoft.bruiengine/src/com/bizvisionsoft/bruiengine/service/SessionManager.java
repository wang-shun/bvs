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

	public List<UserSession> userSessions = new ArrayList<UserSession>();

	public List<UserSession> getUserSessions() {
		return userSessions;
	}

	/**
	 * ��õ�ǰhttp�������û���Ϣ
	 */
	public User getSessionUserInfo() {
		HttpSession hs = RWT.getRequest().getSession();
		return (User) hs.getAttribute(ATT_USRINFO);
	}

	public String getSessionId() {
		HttpSession hs = RWT.getRequest().getSession();
		return hs.getId();
	}

	/**
	 * ���浱ǰhttp�������û���Ϣ
	 */
	public void setSessionUserInfo(User usrinfo) {
		HttpSession hs = RWT.getRequest().getSession();
		hs.setAttribute(ATT_USRINFO, usrinfo);

		final UserSession session = UserSession.current();
		session.setLoginUser(usrinfo);
		session.setLoginTime(new Date());
		
		if (!userSessions.contains(session)) {
			RWT.getUISession().addUISessionListener(l -> {
				userSessions.remove(session);
				session.dispose();
			});
			userSessions.add(session);
		}

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
		Assert.isTrue(usr != null && !usr.isEmpty(), "�û�ID����Ϊ��");
		Assert.isTrue(psw != null && !psw.isEmpty(), "���벻��Ϊ��");
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