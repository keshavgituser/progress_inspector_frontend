package com.capgemini.piapi.exception;

public class TeamLeaderNotFoundExceptionResponse {
	private String id;

	public TeamLeaderNotFoundExceptionResponse(String id) {
		super();
		this.id = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
