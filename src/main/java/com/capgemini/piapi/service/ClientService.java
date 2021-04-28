package com.capgemini.piapi.service;

import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;

/**
 * This Client Service will be used for all client related operations.
 * @author Hrushikesh
 *
 */
public interface ClientService {
	/**
	 * This method will be used to return Task which is associated with the Client.
	 * @param taskIdentifier is the unique identifier for the task
	 * @return the saved Task Object.
	 */
	public Task viewTask(String taskIdentifier);
	/**
	 * This method will be used for adding the remark for specific Task.
	 * @param remark is the object of the Remark containing all the information about Remark.
	 * @param task_id is the unique identifier of the task for which remark is to be added.
	 * @return the saved Remark Object.
	 */
	public Remark addRemark(Remark remark, String task_id);
	/**
	 * This is a temporary method just to add task before we can test view task method.
	 * Should be removed at the time of INTEGRATION.
	 * @param task is the object which contains all the information of task.
	 * @return the saved Task Object. 
	 */
	public Task createNewTask(Task task); 
}
