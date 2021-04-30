package com.capgemini.piapi.service;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;

/**
 * This Client Service will be used for all client related operations.
 * @author Hrushikesh
 *
 */
public interface ClientService {
	/**
	 * This method will be used to return Task which is associated with the Client.
	 * @param taskIdentifier is the unique identifier for the task
	 * @return the saved Task Object.
	 */
	public Task viewTask(String loginName, String taskIdentifier);
	/**
	 * This method will be used for adding the remark for specific Task.
	 * @param remark is the object of the Remark containing all the information about Remark.
	 * @param task_id is the unique identifier of the task for which remark is to be added.
	 * @return the saved Remark Object.
	 */
	public Remark addRemark(Remark remark, String task_id);
	/**
	 * 
	 * @param loginName
	 * @return
	 */
	public List<Task> viewAllTask(String loginName);
	
	
	/**
	 * This method adds Client to database and Registers it
	 * if client already exist it asks user to login
	 * @param client -the client object as request body
	 * @param result -the result 
	 * @return response entity to 
	 *
	 * @param client
	 * @return savedclient object for addCLient method in controller
	 */
	public Client saveClient(Client client);

	/**
	 * This method will fetch all the clients in the database
	 * @return list of client objects
	 */
	public List<Client> getAllClients();
	
	/**
	 * This Method Updates The Client
	 * @param client
	 * @param result
	 * @return
	 */
	public Client updateClient(@Valid Client client);
	
	/**
	 * This Method Finds CLient With LoginName For Admin User
	 * @param loginName
	 * @return
	 */
	public Client findByLoginName(String loginName);
	/**
	 * This Method Deletes The Client With LoginName
	 * @param loginName
	 */
	public void deleteClientByLoginName(String loginName);
	/**
	 * This method Authenticates the client with login Name and PAssword 
	 * @param loginName
	 * @param pwd
	 * @param session
	 * @return
	 */
	public Client authenticateClient(String loginName, String pwd, HttpSession session);
/**
	 * This function will get client by client's id
	 * @param client
	 * @return client if available
	 */
	public Client getClientById(Long id);
}