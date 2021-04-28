package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductOwnerNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Create ProjectIdException object without error message
	 */
	public ProductOwnerNotFoundException() {
		super();
	}
	/**
	 * Create ProjectIdException object with error message
	 */
	public ProductOwnerNotFoundException(String errMsg) {
		super(errMsg);
	}
}
