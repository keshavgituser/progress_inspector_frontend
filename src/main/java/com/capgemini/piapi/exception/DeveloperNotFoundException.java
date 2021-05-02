package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception Class is used for Throwing Exception if Developer
 * is not registered
 * 
 * @author Harsh Joshi
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeveloperNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Create DeveloperNotFoundException object without error message
	 */
	public DeveloperNotFoundException() {
		super();
	}
	/**
	 * Create DeveloperNotFoundException object with error message
	 */
	public DeveloperNotFoundException(String errMsg) {
		super(errMsg);
	}
}
