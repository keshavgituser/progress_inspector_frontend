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

}
