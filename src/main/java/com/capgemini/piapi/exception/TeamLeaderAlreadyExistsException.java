package com.capgemini.piapi.exception;

/**
 * This Exception Class is used for Throwing Exception if you try to Register
 * the already Registered TeamLeader
 * 
 * @author Mantu
 *
 */

public class TeamLeaderAlreadyExistsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * creating TeamLeaderAlreadyExistsException without any Error Message
	 */
	public TeamLeaderAlreadyExistsException() {
		super();
	}

	/**
	 * Creating TeamLeaderAlreadyExistsException Object with The provided Error
	 * Message
	 * 
	 * @param msg
	 */

	public TeamLeaderAlreadyExistsException(String msg) {
		super(msg);
	}
}
