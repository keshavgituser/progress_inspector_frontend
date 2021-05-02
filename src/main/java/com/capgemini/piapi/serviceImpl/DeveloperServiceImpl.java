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

	

}
