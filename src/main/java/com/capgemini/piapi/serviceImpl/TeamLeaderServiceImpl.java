package com.capgemini.piapi.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.exception.DeveloperNotFoundException;
import com.capgemini.piapi.exception.TeamLeaderAlreadyExistsException;
import com.capgemini.piapi.exception.TeamLeaderNotFoundException;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TeamLeaderRepository;
import com.capgemini.piapi.service.TeamLeaderService;

@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

	private static final Logger log = LoggerFactory.getLogger(TeamLeaderServiceImpl.class);

	@Autowired
	private TeamLeaderRepository teamLeaderRepository;

	@Autowired
	private RemarkRepository remarkRepository;

	@Override
	public TeamLeader registerTeamLeader(TeamLeader teamLeader) {
		try {

			return teamLeaderRepository.save(teamLeader);
		} catch (Exception ex) {
			throw new TeamLeaderAlreadyExistsException(
					"Team Leader " + teamLeader.getLoginName() + " Already Exists..");
		}
	}

	@Override
	public TeamLeader authenticateTeamLeader(String loginName, String pwd, HttpSession session) {
		
		TeamLeader teamLeader = teamLeaderRepository.findTeamLeaderByLoginNameAndPwd(loginName, pwd);
		if (teamLeader == null) {
			throw new TeamLeaderNotFoundException("Invalid Login Please Enter Details Correctly");
		}
		addTeamLeaderInSession(teamLeader, session);
		return teamLeader;
	}

	/**
	 * This method is used to add the productOwner to the session
	 * 
	 * @param productOwner to be added to session
	 * @param session      created during login
	 */
	private void addTeamLeaderInSession(TeamLeader teamLeader, HttpSession session) {

		session.setAttribute("userType", "TeamLeader");
		session.setAttribute("teamLeaderId", teamLeader.getId());
		session.setAttribute("teamLeaderLoginName", teamLeader.getLoginName());

	}

	@Override
	public TeamLeader findTeamLeaderByLoginName(String teamLeaderLoginName) {
		TeamLeader fetchedTeamLeader = teamLeaderRepository.findByLoginName(teamLeaderLoginName);
		if (fetchedTeamLeader == null) {
			throw new TeamLeaderNotFoundException("Team Leader with  " + teamLeaderLoginName + "not found");
		}
		return fetchedTeamLeader;
	}

	@Override
	public void deleteTeamLeaderByLoginName(String teamLeaderLoginName) {
		teamLeaderRepository.deleteByLoginName(teamLeaderLoginName);
	}

	@Override
	public List<Remark> viewAllRemark() {
		return remarkRepository.findAll();
	}

}
