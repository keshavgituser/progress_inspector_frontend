package com.capgemini.piapi.service;

import com.capgemini.piapi.domain.Task;

/**
 * This layer will execute all the business logic for the Task operations 
 * 
 * @author Bhaskar
 *
 */
public interface TaskService {


	/**
	 * This method will save task in the database
	 * 
	 * @param task
	 * @return
	 */
	public Task saveOrUpdate(Task task);
	
	/**
	 *  This method will delete task in the database
	 * 
	 * @param taskIdentifier
	 */
	public void deleteTaskByTasktIdentifier(String taskIdentifier);
	
}
