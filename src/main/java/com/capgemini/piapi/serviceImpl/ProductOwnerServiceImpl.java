package com.capgemini.piapi.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ProductOwnerAlreadyExistException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.ProductOwnerService;

/**
 * This ProdcuctOwnerServiceImpl implements the business logic on the database.
 * 
 * @author Aadesh Juvekar
 *
 */
@Service
public class ProductOwnerServiceImpl implements ProductOwnerService {
	@Autowired
	private ProductOwnerRepository productOwnerRepository;

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public ProductOwner saveProductOwner(ProductOwner productOwner) {
		// TODO Auto-generated method stub
		if (productOwner.getLoginName() == null) {
			throw new ProductOwnerNotFoundException("Please Fill the Required Fields");
		} 
		else if ((productOwnerRepository.findByLoginName(productOwner.getLoginName())) != null) {
			throw new ProductOwnerAlreadyExistException("Product owner already exists !!!");
		} 
		else {
			return productOwnerRepository.save(productOwner);
		}

	}

	@Override
	public ProductOwner updateProductOwner(ProductOwner productOwner) {
		// TODO Auto-generated method stub
		if (productOwner.getLoginName() == null) {
			throw new ProductOwnerNotFoundException("Please Fill the Required Fields");
		} 
		ProductOwner oldProductOwner = productOwnerRepository.findByLoginName(productOwner.getLoginName());
		if (oldProductOwner == null) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + productOwner.getLoginName() + " does not exists");
		}
		oldProductOwner = productOwner;
		return productOwnerRepository.save(oldProductOwner);
	}

	@Override
	public void deleteProductOwnerByLoginName(String loginName) {
		// TODO Auto-generated method stub
		ProductOwner productOwner=null;
		if ((productOwner=productOwnerRepository.findByLoginName(loginName)) == null) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + loginName + " does not exists");
		} 
		productOwnerRepository.delete(productOwner);

	}

	@Override
	public List<ProductOwner> findAll() {
		// TODO Auto-generated method stub
		try {
			return productOwnerRepository.findAll();
		} catch (Exception e) {
			throw new ProductOwnerNotFoundException("No Product Owner Found");
		}
	}

	@Override
	public ProductOwner findProductOwnerByLoginName(String loginName) {
		// TODO Auto-generated method stub
		try {
			return productOwnerRepository.findByLoginName(loginName);
		} catch (Exception e) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + loginName + " does not exist");
		}

	}

	@Override
	public ProductOwner authenticateProductOwner(String loginName, String pwd, HttpSession session) {
		ProductOwner productOwner=null;
		if ((productOwner=productOwnerRepository.findByLoginNameAndPwd(loginName, pwd)) == null) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + loginName + " does not exists");
		} 
		addProductOwnerInSession(productOwner, session);
		return productOwner;
	}

	/**
	 * This method is used to add the productOwner to the session
	 * 
	 * @param productOwner to be added to session
	 * @param session      created during login
	 */
	private void addProductOwnerInSession(ProductOwner productOwner, HttpSession session) {

		session.setAttribute("userType", "ProductOwner");
		session.setAttribute("productOwnerId", productOwner.getId());
		session.setAttribute("loginName", productOwner.getLoginName());
	}

	@Override
	public List<Task> getAllTasks() {
		try {
			return taskRepository.findAll();
		} catch (Exception e) {
			throw new TaskNotFoundException("Tasks not available");
		}

	}

	@Override
	public Task getTaskByTaskIdentifier(String taskIdentifier) {
		Task task = taskRepository.findByTaskIdentifier(taskIdentifier);
		if (task == null) {
			throw new TaskNotFoundException("Task with id : '" + taskIdentifier + "' does not exists");
		}
		return task;
	}

	@Override
	public Client addTaskToClient(String clientloginName, String taskIdentifier) {
		Client client=null;
		if ((client=clientRepository.findByLoginName(clientloginName)) == null) {
			throw new ProductOwnerNotFoundException("Client with loginName : " + clientloginName + " does not exists");
		} 
		Task task=null;
		if ((task =taskRepository.findByTaskIdentifier(taskIdentifier))== null) {
			throw new TaskNotFoundException("Task with id : '" + taskIdentifier + "' does not exists");
		}
		List<Task> listOfTask = client.getTask();
		listOfTask.add(task);
		client.setTask(listOfTask);
		clientRepository.save(client);
		return client;
	}

}
