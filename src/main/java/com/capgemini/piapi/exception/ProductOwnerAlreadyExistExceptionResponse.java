package com.capgemini.piapi.exception;
/**
 * The ProductOwnerAlreadyExistResponseException is used to handle exceptions on Product Owner
 * @author Aadesh Juvekar
 *
 */
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
