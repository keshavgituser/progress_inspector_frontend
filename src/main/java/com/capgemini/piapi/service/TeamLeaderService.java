package com.capgemini.piapi.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;

/**
 * This TeamLeaderService Interface is Responsible for Performing various
 * Business Logic in the TeamLeader
 * 
 * @author Mantu
 * @author Shubham Sahu
 * @author Bhaskarrao Puppala
 *
 */

public interface TeamLeaderService {
	/**
	 * This Method is Responsible for Registering the new TeamLeader in the Database
	 * 
	 * @param teamLeader to be register
	 * @return saved Saved TeamLeader from database
	 */
	public TeamLeader registerTeamLeader(TeamLeader teamLeader);

	/**
	 * This Method is Responsible for Authenticating the TeamLeader
	 * 
	 * @param userName of the TeamLeader
	 * @param pwd      of the TeamLeader
	 * @return Authenticated TeamLeader
	 */
	public TeamLeader authenticateTeamLeader(String userName, String pwd, HttpSession session);

	/**
	 * This findTeamLeader method will find the team Leader on basis of loginName
	 * 
	 * @param loginName of the TeamLeader to find
	 * @return team Leader of corresponding loginName or return team leader not
	 *         found
	 */
	public TeamLeader findTeamLeaderByLoginName(String teamLeaderLoginName);

	/**
	 * This deleteTeamLeader method will Delete the provided TeamLeader login Name
	 * 
	 * @param teamLeader to be deleted on basis of login Name
	 * 
	 */

	public void deleteTeamLeaderByLoginName(String teamLeaderLoginName);//

	/**
	 * This method is used to list all remarks based on the task identifier
	 * 
	 * @param taskIdentifier
	 * @return List of Remark if available
	 */
	public List<Remark> viewAllRemark(String taskIdentifier);

	/**
	 * This method is used to create task on basis of product owner and teamleader
	 * 
	 * @param task
	 * @param productOwnerLoginName
	 * @param teamleaderLoginName
	 * @return Created task if executed successfully
	 */
	public Task createTask(Task task, String productOwnerLoginName, String teamleaderLoginName);

	/**
	 * This method will assign task to the developer
	 * 
	 * @param taskID
	 * @param DevId
	 * @return developer with assigned task
	 */
	public Task assignDeveloper(String taskID, String DevId);

	/**
	 * This method is used to view all task to a specific team leader by login name
	 * 
	 * @param LoginName
	 * @return List of Task if available
	 */
	public List<Task> viewAllTaskByTeamLeaderLoginName(String LoginName);

	/**
	 * This method is used to find task by task identifier and teamleader login name
	 * 
	 * @param taskIdentifier
	 * @param LoginName
	 * @return Task is executed properly
	 */
	public Task findTaskByTaskIdentifierAndTeamLeaderLoginName(String taskIdentifier, String LoginName);

	/**
	 * This method will delete task on basis of the task identifier
	 * 
	 * @param taskIdentifier
	 */
	public void deleteTask(String taskIdentifier);

	/**
	 * This method is used to list all developers based on team leader login name
	 * 
	 * @param LoginName
	 * @return
	 */
	List<Developer> findAllDevelopers();

	/**
	 * This method is used to update team leader if team leader exist
	 * 
	 * @param teamLeader
	 * @return updated TeamLeader
	 */
	TeamLeader updateTeamLeader(TeamLeader teamLeader);
}
