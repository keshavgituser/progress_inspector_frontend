package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception Class is used for Throwing Exception if you try to Register
 * the already Registered Developer
 * 
 * @author Harsh Joshi
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeveloperAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create DeveloperAlreadyExistException object without error message
	 */
	public DeveloperAlreadyExistException() {
		super();
	}

	/**
	 * Create DeveloperAlreadyExistException object with error message
	 */
	public DeveloperAlreadyExistException(String errMsg) {
		super(errMsg);
	}

}
