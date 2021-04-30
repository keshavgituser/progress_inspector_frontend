package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLoginExcpetionResponse extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create DeveloperAlreadyExistException object without error message
	 */
	public InvalidLoginExcpetionResponse() {
		super();
	}

	/**
	 * Create DeveloperAlreadyExistException object with error message
	 */
	public InvalidLoginExcpetionResponse(String errMsg) {
		super(errMsg);
	}

}
