package com.capgemini.piapi.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.constant.DeveloperConstant;
import com.capgemini.piapi.constant.TaskConstants;
import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.DeveloperIdException;
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
			developer.setDevIdentifier(developer.getDevIdentifier().toUpperCase());
			developer.setStatus(DeveloperConstant.DEVELOPER_ACTIVE);
			// new developer is created set task no task assigned
			if (developer.getTasks() == null) {
				List<Task> task = new ArrayList<>();
				developer.setTasks(task);
			}
			return developerRepository.save(developer);
		} catch (Exception ex) {
			throw new DeveloperIdException(
					"developer id " + developer.getDevIdentifier().toUpperCase() + " is already available");
		}
	}

	@Override
	public Developer findDeveloperByDevId(String developerId) {
		Developer developer = developerRepository.findByDevId(developerId);
		if (developer == null) {
			throw new DeveloperIdException("Developer with Identifer" + developerId.toUpperCase() + " doesn't exist");
		}
		return developer;
	}

	@Override
	public List<Developer> fillAllDevelopers() {
		return developerRepository.findAll();
	}

	@Override
	public void deleteDeveloperbyDevIdentifier(String developerId) {
		Developer developer = developerRepository.findByDevId(developerId.toUpperCase());
		if (developer == null) {
			throw new DeveloperIdException("Developer with Identifer" + developerId.toUpperCase() + " doesn't exist");
		}
		developerRepository.delete(developer);
	}

	@Override
	public Task updateTaskStatus(String taskId, String devId, Task task) {
		// get developer
		Developer developer = developerRepository.findByDevId(devId);
		// check if available or not
		// throw exception not found
		if (developer == null) {
			throw new DeveloperIdException("Developer with Identifer" + devId.toUpperCase() + " doesn't exist");
		}
		Task task1 = taskRepository.findByTaskIdentifier(taskId);
		if (task1 == null) {
			throw new TaskIdException("Task with Identifer" + taskId.toUpperCase() + " doesn't exist");
		}
		task1.setProgress(task.getProgress());
		return taskRepository.save(task1);
	}

	@Override
	public Task addRemark(String taskId, String devId, Remark remark) {
		// get developer
		Developer developer = developerRepository.findByDevId(devId);
		// check if available or not
		// throw exception not found
		if (developer == null) {
			throw new DeveloperIdException("Developer with Identifer" + devId.toUpperCase() + " doesn't exist");
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

}
