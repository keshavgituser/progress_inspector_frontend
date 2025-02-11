package com.capgemini.piapi.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientAlreadyExistException;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.exception.ClientPassedNullException;
import com.capgemini.piapi.exception.LoginException;
import com.capgemini.piapi.exception.ProductOwnerAlreadyExistException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;

import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.ClientService;



/**
 * This ClientServiceImpl implements the business logic on the database.
 * @author tejas
 *
 */
@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private RemarkRepository remarkRepository;
	
	
	/**
	 * View Task progress and add remark
	 * @author Hrushikesh
	 */
	@Override
	public Task viewTask(String loginName, String taskIdentifier) {
		Task task = null;
		//Integer progress = task.getProgress();
		Client client = clientRepository.findByLoginName(loginName);
		if(client==null) {
			throw new ClientNotFoundException("No Client with login name " + loginName + " found");
		}
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
			task.setRemark(remarkList);
			taskRepository.save(task);
			return remarkRepository.save(remark);
		}catch(Exception ex) {
			throw new TaskIdException("Task id "+ task_id +" is not available");
		}
	}

	@Override
	public List<Task> viewAllTask(HttpSession session) {
		Client client = clientRepository.findByLoginName((String) session.getAttribute("loginName"));
		List<Task> taskList = client.getTask();
		//taskList.forEach(task->task.getTaskIdentifier().equals(taskIdentifier));
		if(taskList==null) {
			throw new TaskIdException("No task with task id found");
		}
		return taskList;
	}

	
	/**
	 *@author Keshav
	 * Register and Authentication
	 */
	@Override
	public Client saveClient(Client client) {
		
		try {
			
			if(clientRepository.findByLoginName(client.getLoginName())!=null)
			{
				throw new ClientAlreadyExistException("Client Already Exist Please Login");
			}
			
		if(client.getClientName()==null)
		{
			throw new ClientPassedNullException("ClientName is Null");
		}
		else if(client.getLoginName()==null)
		{
			
			throw new ClientPassedNullException("loginName is Null");
		}
		else if(client.getPwd()==null)
		{
			throw new ClientPassedNullException("Password is Null");
		}
		else
		{
			
			Client savedClient=clientRepository.save(client);
			return savedClient;
		}
		
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
	
	@Override
	public List<Client> getAllClients() {
		
		List<Client> foundClients=clientRepository.findAll();
			if(foundClients.isEmpty())
			{
				throw new ClientNotFoundException("Clients Not Found");
			
			}
			else
			{
				
				return foundClients;
			}
		
	}

	@Override
	public Client updateClient(@Valid Client client) {
		
		Client oldClient = clientRepository.findByLoginName(client.getLoginName());
		if (oldClient == null) {
			throw new ClientNotFoundException(
					"Client with loginName : " + client.getLoginName() + " does not exists");
		}
		client.setId(oldClient.getId());
		oldClient = client;
		return clientRepository.save(oldClient);
	}

	@Override
	public Client findByLoginName(String loginName) {
		// TODO Auto-generated method stub
		if(loginName!=null)
		{
		return clientRepository.findByLoginName(loginName);
		}
		else
		{
			throw new ClientPassedNullException("Login Name Cannot Be Null");
		}
		
	}

	@Override
	public void deleteClientByLoginName(String loginName) {
		
		Client clientTodelete=null;
		if ((clientTodelete=clientRepository.findByLoginName(loginName)) == null) {
			throw new ClientNotFoundException("Client with loginName : " + loginName + " does not exists");
		} 
		clientRepository.delete(clientTodelete);
		clientTodelete.setLoginName(null);		
	}
	
	@Override
	public Client authenticateClient(String loginName, String pwd, HttpSession session) {
		Client productOwner = null;

		if (loginName == null || pwd == null) {
			throw new LoginException("Please Enter Credentials");
		}

		if ((productOwner = clientRepository.findByLoginName(loginName)) == null) {
			throw new ClientNotFoundException("Client with loginName : " + loginName + " does not exist");
		}

		if (productOwner.getPwd().equals(pwd)) {
			addClientInSession(productOwner, session);
			return productOwner;
		}
			throw new LoginException("Login Failed ! Invalid Credentials");
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
