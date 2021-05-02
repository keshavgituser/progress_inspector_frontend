package com.capgemini.piapi.exception;

public class LoginExceptionResponse {
	private String error;
	

	public LoginExceptionResponse(String error) {
		super();
		this.error = error;
	}

	public String geterror() {
		return error;
	}

	public void seterror(String error) {
		this.error = error;
	}
	
}
