package com.bizvisionsoft.bruiengine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.SingletonUtil;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.widgets.Display;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.bruiengine.BruiEntryPoint;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;
import com.bizvisionsoft.service.model.User;

public class UserSession {

	private BruiToolkit bruiToolkit;

	private BruiEntryPoint bruiEntryPoint;

	private List<IBruiContext> contexts = new ArrayList<>();

	private Display display;

	private ServerPushSession pushSession;

	private boolean pushStoped;

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

	// public UserSession setShell(Shell shell) {
	// this.shell = shell;
	// return this;
	// }
	//
	// public Shell getShell() {
	// return shell;
	// }

	public void dispose() {
		contexts.forEach(c -> c.dispose());
		if (!pushStoped) {
			pushSession.stop();
			pushStoped = true;
		}
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

	void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * ǿ�Ƶǳ��û�����
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
				Layer.message("ϵͳ������ֹͣ�����ķ���<br/>�����κ���������ѯϵͳ������Ա��", Layer.ICON_INFO);
				RWT.getUISession().getHttpSession().setMaxInactiveInterval(1);
			} else {
				MessageDialog.openWarning(display.getActiveShell(), "֪ͨ",
						"ϵͳ����" + getFriendlyTimeDuration(interval) + "��ֹͣ�����ķ���<br/>�����κ���������ѯϵͳ������Ա��");
				RWT.getUISession().getHttpSession().setMaxInactiveInterval(interval / 1000);
			}
		});
		return true;
	}

	public void sendMessage(final String message) {
		display.asyncExec(() -> MessageDialog.openWarning(display.getActiveShell(), "֪ͨ", message));
	}

	@ReadValue
	private Date loginTime;

	@ReadValue
	private String remoteAddr;

	@ReadValue
	private User loginUser;

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

	@ReadValue("loginDuration")
	private String getLoginDuration() {
		Date one = new Date();
		Date two = loginTime;
		long time1 = one.getTime();
		long time2 = two.getTime();

		long diff;
		if (time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}

		return getFriendlyTimeDuration(diff);
	}

	private String getFriendlyTimeDuration(long diff) {
		long day = diff / (24 * 60 * 60 * 1000);
		long hour = (diff / (60 * 60 * 1000) - day * 24);
		long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		String result = "";
		if (day != 0)
			result += day + "�� ";
		if (hour != 0)
			result += hour + "Сʱ ";
		if (min != 0)
			result += min + "���� ";
		if (sec != 0)
			result += sec + "��";
		return result;
	}

	public User getLoginUser() {
		return loginUser;
	}

}
