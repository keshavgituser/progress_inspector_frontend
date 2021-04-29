package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientAlreadyExistException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Create ProjectIdException object without error message
	 */
	public ClientAlreadyExistException() {
		super();
	}
	/**
	 * Create ProjectIdException object with error message
	 */
	public ClientAlreadyExistException(String errMsg) {
		super(errMsg);
	}
}