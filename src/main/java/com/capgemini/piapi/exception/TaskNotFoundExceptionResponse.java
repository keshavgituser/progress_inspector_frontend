package com.capgemini.piapi.exception;

public class TaskNotFoundExceptionResponse {
	private String taskNotFound;

	public TaskNotFoundExceptionResponse(String taskNotFound) {
		super();
		this.taskNotFound = taskNotFound;
	}

	public String getTaskNotFound() {
		return taskNotFound;
	}

	public void setTaskNotFound(String taskNotFound) {
		this.taskNotFound = taskNotFound;
	}
}
