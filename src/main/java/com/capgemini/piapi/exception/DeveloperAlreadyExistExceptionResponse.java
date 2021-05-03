package com.capgemini.piapi.exception;

public class DeveloperAlreadyExistExceptionResponse {

	private String loginName;

	public DeveloperAlreadyExistExceptionResponse(String loginName) {
		super();
		this.loginName = loginName;
	}
	//Getters and Setters
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	

}
