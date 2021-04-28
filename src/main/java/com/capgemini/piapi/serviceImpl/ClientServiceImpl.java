package com.capgemini.piapi.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private RemarkRepository remarkRepository;
	
	@Override
	public Task viewTask(String taskIdentifier) {
		Task task = taskRepository.findByTaskIdentifier(taskIdentifier);
		//Integer progress = task.getProgress();
		if(task==null) {
			throw new TaskIdException("No task with task id " + taskIdentifier + " found");
		}
		return task;
	}

	@Override
	public Remark addRemark(Remark remark, String task_id) {
		try {
			//We have to set the task for the remark
			Task task = taskRepository.findByTaskIdentifier(task_id);
			List<Remark> remarkList = task.getRemark();
			remarkList.add(remark);
			remark.setTask(task);
			taskRepository.save(task);
			return remarkRepository.save(remark);
		}catch(Exception ex) {
			throw new TaskIdException("Task id "+ task_id +" is not available");
		}
	}

	@Override
	public Task createNewTask(Task task) {
		return taskRepository.save(task);
	}

}
