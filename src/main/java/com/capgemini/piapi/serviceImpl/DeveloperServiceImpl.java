package com.capgemini.piapi.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.constant.DeveloperConstant;
import com.capgemini.piapi.constant.TaskConstants;
import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.DeveloperAlreadyExistException;
import com.capgemini.piapi.exception.DeveloperNotFoundException;
import com.capgemini.piapi.exception.InvalidLoginException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.repository.DeveloperRepository;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.DeveloperService;

import jdk.internal.org.jline.utils.Log;

@Service
public class DeveloperServiceImpl implements DeveloperService {

	@Autowired
	private DeveloperRepository developerRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private RemarkRepository remarkRepository;

	@Autowired
	private DeveloperConstant developerConstant;

	@Autowired
	private TaskConstants taskConstants;

	@Override
	public Developer saveDeveloper(Developer developer) {
		if (developer.getName() == null || developer.getLoginName() == null || developer.getPwd() == null) {
			throw new NullPointerException("Please Enter All Values in the field");
		}
		if ((developerRepository.findByLoginName(developer.getLoginName())) != null) {
			throw new DeveloperAlreadyExistException(
					"developer with " + developer.getLoginName() + " login name is already available");
		}
		developer.setStatus(DeveloperConstant.DEVELOPER_INACTIVE);
		return developerRepository.save(developer);
	}

	@Override
	public Developer findDeveloperByDeveloperLoginName(String developerLoginName) {
		if (developerLoginName == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		if (developer == null) {
			throw new DeveloperNotFoundException("developer with " + developerLoginName + " login name not found");
		}
		return developer;
	}

	@Override
	public void deleteDeveloperbyDeveloperLoginName(String developerLoginName) {
		if (developerLoginName == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		if (developer == null) {
			throw new DeveloperNotFoundException("developer with " + developerLoginName + " login name not found");
		}
		developerRepository.delete(developer);
	}

	@Override
	public Developer updateDeveloper(Developer developer) {
		// TODO Auto-generated method stub
		if (developer.getLoginName() == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		Developer oldProductOwner = developerRepository.findByLoginName(developer.getLoginName());
		if (oldProductOwner == null) {
			throw new DeveloperNotFoundException(
					"Developer with loginName : " + developer.getLoginName() + " does not exists");
		}
		oldProductOwner = developer;
		return developerRepository.save(oldProductOwner);
	}

	@Override
	public List<Task> viewAllTaskByDeveloperLoginName(String developerLoginName) {
		if (developerLoginName == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		List<Task> taskList = developer.getTasks();
		return taskList;
	}

	@Override
	public Task findTaskByTaskIdentifierAndDevelpoerLoginName(String taskIdentifier, String developerLoginName) {
		if (developerLoginName == null || taskIdentifier == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		if (developer == null) {
			throw new DeveloperNotFoundException(
					"Developer with loginName : " + developerLoginName + " does not exists");
		}
		List<Task> taskList = developer.getTasks();
		for (Task task : taskList) {
			if (task.getTaskIdentifier().equals(taskIdentifier)) {
				return task;
			}
		}
		throw new TaskIdException("Task with id " + taskIdentifier.toUpperCase() + " is not available");
	}

	@Override
	public Task updateTaskStatus(String taskIdentifier, String developerLoginName, Task task) {
		if (developerLoginName == null || taskIdentifier == null || task == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		if (developer == null) {
			throw new DeveloperNotFoundException("developer with " + developerLoginName + " does not exist");
		}
		// Task task1 = taskRepository.findByTaskIdentifier(taskIdentifier);
		Task task1 = new Task();
		for (Task t : developer.getTasks()) {
			if (t.getTaskIdentifier().equals(taskIdentifier)) {
				task1 = t;
			}
		}
		if (task1.getTaskIdentifier() == null) {
			throw new TaskIdException("Task with Identifer" + taskIdentifier.toUpperCase() + " doesn't exist");
		}
		task1.setProgress(task.getProgress());
		return taskRepository.save(task1);
	}

	@Override
	public Task addRemark(String taskIdentifier, String developerLoginName, Remark remark) {
		if (developerLoginName == null || taskIdentifier == null || remark == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		if (developer == null) {
			throw new DeveloperNotFoundException("developer with " + developerLoginName + " does not exist");
		}
		Task task = new Task();
		for (Task t : developer.getTasks()) {
			if (t.getTaskIdentifier().equals(taskIdentifier)) {
				task = t;
			}
		}
		if (task.getTaskIdentifier() == null) {
			throw new TaskIdException("Task with Identifer" + taskIdentifier.toUpperCase() + " doesn't exist");
		}
		remark.setTask(task);
		List<Remark> remarkList = new ArrayList<>();
		if (task.getRemark() != null) {
			remarkList = task.getRemark();
		}
		remarkList.add(remark);
		task.setRemark(remarkList);
		remarkRepository.save(remark);
		return taskRepository.save(task);
	}

	@Override
	public Developer authenticateDeveloper(String developerLoginName, String pwd, HttpSession session) {
		Developer developer = null;

		if (developerLoginName == null || pwd == null) {
			throw new InvalidLoginException("Please Enter Credentials");
		}

		if ((developer = developerRepository.findByLoginName(developerLoginName)) == null) {
			throw new DeveloperNotFoundException(
					"Developer with loginName : " + developerLoginName + " does not exist");
		}

		if (developer.getPwd().equals(pwd)) {
			addDeveloperInSession(developer, session);
			return developer;
		}
		throw new InvalidLoginException("Login Failed ! Invalid Credentials");

	}

	/**
	 * This method is used to add the productOwner to the session
	 * 
	 * @param productOwner to be added to session
	 * @param session      created during login
	 */
	private void addDeveloperInSession(Developer developer, HttpSession session) {

		session.setAttribute("userType", "Developer");
		session.setAttribute("devloperId", developer.getId());
		session.setAttribute("developerLoginName", developer.getLoginName());

	}

}
