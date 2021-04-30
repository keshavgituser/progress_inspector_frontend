package com.capgemini.piapi.service;

import java.util.List;

import com.capgemini.piapi.domain.Task;

/**
 * This layer will execute all the bussiness logic for the Progress Inspector
 * @author Vatsal Shah
 *
 */
public interface TaskServcie {

	/**
	 * This method is used to create task into the database
	 * @param task
	 * @return created task
	 */
	public Task createTask(Task task ,String productOwnerLoginName, String teamleaderLoginName);
	/**
	 * This method is used to find task by task identifier
	 * @param taskIdentifier
	 * @return task if identifier exist
	 */
	public Task findTaskByTaskIdentifier(String taskIdentifier);
	/**
	 * This method will list all tasks available
	 * @return list of task 
	 */
	public List<Task> findAllTasks();
	/**
	 * This method will delete task on basis of the task identifier
	 * @param taskIdentifier
	 */
	public void DeleteTask(String taskIdentifier);
	/**
	 * This method will assign task to the developer
	 * @param taskID
	 * @param DevId
	 * @return developer with assigned task
	 */
	public Task assignDeveloper(String taskID , String DevId);
	
}
