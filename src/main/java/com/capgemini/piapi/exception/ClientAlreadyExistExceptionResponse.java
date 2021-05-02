package com.capgemini.piapi.exception;

public class ClientAlreadyExistExceptionResponse {
	private String loginName;
	

	public ClientAlreadyExistExceptionResponse(String loginName) {
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
