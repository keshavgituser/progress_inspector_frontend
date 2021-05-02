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
	 * This method will update the developer entity if developer exist
	 * @param developer
	 * @return Updated Developer
	 */
	Developer updateDeveloper(Developer developer);
	
	/**
	 * This method is used to find developer on basis of Developer LoginName
	 * 
	 * @param developerLoginName
	 * @return developer for that developer identifier if identifier exist
	 */
	Developer findDeveloperByDeveloperLoginName(String developerLoginName);

	/**
	 * This method is used to view all task to a specific team leader by login name
	 * 
	 * @param LoginName
	 * @return list of task if available
	 */
	public List<Task> viewAllTaskByDeveloperLoginName(String LoginName);
}
