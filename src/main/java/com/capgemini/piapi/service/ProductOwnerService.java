package com.capgemini.piapi.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Task;
/**
 * This ProductOwnerService is responsible for performing product owner related task
 * @author Aadesh Juvekar
 *
 */
public interface ProductOwnerService {
	/**
	 * This saveProductOwner method will register user in system.
	 * @param productOwner to be registered
	 * @return Registered productOwner if successfully registered otherwise null
	 */
	public ProductOwner saveProductOwner(ProductOwner productOwner);
	/**
	 * This updateProductOwner method will update user in system.
	 * @param productOwner to be updated
	 * @return Registered productOwner if successfully updated 
	 */
	public ProductOwner updateProductOwner(ProductOwner productOwner);
	/**
	 * This method is used for deleting productOwner details in database.
	 * @param loginName of the productOwner
	 */
	public void deleteProductOwnerByLoginName(String loginName);
	/**
	 * This method return list of all productOwners in database.
	 * @return list of productOwners.
	 */
	public List<ProductOwner> findAll();
	/**
	 * This method is used to find productOwner in database.
	 * @param loginName of the productOwner to be found
	 * @return productOwner if found otherwise null
	 */
	public ProductOwner findProductOwnerByLoginName(String loginName);
	/**
	 * This method will return list of tasks 
	 * @return list of tasks
	 */
	public List<Task> getAllTasks(HttpSession session);
	/**
	 * This method is used to return task by taskIdentifier
	 * @param taskIdentifier of the task
	 * @return task if found otherwise null
	 */
	public Task getTaskByTaskIdentifier(String taskIdentifier, HttpSession session);
	/**
	 * This method is used for authentication and login of productOwner
	 * @param loginName of the productOwner
	 * @param pwd of the productOwner
	 * @param session created for login
	 * @return logged in productOwner
	 */
	ProductOwner authenticateProductOwner(String loginName, String pwd, HttpSession session);
	/**
	 * This function will authorize client to view task by adding task to client
	 * @param Login Name of the client
	 * @param taskIdentifier of the task
	 * @return Client with authorized task
	 */
	public Client addTaskToClient(String clientLoginName,String taskIdentifier);
	
}
