package com.bizvisionsoft.bruiengine.app.sysman;

import java.util.Date;

import com.bizvisionsoft.annotations.md.service.ReadValue;
import com.bizvisionsoft.annotations.md.service.WriteValue;

public class ConnectionInfo {
	
	@ReadValue
	@WriteValue
	private String userId;
	
	@ReadValue
	@WriteValue
	private String userName;
	
	@ReadValue
	@WriteValue
	private Date loginTime;
	
	@ReadValue
	@WriteValue
	private String remoteIP;
	
	@ReadValue
	@WriteValue
	private String sessionId;

	public String getUserId() {
		return userId;
	}

	public ConnectionInfo setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public ConnectionInfo setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public ConnectionInfo setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
		return this;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public ConnectionInfo setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
		return this;
	}

	public String getSessionId() {
		return sessionId;
	}

	public ConnectionInfo setSessionId(String sessionId) {
		this.sessionId = sessionId;
		return this;
	}
	
	

}
