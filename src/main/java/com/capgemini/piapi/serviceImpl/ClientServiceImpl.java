package com.capgemini.piapi.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientAlreadyExistException;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.ClientService;


@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private RemarkRepository remarkRepository;
	
	@Override
	public Task viewTask(String loginName, String taskIdentifier) {
		Task task = null;
		//Integer progress = task.getProgress();
		Client client = clientRepository.findByLoginName(loginName);
		List<Task> taskList = client.getTask();
		//taskList.forEach(task->task.getTaskIdentifier().equals(taskIdentifier));
		for(Task t : taskList) {
			if(t.getTaskIdentifier().equals(taskIdentifier)) {
				task = t;
				break;
			}
		}
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
	public List<Task> viewAllTask(String loginName) {
		Client client = clientRepository.findByLoginName(loginName);
		List<Task> taskList = client.getTask();
		//taskList.forEach(task->task.getTaskIdentifier().equals(taskIdentifier));
		if(taskList.isEmpty()) {
			throw new TaskIdException("No task with task id found");
		}
		return taskList;
	}

	
	/**
	 * @author Keshav Register and Authentication
	 */
	@Override
	public Client saveClient(Client client) {
		
		
		try {
		Client savedClient=clientRepository.save(client);
		return savedClient;
		}
		catch(Exception e)
		{
			throw new ClientAlreadyExistException("Client Already Exists"); 
		}
		
	}
	
	@Override
	public List<Client> getAllClients() {
		
		return clientRepository.findAll();
	}

	@Override
	public Client updateClient(@Valid Client client) {
		
		Client oldClient = clientRepository.findByLoginName(client.getLoginName());
		if (oldClient == null) {
			throw new ClientNotFoundException(
					"Client with loginName : " + client.getLoginName() + " does not exists");
		}
		oldClient = client;
		return clientRepository.save(oldClient);
	}

	@Override
	public Client findByLoginName(String loginName) {
		// TODO Auto-generated method stub
		return clientRepository.findByLoginName(loginName);
	}

	@Override
	public void deleteClientByLoginName(String loginName) {
		
		Client clientTodelete=null;
		if ((clientTodelete=clientRepository.findByLoginName(loginName)) == null) {
			throw new ClientNotFoundException("Client with loginName : " + loginName + " does not exists");
		} 
		clientRepository.delete(clientTodelete);
		
	}
	
	@Override
	public Client authenticateClient(String loginName, String pwd, HttpSession session) {
		Client client=null;
		if ((client=clientRepository.findByLoginNameAndPwd(loginName, pwd)) == null) {
			throw new ClientNotFoundException("Client with loginName : " + loginName + " does not exists");
		} 
		addClientInSession(client, session);
		return client;
	}
	
	/**
	 * This Methods Adds Client in session
	 * @param client Client Objeect
	 * @param session Session Object
	 */
	private void addClientInSession(Client client, HttpSession session) {

		session.setAttribute("userType", "Client");
		session.setAttribute("clientId", client.getId());
		session.setAttribute("Name", client.getClientName());
		session.setAttribute("loginName", client.getLoginName());
		
	}
	
	
}
