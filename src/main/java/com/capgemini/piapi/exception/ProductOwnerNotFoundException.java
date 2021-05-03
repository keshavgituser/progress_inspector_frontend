package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The ProductOwnerNotFoundException is used to handle exceptions on Product Owner
 * @author Aadesh Juvekar
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductOwnerNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Create ProductOwnerNotFoundException object without error message
	 */
	public ProductOwnerNotFoundException() {
		super();
	}
	/**
	 * Create ProductOwnerNotFoundException object with error message
	 */
	public ProductOwnerNotFoundException(String errMsg) {
		super(errMsg);
	}
}
