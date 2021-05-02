package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Create ClientNotFoundException object without error message
	 */
	public ClientNotFoundException() {
		super();
	}
	/**
	 * Create ClientNotFoundException object with error message
	 */
	public ClientNotFoundException(String errMsg) {
		super(errMsg);
	}
}
