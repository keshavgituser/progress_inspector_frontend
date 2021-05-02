package com.capgemini.piapi.exception;

/**
 * This Exception Class is used for Throwing Exception if you try to access Team leader
 * which is not registered
 * 
 * @author Mantu
 *
 */

public class TeamLeaderNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This method is used to catch Team leader not found exception without error message
	 */
	public TeamLeaderNotFoundException() {
		super();
	}
	/**
	 *  This method is used to catch Team leader not found exception with error message
	 * @param msg
	 */
	public TeamLeaderNotFoundException(String msg) {
		super(msg);
	}
}
