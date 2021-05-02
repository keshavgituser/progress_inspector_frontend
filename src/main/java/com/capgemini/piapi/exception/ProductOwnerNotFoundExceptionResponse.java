package com.capgemini.piapi.exception;

public class ProductOwnerNotFoundExceptionResponse {

private String loginName;
	

	public ProductOwnerNotFoundExceptionResponse(String loginName) {
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
