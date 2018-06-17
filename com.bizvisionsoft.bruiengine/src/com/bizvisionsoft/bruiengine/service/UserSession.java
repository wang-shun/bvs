package com.bizvisionsoft.bruiengine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.eclipse.jface.window.IShellProvider;
import org.eclipse.rap.rwt.SingletonUtil;
import org.eclipse.swt.widgets.Shell;

import com.bizvisionsoft.bruiengine.BruiEntryPoint;
import com.bizvisionsoft.bruiengine.ui.BruiToolkit;

public class UserSession implements IShellProvider {

	private Shell shell;

	private BruiToolkit bruiToolkit;

	private BruiEntryPoint bruiEntryPoint;

	private HttpSession httpSession;

	private Date loginTime;

	private String remoteAddr;

	private List<IBruiContext> contexts;

	public UserSession() {
		contexts = new ArrayList<>();
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

	public UserSession setShell(Shell shell) {
		this.shell = shell;
		return this;
	}

	public Shell getShell() {
		return shell;
	}

	public void dispose() {

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

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public UserSession setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
		return this;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

}
