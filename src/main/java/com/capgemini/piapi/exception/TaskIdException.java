package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create DeveloperIdException object without error message
	 */
	public TaskIdException() {
		super();
	}

	/**
	 * Create DeveloperIdException object with error message
	 */
	public TaskIdException(String errMsg) {
		super(errMsg);
	}

}
