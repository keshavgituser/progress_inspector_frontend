package com.capgemini.piapi.exception;

public class TeamLeaderNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TeamLeaderNotFoundException() {
		super();
	}

	public TeamLeaderNotFoundException(String msg) {
		super(msg);
	}
}
