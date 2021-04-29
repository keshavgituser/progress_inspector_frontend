package com.capgemini.piapi.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.TaskService;


@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskRepository taskRepository;
	
	
	
	
	@Override
	//This method is used to create the task
	public Task saveOrUpdate(Task task) {
		
		try {
			task.setTaskIdentifier(task.getTaskIdentifier().toUpperCase());
			return taskRepository.save(task);
		}catch(Exception ex) {
			throw new TaskIdException("task id "+task.getTaskIdentifier().toUpperCase()+" is already available");
			
		}
		
	}

	@Override
	//This method is used to delete the task using taskIdentifier
	public void deleteTaskByTasktIdentifier(String taskIdentifier) {
		Task task = taskRepository.findByTaskIdentifier(taskIdentifier);
		if(task==null) {
			throw new TaskIdException("cannot delete task with task id "+taskIdentifier+". This id does not exist.");
		}
		taskRepository.delete(task);
		
	}

	
}
