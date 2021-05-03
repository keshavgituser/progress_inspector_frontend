package com.capgemini.piapi.exception;
/**
 * The ProductOwnerNotFoundResponseException is used to handle exceptions on Product Owner
 * @author Aadesh Juvekar
 *
 */
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
