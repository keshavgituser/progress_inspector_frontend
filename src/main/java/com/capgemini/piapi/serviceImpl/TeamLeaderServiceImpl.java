package com.capgemini.piapi.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.constant.DeveloperConstant;
import com.capgemini.piapi.constant.TaskConstants;
import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.exception.DeveloperNotFoundException;
import com.capgemini.piapi.exception.InvalidLoginException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.exception.TeamLeaderAlreadyExistsException;
import com.capgemini.piapi.exception.TeamLeaderNotFoundException;
import com.capgemini.piapi.repository.DeveloperRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.repository.TeamLeaderRepository;
import com.capgemini.piapi.service.TeamLeaderService;

@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

	@Autowired
	private TeamLeaderRepository teamLeaderRepository;

	@Autowired
	private ProductOwnerRepository productOwnerRepository;

	@Autowired
	private DeveloperRepository developerRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public Task createTask(Task task, String productOwnerLoginName, String teamLeaderLoginName) {

		ProductOwner productOwner = productOwnerRepository.findByLoginName(productOwnerLoginName);
		if (productOwner == null) {
			// TODO Product Owner Not Found Exception
			throw new ProductOwnerNotFoundException("Product Owner Not Found");
		}
		TeamLeader teamLeader = teamLeaderRepository.findByLoginName(teamLeaderLoginName);
		if (teamLeader == null) {
			throw new TeamLeaderNotFoundException("Team Leader not found");
		}
		task.setTaskIdentifier(task.getTaskIdentifier().toUpperCase());
		task.setProgress(TaskConstants.TASK_STATUS_PENDING);
		List<Task> productOwnerTaskList = productOwner.getTask();
		productOwnerTaskList.add(task);
		productOwner.setTask(productOwnerTaskList);
		task.setProductOwner(productOwner);
		if (taskRepository.findByTaskIdentifier(task.getTaskIdentifier()) != null) {
			throw new TaskIdException("Task id " + task.getTaskIdentifier().toUpperCase() + " is already available");
		}
		Task newTask = taskRepository.save(task);
		List<Task> teamLeaderTaskList = teamLeader.getTask();
		teamLeaderTaskList.add(task);
		teamLeader.setTask(teamLeaderTaskList);
		task.setTeamLeader(teamLeader);
		teamLeaderRepository.save(teamLeader);
		productOwnerRepository.save(productOwner);
		return newTask;

	}

	@Override
	public TeamLeader registerTeamLeader(TeamLeader teamLeader) {

		if (teamLeaderRepository.findByLoginName(teamLeader.getLoginName()) != null) {
			throw new TeamLeaderAlreadyExistsException(
					"Team Leader " + teamLeader.getLoginName() + " Already Exists..");
		}

		return teamLeaderRepository.save(teamLeader);

	}

	@Override
	public TeamLeader authenticateTeamLeader(String teamLeaderLoginName, String pwd, HttpSession session) {

		TeamLeader teamLeader = teamLeaderRepository.findTeamLeaderByLoginNameAndPwd(teamLeaderLoginName, pwd);
		if (teamLeader == null) {
			throw new InvalidLoginException("Invalid Login Please Enter Details Correctly");
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
			throw new TeamLeaderNotFoundException("Team Leader with  " + teamLeaderLoginName + " not found");

		}
		return fetchedTeamLeader;
	}

	@Override
	public TeamLeader updateTeamLeader(TeamLeader teamLeader) {
		// TODO Auto-generated method stub
		if (teamLeader.getLoginName() == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		TeamLeader oldTeamLeader = teamLeaderRepository.findByLoginName(teamLeader.getLoginName());
		if (oldTeamLeader == null) {
			throw new TeamLeaderNotFoundException(
					"TeamLeader with loginName : " + teamLeader.getLoginName() + " does not exists");
		}
		teamLeader.setId(oldTeamLeader.getId());
		oldTeamLeader = teamLeader;
		return teamLeaderRepository.save(oldTeamLeader);
	}

	@Override
	public void deleteTeamLeaderByLoginName(String teamLeaderLoginName) {

		if (teamLeaderRepository.findByLoginName(teamLeaderLoginName) == null) {
			throw new TeamLeaderNotFoundException("Cannot delete TeamLeader");
		}
		teamLeaderRepository.deleteByLoginName(teamLeaderLoginName);
	}

	@Override
	public List<Remark> viewAllRemark(String taskIdentifier) {
		Task task = taskRepository.findByTaskIdentifier(taskIdentifier);
		if (task == null) {
			throw new TaskNotFoundException("Task is not found");
		}
		List<Remark> remarkList = task.getRemark();
		return remarkList;
	}

	@Override
	public Task assignDeveloper(String taskID, String developerLoginName) {
		// get developer
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		// check if available or not
		// throw exception not found
		if (developer == null) {
			throw new DeveloperNotFoundException("Developer with Identifer " + developerLoginName + " doesn't exist");
		}
		Task task = taskRepository.findByTaskIdentifier(taskID);
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
		task.setProgress(TaskConstants.TASK_STATUS_INPROGRESS);
		developer.setStatus(DeveloperConstant.DEVELOPER_ACTIVE);
		return taskRepository.save(task);
	}

	@Override
	public List<Task> viewAllTaskByTeamLeaderLoginName(String teamLeaderLoginName) {
		TeamLeader teamleader = teamLeaderRepository.findByLoginName(teamLeaderLoginName);
		List<Task> taskList = teamleader.getTask();
		return taskList;
	}

	@Override
	public Task findTaskByTaskIdentifierAndTeamLeaderLoginName(String taskIdentifier, String teamLeaderLoginName) {
		TeamLeader teamleader = teamLeaderRepository.findByLoginName(teamLeaderLoginName);
		List<Task> taskList = teamleader.getTask();
		for (Task task : taskList) {
			if (task.getTaskIdentifier().equals(taskIdentifier)) {
				return task;
			}
		}
		throw new TaskIdException("Task with id " + taskIdentifier.toUpperCase() + " is not available");
	}

	@Override
	public void deleteTask(String taskIdentifier) {
		Task task = taskRepository.findByTaskIdentifier(taskIdentifier.toUpperCase());
		if (task == null) {
			throw new TaskIdException("Task with Identifer " + taskIdentifier.toUpperCase() + " doesn't exist");
		}
		taskRepository.delete(task);
	}

	@Override
	public List<Developer> findAllDevelopers() {
		List<Developer> developerList = developerRepository.findAll();
		if (developerList.isEmpty()) {
			throw new DeveloperNotFoundException("Currently there are no developer available");
		}
		return developerList;
	}

}
