package com.capgemini.piapi.serviceImpl;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.exception.TeamLeaderAlreadyExistsException;
import com.capgemini.piapi.exception.TeamLeaderNotFoundException;
import com.capgemini.piapi.repository.TeamLeaderRepository;
import com.capgemini.piapi.service.TeamLeaderService;


@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

	Logger logger =LoggerFactory.getLogger(TeamLeaderServiceImpl.class);
	@Autowired
	private TeamLeaderRepository teamLeaderRepository;
	
	@Override
	public TeamLeader registerTeamLeader(TeamLeader teamLeader) {
		// TODO Auto-generated method stub
		try {
		
		 return teamLeaderRepository.save(teamLeader);
		}
		catch(Exception ex) {
			throw new TeamLeaderAlreadyExistsException("Team Leader "+ teamLeader.getLoginName()+" Already Exists..");
		}
		
		
	}

	@Override
	public TeamLeader authenticateTeamLeader(String userName, String pwd,HttpSession session) {
		TeamLeader teamLeader = teamLeaderRepository.findTeamLeaderByLoginNameAndPwd(userName, pwd);
		if(teamLeader != null) {
			addTeamLeaderSession(teamLeader,session);
			return teamLeader;
		}
		return null;
		
		// TODO Auto-generated method stub
		
	}

	private void addTeamLeaderSession(TeamLeader teamLeader, HttpSession session) {
		session.setAttribute("userType", "TeamLeader");
		session.setAttribute("TeamLeaderId", teamLeader.getId());
		session.setAttribute("TeamLeaderName", teamLeader.getName());
		
	}

	@Override
	public TeamLeader findTeamLeader(Long id) {
		TeamLeader fetchedTeamLeader= teamLeaderRepository.findTeamLeaderById(id);
		if(fetchedTeamLeader == null) {
			throw new TeamLeaderNotFoundException("Team Leader with Id " + id +"not found");
		}
		return fetchedTeamLeader;
	}

	@Override
	public void deleteTeamLeader(Long teamLeaderId) {
		// TODO Auto-generated method stub
		teamLeaderRepository.deleteById(teamLeaderId);
	}

}
