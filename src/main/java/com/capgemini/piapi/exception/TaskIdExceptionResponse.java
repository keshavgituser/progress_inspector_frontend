package com.capgemini.piapi.exception;

/**
 * This is to provide response to TaskIdException
 * @author Shubham
 *
 */
public class TaskIdExceptionResponse {

	private String taskIdentifier;

	public TaskIdExceptionResponse(String taskIdentifier) {
		super();
		this.taskIdentifier = taskIdentifier;
	}

	// Getter and Setter
	public String getTaskIdentifier() {
		return taskIdentifier;
	}

	public void setTaskIdentifier(String taskIdentifier) {
		this.taskIdentifier = taskIdentifier;
	}

}
