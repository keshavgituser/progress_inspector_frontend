package com.capgemini.piapi.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.constant.TaskConstants;
import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.exception.DeveloperIdException;
import com.capgemini.piapi.exception.DeveloperNotFoundException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.exception.TeamLeaderAlreadyExistsException;
import com.capgemini.piapi.exception.TeamLeaderNotFoundException;
import com.capgemini.piapi.repository.DeveloperRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.repository.TeamLeaderRepository;
import com.capgemini.piapi.service.TeamLeaderService;

/**
 * This class is the Service implementation of
 * TeamLeaderService
 * @author Mantu,Shubham,Bhaskarrao
 *
 */
@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

	private static final Logger log = LoggerFactory.getLogger(TeamLeaderServiceImpl.class);

	@Autowired
	private TeamLeaderRepository teamLeaderRepository;

	@Autowired
	private RemarkRepository remarkRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskConstants taskConstants;
	
	@Autowired
	private ProductOwnerRepository productOwnerRepository;
	
	@Autowired
	private DeveloperRepository developerRepository;
	

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
	@Override
	public Task findTaskByTaskIdentifier(String taskIdentifier) {
		return taskRepository.findTaskByTaskIdentifier(taskIdentifier);
	}

	@Override
	public List<Task> findAllTasks() {
		List<Task> taskList = taskRepository.findAll();
		if(taskList.isEmpty()) {
			throw new TaskNotFoundException("Currently there are no tasks available");
		}
		return taskList;
	}
	@Override
	public Task createTask(Task task , String productOwnerLoginName,String teamleaderLoginName) {
		try {
			ProductOwner productOwner = productOwnerRepository.findByLoginName(productOwnerLoginName);
			if(productOwner == null) {
				//TODO Product Owner Not Found Exception
				throw new Exception();
			}
			TeamLeader teamLeader = teamLeaderRepository.findByLoginName(teamleaderLoginName);
			if(teamLeader == null) {
				throw new TeamLeaderNotFoundException("Team Leader not found");
			}
			task.setTaskIdentifier(task.getTaskIdentifier().toUpperCase());
			task.setProgress(taskConstants.TASK_STATUS_PENDING);
			List<Task> productOwnerTaskList = productOwner.getTask();
			productOwnerTaskList.add(task);
			productOwner.setTask(productOwnerTaskList);
			task.setProductOwner(productOwner);
			Task newTask = taskRepository.save(task);
			List<Task> teamLeaderTaskList = teamLeader.getTask();
			teamLeaderTaskList.add(task);
			teamLeader.setTask(teamLeaderTaskList);
			task.setTeamLeader(teamLeader);
			teamLeaderRepository.save(teamLeader);
			productOwnerRepository.save(productOwner);
			return newTask;
		} catch (Exception ex) {
			throw new TaskIdException("Task id " + task.getTaskIdentifier().toUpperCase() + " is already available");
		}
	}

	

	@Override
	public void DeleteTask(String taskIdentifier) {
		Task task = taskRepository.findTaskByTaskIdentifier(taskIdentifier.toUpperCase());
		if (task == null) {
			throw new TaskIdException("Task with Identifer " + taskIdentifier.toUpperCase() + " doesn't exist");
		}
		taskRepository.delete(task);
	}

	@Override
	public Task assignDeveloper(String taskID, String developerLoginName) {
		// get developer
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		// check if available or not
		// throw exception not found
		if (developer == null) {
			throw new DeveloperIdException("Developer with Identifer " + developerLoginName + " doesn't exist");
		}
		Task task = taskRepository.findTaskByTaskIdentifier(taskID);
		if (task == null) {
			throw new TaskIdException("Task with Identifer " + taskID.toUpperCase() + " doesn't exist");
		}
		// update task
		task.setDeveloper(developer);
		// Set this task in developer
		List<Task> taskList = new ArrayList<>();
		taskList.add(task);
		developer.setTasks(taskList);
		developerRepository.save(developer);
		task.setProgress(taskConstants.TASK_STATUS_INPROGRESS);
		return taskRepository.save(task);
	}


}
