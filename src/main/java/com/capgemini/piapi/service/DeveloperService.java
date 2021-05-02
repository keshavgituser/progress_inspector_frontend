package com.capgemini.piapi.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;

/**
 * This layer will execute all the bussiness logic for the Developer operations
 * 
 * @author Vatsal Shah
 *
 */

public interface DeveloperService {

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
	 * 
	 * @param developerLoginName
	 * @return
	 */
	Developer findDeveloperByDeveloperLoginName(String developerLoginName);

	
}
