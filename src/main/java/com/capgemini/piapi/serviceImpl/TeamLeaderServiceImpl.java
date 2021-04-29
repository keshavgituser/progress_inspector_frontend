package com.capgemini.piapi.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.exception.TeamLeaderAlreadyExistsException;
import com.capgemini.piapi.exception.TeamLeaderNotFoundException;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.repository.TeamLeaderRepository;
import com.capgemini.piapi.service.TeamLeaderService;


@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

	Logger logger =LoggerFactory.getLogger(TeamLeaderServiceImpl.class);
	@Autowired
	private TeamLeaderRepository teamLeaderRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
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
		//If teamLeader is  Valid then add TeamLeader in the Session and return logged TeamLeader
		//otherwise return null
		if(teamLeader != null) {
			addTeamLeaderSession(teamLeader,session);
			return teamLeader;
		}
		return null;
		
		
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


	/**
	 * This is service implementation of find by taskIdentifier
	 * it is to show particular task based on taskIdentifier
	 */
	@Override
	public Task findByTaskIdentifier(String taskIdentifier) {
		// TODO Auto-generated method stub
		
		Task task = taskRepository.findByTaskIdentifier(taskIdentifier.toUpperCase());
		logger.info("--TaskServiceImpl:TASK--"+task);
		if(task==null) {
			throw new TaskIdException("Task id "+taskIdentifier.toUpperCase()+" is not available");
		}
	
		return task;
	}
	
	/**
	 * This is service implementation of findAll
	 * it will show all the tasks
	 */

	@Override
	public List<Task> findAllTasks() {
		// TODO Auto-generated method stub
		List<Task> taskList=taskRepository.findAll();
		if(taskList.isEmpty())
		{
			throw new TaskNotFoundException("Currently Tasks are not available...");
		}
		return taskList;
	}
}
