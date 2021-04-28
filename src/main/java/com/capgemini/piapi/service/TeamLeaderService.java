package com.capgemini.piapi.service;

import javax.servlet.http.HttpSession;
import com.capgemini.piapi.domain.TeamLeader;

/**
 * This TeamLeaderService Interface is Responsible for Performing various 
 * Business Logic in the TeamLeader
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
	public TeamLeader registerTeamLeader( TeamLeader teamLeader );
	
	/**
	 * This Method is Responsible for Authenticating the TeamLeader
	 * @param userName of the TeamLeader
	 * @param pwd of the TeamLeader
	 * @return Authenticated TeamLeader
	 */
	public TeamLeader authenticateTeamLeader(String userName,String pwd,HttpSession session);

	/**
	 * This findTeamLeader method will find the team Leader
	 * @param id of the TeamLeader to find
	 * @return team Leader of corresponding id or return null
	 */
	public TeamLeader findTeamLeader(Long id);
	
	/**
	 * This deleteTeamLeader method will Delete the provided TeamLeader
	 * @param teamLeader to be deleted
	 * 
	 */
	
	public void deleteTeamLeader(Long teamLeaderId);
}
