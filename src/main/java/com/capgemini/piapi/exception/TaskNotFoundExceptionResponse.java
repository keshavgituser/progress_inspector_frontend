package com.capgemini.piapi.exception;

public class TaskNotFoundExceptionResponse {
	private String taskIdentifier;
	

	public TaskNotFoundExceptionResponse(String taskIdentifier) {
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
