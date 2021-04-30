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
		try {
			developer.setStatus(DeveloperConstant.DEVELOPER_INACTIVE);
			// new developer is created set task no task assigned
			if (developer.getTasks() == null) {
				List<Task> task = new ArrayList<>();
				developer.setTasks(task);
			}
			return developerRepository.save(developer);
		} catch (Exception ex) {
			throw new DeveloperAlreadyExistException(
					"developer with " + developer.getLoginName() + " login name is already available");
		}
	}

	@Override
	public Developer findDeveloperByDeveloperLoginName(String developerLoginName) {
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		if (developer == null) {
			throw new DeveloperNotFoundException("developer with " + developerLoginName + " login name not found");
		}
		return developer;
	}

	@Override
	public void deleteDeveloperbyDeveloperLoginName(String developerLoginName) {
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		if (developer == null) {
			throw new DeveloperNotFoundException("developer with " + developerLoginName + " login name not found");
		}
		developerRepository.delete(developer);
	}

	@Override
	public Developer updateProductOwner(Developer developer) {
		// TODO Auto-generated method stub
		if (developer.getLoginName() == null) {
			throw new DeveloperNotFoundException("Please Fill the Required Fields");
		}
		Developer oldProductOwner = developerRepository.findByLoginName(developer.getLoginName());
		if (oldProductOwner == null) {
			throw new DeveloperNotFoundException(
					"Product Owner with loginName : " + developer.getLoginName() + " does not exists");
		}
		oldProductOwner = developer;
		return developerRepository.save(oldProductOwner);
	}

	@Override
	public Task updateTaskStatus(String taskId, String developerLoginName, Task task) {
		// get developer
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		// check if available or not
		// throw exception not found
		if (developer == null) {
			throw new DeveloperNotFoundException(
					"developer with " + developerLoginName + " login name is already available");
		}
		Task task1 = taskRepository.findByTaskIdentifier(taskId);
		if (task1 == null) {
			throw new TaskIdException("Task with Identifer" + taskId.toUpperCase() + " doesn't exist");
		}
		task1.setProgress(task.getProgress());
		return taskRepository.save(task1);
	}

	@Override
	public Task addRemark(String taskId, String developerLoginName, Remark remark) {
		// get developer
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		// check if available or not
		// throw exception not found
		if (developer == null) {
			throw new DeveloperNotFoundException(
					"developer with " + developerLoginName + " login name is already available");
		}
		Task task = taskRepository.findByTaskIdentifier(taskId);
		if (task == null) {
			throw new TaskIdException("Task with Identifer" + taskId.toUpperCase() + " doesn't exist");
		}
		remark.setTask(task);
		List<Remark> remarkList = task.getRemark();
		remarkList.add(remark);
		task.setRemark(remarkList);
		remarkRepository.save(remark);
		return taskRepository.save(task);
	}

	@Override
	public Developer authenticateDeveloper(String developerLoginName, String pwd, HttpSession session) {
		Developer developer = developerRepository.findByLoginNameAndPwd(developerLoginName, pwd);
		if (developer == null) {
			throw new InvalidLoginException("Invalid Login Please Enter Details Correctly");
		}
		addDeveloperInSession(developer, session);
		return developer;
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

	@Override
	public List<Task> viewAllTaskByDeveloperLoginName(String developerLoginName) {
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		List<Task> taskList = developer.getTasks();
		return taskList;
	}

	@Override
	public Task findTaskByTaskIdentifierAndDevelpoerLoginName(String taskIdentifier, String developerLoginName) {
		Developer developer = developerRepository.findByLoginName(developerLoginName);
		List<Task> taskList = developer.getTasks();
		for (Task task : taskList) {
			if (task.getTaskIdentifier().equals(taskIdentifier)) {
				return task;
			}
		}
		throw new TaskIdException("Task with id " + taskIdentifier.toUpperCase() + " is not available");
	}

}