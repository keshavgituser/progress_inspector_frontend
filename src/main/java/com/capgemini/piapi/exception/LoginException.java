package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Create LoginException object without error message
	 */
	public LoginException() {
		super();
	}
	/**
	 * Create LoginException object with error message
	 */
	public LoginException(String errMsg) {
		super(errMsg);
	}
}