package com.capgemini.piapi.exception;

public class ClientNotFoundExceptionResponse {
	private String loginName;
	

	public ClientNotFoundExceptionResponse(String loginName) {
		super();
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setTaskIdentifier(String loginName) {
		this.loginName = loginName;
	}
	
}