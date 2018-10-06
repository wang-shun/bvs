package com.bizvisionsoft.bruiengine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.SingletonUtil;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.bruiengine.BruiEntryPoint;
import com.bizvisionsoft.bruiengine.util.BruiToolkit;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.service.tools.Formatter;
import com.bizvisionsoft.serviceconsumer.Services;

public class UserSession {

	private BruiToolkit bruiToolkit;

	private BruiEntryPoint bruiEntryPoint;

	private List<IBruiContext> contexts = new ArrayList<>();

	private Display display;

	private ServerPushSession pushSession;

	public UserSession() {
		HttpServletRequest request = RWT.getRequest();
		remoteAddr = request.getRemoteAddr();
		remoteHost = request.getRemoteHost();
		userAgent = request.getHeader("user-agent");
		characterEncoding = request.getCharacterEncoding();
		authType = request.getAuthType();
		pathInfo = request.getPathInfo();
		remoteUser = request.getRemoteUser();
		requestSessionId = request.getRequestedSessionId();
		requestURI = request.getRequestURI();

		display = Display.getCurrent();
		pushSession = new ServerPushSession();
		pushSession.start();
		bruiToolkit = new BruiToolkit();

		if (!Brui.sessionManager.userSessions.contains(this)) {
			RWT.getUISession().addUISessionListener(l -> {
				Brui.sessionManager.userSessions.remove(this);
				dispose();
			});
			Brui.sessionManager.userSessions.add(this);
		}
	}

	public static BruiAssemblyContext newAssemblyContext() {
		return current().newAssemblyContextInstance();
	}

	public static BruiEditorContext newEditorContext() {
		return current().newEditorContextInstance();
	}

	private BruiAssemblyContext newAssemblyContextInstance() {
		BruiAssemblyContext context = new BruiAssemblyContext();
		this.contexts.add(context);
		return context;
	}

	private BruiEditorContext newEditorContextInstance() {
		BruiEditorContext context = new BruiEditorContext();
		this.contexts.add(context);
		return context;
	}

	public static UserSession current() {
		UserSession session = (UserSession) SingletonUtil.getSessionInstance(UserSession.class);
		return session;
	}

	public void dispose() {
		contexts.forEach(c -> c.dispose());
		// pushSession.stop();
	}

	public static BruiToolkit bruiToolkit() {
		return ((UserSession) SingletonUtil.getSessionInstance(UserSession.class)).bruiToolkit;
	}

	public UserSession setEntryPoint(BruiEntryPoint bruiEntryPoint) {
		this.bruiEntryPoint = bruiEntryPoint;
		return this;
	}

	public BruiEntryPoint getEntryPoint() {
		return bruiEntryPoint;
	}

	/**
	 * 强制登出用户进程
	 * 
	 * @param date
	 */
	public boolean logout(Date date) {
		if (display.isDisposed()) {
			dispose();
			return false;
		}

		final int interval;
		if (date == null) {
			interval = 0;
		} else {
			int _interval = (int) ((date.getTime() - new Date().getTime()));
			if (_interval <= 0) {
				interval = 0;
			} else {
				interval = _interval;
			}
		}
		display.asyncExec(() -> {
			if (interval < 1000) {
				Layer.message("系统将立刻停止对您的服务。<br/>如有任何疑问请咨询系统管理人员", Layer.ICON_INFO);
				RWT.getUISession().getHttpSession().setMaxInactiveInterval(1);
			} else {
				MessageDialog.openWarning(display.getActiveShell(), "通知",
						"系统将于" + Formatter.getFriendlyTimeDuration(interval) + "后停止对您的服务。<br/>如有任何疑问请咨询系统管理人员。");
				RWT.getUISession().getHttpSession().setMaxInactiveInterval(interval / 1000);
			}
		});
		return true;
	}

	public void sendMessage(final String message) {
		display.asyncExec(() -> MessageDialog.openWarning(display.getActiveShell(), "通知", message));
	}

	@ReadValue("loginTime")
	private Date loginTime;

	void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@ReadValue
	private String remoteAddr;

	@ReadValue
	private User loginUser;

	void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	void setConsignUser(User consignUser) {
		this.consignUser = consignUser;
	}

	@ReadValue
	private String remoteHost;

	@ReadValue
	private String userAgent;

	@ReadValue
	private String requestURI;

	@ReadValue
	private String characterEncoding;

	@ReadValue
	private String authType;

	@ReadValue
	private String pathInfo;

	@ReadValue
	private String remoteUser;

	@ReadValue
	private String requestSessionId;

	private User consignUser;

	@ReadValue("loginDuration")
	private String getLoginDuration() {
		if (loginTime == null) {
			return "";
		}
		long time1 = new Date().getTime();
		long time2 = loginTime.getTime();

		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}

		return Formatter.getFriendlyTimeDuration(diff);
	}

	public User getUser() {
		return loginUser;
	}

	public User getConsigner() {
		return consignUser;
	}

	public String getClientSetting(String name) {
		return Services.get(SystemService.class).getClientSetting(loginUser.getUserId(), "pms", name);
	}

	public void saveClientSetting(String name, String setting) {
		Document doc = new Document("userId",loginUser.getUserId()).append("clientId", "pms").append("name", name).append("value", setting);
		Services.get(SystemService.class).updateClientSetting(doc);
	}

}
