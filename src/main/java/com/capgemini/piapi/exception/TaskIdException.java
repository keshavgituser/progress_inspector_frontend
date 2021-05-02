package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception Class is used for Throwing Exception if you try to access Task whose Identifier is not avaialable
 * 
 * @author Harsh Joshi
 *
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskIdException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Create TaskIdException object without error message
	 */
	public TaskIdException() {
		super();
	}

	/**
	 * Create TaskIdException object with error message
	 */
	public TaskIdException(String errMsg) {
		super(errMsg);
  }
}
