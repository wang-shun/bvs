package com.bizvisionsoft.bruiengine.session;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.eclipse.core.runtime.Assert;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.SettingStore;

import com.bizvisionsoft.bruiengine.util.Coder;
import com.bizvisionsoft.service.model.User;

public class SessionManager {

	public static final String ATT_USRINFO = "usrinfo";

//	public String requireSessionUserId(Shell shell) throws Exception {
//		UserInfo userInfo = requireSessionUserInfo(shell);
//		return userInfo.getUserId();
//	}
//
//	/**
//	 * 强制获得用户信息
//	 * 
//	 * @param page
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	public UserInfo requireSessionUserInfo(Shell shell) throws Exception {
//		UserInfo userInfo = getSessionUserInfo();
//		if (userInfo != null) {
//			return userInfo;
//		}
//
//		String[] usrNpsw = loadClientLogin();
//		if (usrNpsw != null) {
//			try {
//				userInfo = login(usrNpsw[0], usrNpsw[1]);
//			} catch (Exception e) {
//			}
//		}
//		if (userInfo != null) {
//			return userInfo;
//		}
//
//		Login login = new Login(shell, "请登录");
//		login.open();
//		if (login.userInfo != null) {
//			try {
//				saveClientLogin(login.getUserId(), login.getPassword());
//			} catch (Exception e) {
//			}
//			userInfo = login.userInfo;
//			setSessionUserInfo(userInfo);
//		}
//		if (userInfo != null) {
//			return userInfo;
//		} else {
//			throw new Exception("取消验证");
//		}
//	}

	/**
	 * 获得当前http进程中用户信息
	 */
	public User getSessionUserInfo() {
		HttpSession hs = RWT.getRequest().getSession();
		return (User) hs.getAttribute(ATT_USRINFO);
	}

	/**
	 * 保存当前http进程中用户信息
	 */
	public void setSessionUserInfo(User usrinfo) {
		HttpSession hs = RWT.getRequest().getSession();

//		UserInfo oldValue = (UserInfo) hs.getAttribute(ATT_USRINFO);
		hs.setAttribute(ATT_USRINFO, usrinfo);

//		ListenerList listeners = (ListenerList) hs.getAttribute("listeners");
//		if (listeners != null) {
//			Object[] lis = listeners.getListeners();
//			for (int i = 0; i < lis.length; i++) {
//				((ISessionListener) lis[i]).attributeChanged(ATT_USRINFO,
//						oldValue, usrinfo);
//			}
//		}

		if (usrinfo == null) {
//			Apps.logInfo("Logout! " + oldValue.getUserId() + "\nRemoteHost:"
//					+ RWT.getRequest().getRemoteHost() + "\nUser-Agent:"
//					+ RWT.getRequest().getHeader("User-Agent"));
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
		Assert.isTrue(usr != null && !usr.isEmpty(), "用户ID不可为空");
		Assert.isTrue(psw != null && !psw.isEmpty(), "密码不可为空");
		psw = Coder.encryptBASE64(psw.getBytes());
		SettingStore store = RWT.getSettingStore();
		store.setAttribute("usr", usr);
		store.setAttribute("psw", psw);
	}

	public SessionManager start() {
		// TODO Auto-generated method stub
		return this;
	}
	
	public SessionManager stop() {
		
		return this;
	}

//	public UserInfo login(String userId, String password) throws Exception {
//		return login(userId, password, Apps.isAdEnable());
//	}
//
//	private UserInfo login(String userId, String password, boolean adVerify)
//			throws Exception {
//		UserInfo userinfo = null;
//		try {
//			String message = Utils.checkUserId(userId);
//			if (message == null) {
//				BasicDBObject user = UserPersistUtil.getUser(userId);
//				if (user != null) {
//					if (Boolean.TRUE.equals(user.get("activated"))) {
//						if (adVerify) {
//							if (!Utils.checkLdapUser(userId, password)) {
//								message = "输入的帐号和密码不正确，请重新输入。";
//							} else {
//								userinfo = new UserInfo(user);
//							}
//						} else {
//							if (!password.equals(user.get("password"))) {
//								message = "输入的帐号和密码不正确，请重新输入。";
//							} else {
//								userinfo = new UserInfo(user);
//							}
//						}
//					} else {
//						message = "您的帐号未激活。";
//					}
//				} else {
//					message = "输入的帐号和密码不正确，请重新输入。";
//				}
//			}
//
//			Utils.log(userId, "login", message);
//
//			if (message != null) {
//				throw new Exception(message);
//			}
//			Apps.logInfo("Login Success! " + userId + "\nRemoteHost:"
//					+ RWT.getRequest().getRemoteHost() + "\nUser-Agent:"
//					+ RWT.getRequest().getHeader("User-Agent"));
//		} catch (Exception e) {
//			Apps.logInfo("Login Failed! " + userId + "\nRemoteHost:"
//					+ RWT.getRequest().getRemoteHost() + "\nUser-Agent:"
//					+ RWT.getRequest().getHeader("User-Agent") + "\n"
//					+ e.getMessage());
//			throw e;
//		}
//		return userinfo;
//	}

//	public Object getInput() {
//		final UserSession userSession = UserSession.get();
//		RWT.getUISession().addUISessionListener(new UISessionListener() {
//
//			@Override
//			public void beforeDestroy(UISessionEvent event) {
//				userSession.dispose();
//			}
//		});
//
//		String id = RWT.getRequest().getParameter("id");
//		if (id == null) {
//			return null;
//		} else {
//			Object input = RWT.getRequest().getSession().getAttribute(id);
//			return input;
//		}
//	}
//
//	public void setInput(String id, Object input) {
//		HttpSession hs = RWT.getRequest().getSession();
//		hs.setAttribute(id, input);
//	}

//	public void addSessionListener(ISessionListener listener) {
//		HttpSession hs = RWT.getRequest().getSession();
//		ListenerList listeners = (ListenerList) hs.getAttribute("listeners");
//		if (listeners == null) {
//			listeners = new ListenerList();
//			hs.setAttribute("listeners", listeners);
//		}
//		listeners.add(listener);
//	}
//
//	public void removeSessionListener(ISessionListener listener) {
//		HttpSession hs = RWT.getRequest().getSession();
//		ListenerList listeners = (ListenerList) hs.getAttribute("listeners");
//		if (listeners != null) {
//			listeners.remove(listener);
//		}
//	}
}
