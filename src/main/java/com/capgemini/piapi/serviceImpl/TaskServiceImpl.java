package com.capgemini.piapi.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.constant.DeveloperConstant;
import com.capgemini.piapi.constant.TaskConstants;
import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.exception.DeveloperIdException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.exception.TeamLeaderNotFoundException;
import com.capgemini.piapi.repository.DeveloperRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.repository.TeamLeaderRepository;
import com.capgemini.piapi.service.TaskServcie;

@Service
public class TaskServiceImpl implements TaskServcie {

	private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskConstants taskConstants;
	
	@Autowired
	private DeveloperConstant developerConstants;

	@Autowired
	private DeveloperRepository developerRepository;
	
	@Autowired
	private ProductOwnerRepository productOwnerRepository;
	
	@Autowired
	private TeamLeaderRepository teamLeaderRepository;

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
	public Task findTaskByTaskIdentifier(String taskIdentifier) {
		return taskRepository.findByTaskIdentifier(taskIdentifier);
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
	public void DeleteTask(String taskIdentifier) {
		Task task = taskRepository.findByTaskIdentifier(taskIdentifier.toUpperCase());
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
		task.setProgress(taskConstants.TASK_STATUS_INPROGRESS);
		developer.setStatus(developerConstants.DEVELOPER_ACTIVE);
		return taskRepository.save(task);
	}

}
