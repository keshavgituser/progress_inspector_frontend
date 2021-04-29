package com.capgemini.piapi.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;

/**
 * This layer will execute all the bussiness logic for the Developer operations
 * 
 * @author Harsh Joshi
 *
 */

public interface DeveloperService {

	/**
	 * This method will save developer in the Database
	 * 
	 * @param developer
	 * @return will return saved developer if entered successfully
	 */
	Developer saveDeveloper(Developer developer);

	/**
	 * This method is used to find developer on basis of Developer LoginName
	 * 
	 * @param developerLoginName
	 * @return developer for that developer identifier if identifier exist
	 */
	Developer findDeveloperByDeveloperLoginName(String developerLoginName);

	/**
	 * This method will list all developers available
	 * 
	 * @return list of developers
	 */
	List<Developer> fillAllDevelopers();

	/**
	 * This method is used to update the task status
	 * 
	 * @param taskId
	 * @param developerLoginName
	 * @param task
	 * @return updated task if all identifiers exist
	 */
	Task updateTaskStatus(String taskId, String developerLoginName, Task task);

	/**
	 * This method is used to delete developer based on developer LoginName
	 * 
	 * @param developerLoginName
	 */
	public void deleteDeveloperbyDeveloperLoginName(String developerLoginName);

	/**
	 * This method is used to add remarks by the developer for a particular task
	 * 
	 * @param taskId
	 * @param developerLoginName
	 * @param task
	 * @return Task with added remark if all Identifier exist
	 */
	Task addRemark(String taskId, String developerLoginName, Remark remark);

	/**
	 * his method is used to authorize the developer to the session
	 * 
	 * @param loginName
	 * @param pwd
	 * @param session
	 * @return
	 */
	public Developer authenticateDeveloper(String loginName, String pwd, HttpSession session);
}
