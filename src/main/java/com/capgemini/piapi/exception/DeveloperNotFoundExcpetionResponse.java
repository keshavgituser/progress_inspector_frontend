package com.capgemini.piapi.exception;

public class DeveloperNotFoundExcpetionResponse {
	private String loginName;

	public DeveloperNotFoundExcpetionResponse(String loginName) {
		super();
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
