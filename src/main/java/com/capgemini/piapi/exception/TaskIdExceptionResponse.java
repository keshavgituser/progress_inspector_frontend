package com.capgemini.piapi.exception;

public class TaskIdExceptionResponse {

	private String taskIdentifier;

	public TaskIdExceptionResponse(String taskIdentifier) {
		super();
		this.taskIdentifier = taskIdentifier;
	}

	public String getTaskIdentifier() {
		return taskIdentifier;
	}
	public void setTaskIdentifier(String taskIdentifier) {
		this.taskIdentifier = taskIdentifier;
	}
}
