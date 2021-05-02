package com.capgemini.piapi.exception;

public class DeveloperAlreadyExistExceptionResponse {

	private String devIdentifier;

	public DeveloperAlreadyExistExceptionResponse(String devIdentifier) {
		super();
		this.devIdentifier = devIdentifier;
	}

	// Getter and Setter
	public String getDevIdentifier() {
		return devIdentifier;
	}

	public void setDevIdentifier(String devIdentifier) {
		this.devIdentifier = devIdentifier;
	}

}
