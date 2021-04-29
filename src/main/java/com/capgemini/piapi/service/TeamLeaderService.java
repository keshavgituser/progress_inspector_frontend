package com.capgemini.piapi.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;

/**
 * This TeamLeaderService Interface is Responsible for Performing various
 * Business Logic in the TeamLeader
 * 
 * @author Mantu
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

	public void deleteTeamLeaderByLoginName(String teamLeaderLoginName);

	/**
	 * This method is used to view all remarks
	 * 
	 * @return list of remarks
	 */
	public List<Remark> viewAllRemark();

}
