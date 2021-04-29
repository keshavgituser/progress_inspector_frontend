package com.capgemini.piapi.exception;

public class DeveloperIdExceptionResponse {

	private String devIdentifier;

	public DeveloperIdExceptionResponse(String devIdentifier) {
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
