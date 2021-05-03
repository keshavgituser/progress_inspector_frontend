package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * The ProductOwnerAlreadyExistException is used to handle exceptions on Product Owner
 * @author Aadesh Juvekar
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductOwnerAlreadyExistException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	/**
	 * Create ProductOwnerAlreadyExistException object without error message
	 */
	public ProductOwnerAlreadyExistException() {
		super();
	}
	/**
	 * Create ProductOwnerAlreadyExistException object with error message
	 */
	public ProductOwnerAlreadyExistException(String errMsg) {
		super(errMsg);
	}
}
