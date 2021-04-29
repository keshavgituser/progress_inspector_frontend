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
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.DeveloperIdException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.repository.DeveloperRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.TaskServcie;

@Service
public class TaskServiceImpl implements TaskServcie {

	private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskConstants taskConstants;

	@Autowired
	private DeveloperRepository developerRepository;

	@Override
	public Task createTask(Task task) {
		try {
			task.setTaskIdentifier(task.getTaskIdentifier().toUpperCase());
			task.setProgress(taskConstants.TASK_STATUS_PENDING);
			return taskRepository.save(task);
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
		return taskRepository.findAll();
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
	public Task assignDeveloper(String taskID, String DevId) {
		// get developer
		Developer developer = developerRepository.findByDevId(DevId);
		// check if available or not
		// throw exception not found
		if (developer == null) {
			throw new DeveloperIdException("Developer with Identifer " + DevId.toUpperCase() + " doesn't exist");
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
		task.setProgress(taskConstants.TASK_STATUS_INPROGRESS);
		return taskRepository.save(task);
	}

}
