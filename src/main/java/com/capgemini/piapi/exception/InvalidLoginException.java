package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception Class is used for Throwing Exception if you try to enter invalid login credentials
 * 
 * @author Mantu
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLoginException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create DeveloperAlreadyExistException object without error message
	 */
	public InvalidLoginException() {
		super();
	}

	/**
	 * Create DeveloperAlreadyExistException object with error message
	 */
	public InvalidLoginException(String errMsg) {
		super(errMsg);
	}

}
