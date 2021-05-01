/**
 * 
 */
package com.capgemini.piapi.exception;

/**
 * @author kmahendr
 *
 */
public class ClientPassedNullExceptionResponse {
	
private String loginName;
	

	public ClientPassedNullExceptionResponse(String loginName) {
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
