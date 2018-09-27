package com.bizvisionsoft.bruiengine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.eclipse.core.runtime.Assert;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.SettingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bizvisionsoft.bruiengine.util.Coder;
import com.bizvisionsoft.service.model.User;

public class SessionManager {

	public Logger logger = LoggerFactory.getLogger(getClass());

	public static final String ATT_USRINFO = "usrinfo";

	public static final String ATT_LOGINTIME = "logintime";

	private static final String ATT_CONSIGNINFO = "cuseinfo";

	public List<UserSession> userSessions = new ArrayList<UserSession>();

	public List<UserSession> getUserSessions() {
		return userSessions;
	}

	/**
	 * ��õ�ǰhttp�������û���Ϣ
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
	 * ���浱ǰhttp�������û���Ϣ
	 * 
	 * @throws Exception
	 */
	public void setSessionUserInfo(User user) {
		HttpSession hs = RWT.getRequest().getSession();
		Object usrinfo = hs.getAttribute(ATT_USRINFO);
		if (usrinfo != null && !usrinfo.equals(user))
			throw new RuntimeException("�����Ե�ǰ�û���¼ϵͳ��");
		else if (usrinfo != null && usrinfo.equals(user))
			return;

		hs.setAttribute(ATT_USRINFO, user);
		hs.setAttribute(ATT_LOGINTIME, new Date());

		final UserSession session = UserSession.current();
		session.setLoginUser(user);
		session.setLoginTime(new Date());

		TraceUserUtil.trace("��¼ϵͳ");

		if (logger.isDebugEnabled()) {
			String msg = "HttpSession�����û���Ϣ��";
			msg += ATT_USRINFO + "-" + hs.getAttribute(ATT_USRINFO) + ";";
			msg += ATT_LOGINTIME + "-" + hs.getAttribute(ATT_LOGINTIME) + ";";
			logger.debug(msg);
		}
	}

	public void logout() {
		clearSessionUserInfo();
		UserSession.current().getEntryPoint().home();
		TraceUserUtil.trace("�ǳ�ϵͳ");
	}

	private void clearSessionUserInfo() {
		HttpSession hs = RWT.getRequest().getSession();

		if (logger.isDebugEnabled()) {
			String msg = "HttpSession����û���Ϣ��";
			msg += ATT_USRINFO + "-" + hs.getAttribute(ATT_USRINFO) + ";";
			msg += ATT_CONSIGNINFO + "-" + hs.getAttribute(ATT_CONSIGNINFO) + ";";
			msg += ATT_LOGINTIME + "-" + hs.getAttribute(ATT_LOGINTIME) + ";";
			logger.debug(msg);
		}

		hs.removeAttribute(ATT_USRINFO);
		hs.removeAttribute(ATT_CONSIGNINFO);
		hs.removeAttribute(ATT_LOGINTIME);

		UserSession session = UserSession.current();
		session.setLoginUser(null);
		session.setConsignUser(null);
		session.setLoginTime(null);
	}

	public void consign(User user) {
		final UserSession session = UserSession.current();
		User loginUser = session.getUser();

		HttpSession hs = RWT.getRequest().getSession();
		hs.setAttribute(ATT_USRINFO, user);
		hs.setAttribute(ATT_CONSIGNINFO, loginUser);

		session.setLoginUser(user);
		session.setConsignUser(loginUser);
		UserSession.current().getEntryPoint().home();
		TraceUserUtil.trace("�����û�");

		if (logger.isDebugEnabled()) {
			String msg = "HttpSessionע������û���Ϣ��";
			msg += ATT_USRINFO + "-" + hs.getAttribute(ATT_USRINFO) + ";";
			msg += ATT_CONSIGNINFO + "-" + hs.getAttribute(ATT_CONSIGNINFO) + ";";
			logger.debug(msg);
		}
	}

	public User getConsigner() {
		return Optional.ofNullable(UserSession.current().getConsigner()).orElse(getUser());
	}

	public void updateSessionUserInfo() {
		HttpSession hs = RWT.getRequest().getSession();
		User user = (User) hs.getAttribute(ATT_USRINFO);
		User consigner = (User) hs.getAttribute(ATT_CONSIGNINFO);
		Date date = (Date) hs.getAttribute(ATT_LOGINTIME);

		UserSession session = UserSession.current();
		session.setLoginUser(user);
		session.setConsignUser(consigner);
		session.setLoginTime(date);

		if (logger.isDebugEnabled()) {
			String msg = "����UserSession�û���Ϣ��";
			msg += ATT_USRINFO + "-" + hs.getAttribute(ATT_USRINFO) + ";";
			msg += ATT_CONSIGNINFO + "-" + hs.getAttribute(ATT_CONSIGNINFO) + ";";
			msg += ATT_LOGINTIME + "-" + hs.getAttribute(ATT_LOGINTIME) + ";";
			logger.debug(msg);
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

	public void saveDefaultPageAssembly(String pageId, String assemblyId) {
		String user = getUser().getUserId();
		SettingStore store = RWT.getSettingStore();
		try {
			store.setAttribute(user + "_page_" + pageId, assemblyId);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getDefaultPageAssembly(String pageId) {
		String user = getUser().getUserId();
		SettingStore store = RWT.getSettingStore();
		return store.getAttribute(user + "_page_" + pageId);
	}

}
