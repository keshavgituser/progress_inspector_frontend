package com.capgemini.piapi.exception;
/**
 * This Class is used for providing Response to the TeamLeaderAlreadyExistsException
 * @author Antu
 *
 */
public class TeamLeaderAlreadyExistsExceptionResponse {

	private String teamLeaderExists;

	public TeamLeaderAlreadyExistsExceptionResponse(String teamLeaderExists) {
		super();
		this.teamLeaderExists = teamLeaderExists;
	}

	public String getTeamLeaderExists() {
		return teamLeaderExists;
	}

	public void setTeamLeaderExists(String teamLeaderExists) {
		this.teamLeaderExists = teamLeaderExists;
	}
	
	
}
