package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception Class is used for Throwing Exception if you try to Register
 * the already Registered Developer Identifier
 * 
 * @author Harsh Joshi
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeveloperIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create DeveloperIdException object without error message
	 */
	public DeveloperIdException() {
		super();
	}

	/**
	 * Create DeveloperIdException object with error message
	 */
	public DeveloperIdException(String errMsg) {
		super(errMsg);
	}

}
