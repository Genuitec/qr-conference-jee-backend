package com.genuitec.qfconf.backend.model;

public class LoginResult {

	private boolean loggedIn = false;
	private String reason = null;
	private String session;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getSession() {
		return session;
	}

}
