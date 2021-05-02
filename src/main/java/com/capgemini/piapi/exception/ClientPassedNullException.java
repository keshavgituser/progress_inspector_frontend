/**
 * 
 */
package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Keshav
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientPassedNullException extends RuntimeException {

	/**
	 * Create ClientPassedNullException object without error message
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ClientPassedNullException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param errMsg
	 */
	public ClientPassedNullException(String errMsg) {
		super(errMsg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ClientPassedNullException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
	

	
}
