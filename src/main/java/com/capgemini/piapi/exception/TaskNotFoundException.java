package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception Class is used for Throwing Exception if you try to access Task which are not created
 * 
 * @author Harsh Joshi
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * This is used to catch exception if task is not found without error message
	 */
	public TaskNotFoundException() {
		super();
	}

	/**
	 * This is used to catch exception if task is not found with error message
	 * 
	 * @param errMsg
	 */
	public TaskNotFoundException(String errMsg) {
		super(errMsg);
	}
}
