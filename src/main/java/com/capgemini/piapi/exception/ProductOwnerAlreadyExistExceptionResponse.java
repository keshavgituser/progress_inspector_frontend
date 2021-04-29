package com.capgemini.piapi.exception;

public class ProductOwnerAlreadyExistExceptionResponse {
	private String loginName;
	

	public ProductOwnerAlreadyExistExceptionResponse(String loginName) {
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