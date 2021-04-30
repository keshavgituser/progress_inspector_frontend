package com.capgemini.piapi.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.exception.LoginException;
import com.capgemini.piapi.exception.ProductOwnerAlreadyExistException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.exception.TaskIdException;
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
	/**TO DO
	 * product Owner null exception
	 * Delete and Patch API Verify
	 * Login Test Password wrong or loginname wrong or not found.
	 * Comments and naming conventions
	 * Task progress and all tasks test cases
	 * add Client to task test cases
	 * 
	 * Optimize Service Logic
	 */
	
	
	private static final Logger log = LoggerFactory.getLogger(ProductOwnerServiceImpl.class);

	
	@Autowired
	private ProductOwnerRepository productOwnerRepository;

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public ProductOwner saveProductOwner(ProductOwner productOwner) {
		// TODO Auto-generated method stub
		if (productOwner.getLoginName() == null || productOwner.getName()==null || productOwner.getPwd()==null) {
			throw new ProductOwnerNotFoundException("Please Fill the Required Fields");
		} else if ((productOwnerRepository.findByLoginName(productOwner.getLoginName())) != null) {
			throw new ProductOwnerAlreadyExistException("Product owner already exists !!!");
		} else {
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
			throw new ProductOwnerNotFoundException(
					"Product Owner with loginName : " + productOwner.getLoginName() + " does not exists");
		}
		oldProductOwner = productOwner;
		return productOwnerRepository.save(oldProductOwner);
	}

	@Override
	public void deleteProductOwnerByLoginName(String loginName) {
		// TODO Auto-generated method stub
		ProductOwner productOwner = null;
		if ((productOwner = productOwnerRepository.findByLoginName(loginName)) == null) {
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
		ProductOwner productOwner = null;
		if(loginName==null || pwd==null) throw new LoginException("Please Enter Credentials");
		if ((productOwner = productOwnerRepository.findByLoginName(loginName)) == null) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + loginName + " does not exist");
		}
		if (productOwner.getPwd().equals(pwd)) {
			addProductOwnerInSession(productOwner, session);
			log.info("-------LoginMethod------- : Login Successfull");
			return productOwner;		
		}
		else {
			throw new LoginException("Login Failed ! Invalid Credentials");
		}
		
		
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
	public List<Task> getAllTasks(HttpSession session) {
		try {
			ProductOwner productOwner = findProductOwnerByLoginName((String) session.getAttribute("loginName"));
			return productOwner.getTask();
		} catch (Exception e) {
			throw new TaskNotFoundException("Tasks not available");
		}
}

	@Override
	public Task getTaskByTaskIdentifier(String taskIdentifier, HttpSession session) {
			ProductOwner productOwner = findProductOwnerByLoginName((String) session.getAttribute("loginName"));
			List<Task> tasks = productOwner.getTask();
			for (Task task : tasks) {
				if (task.getTaskIdentifier().equals(taskIdentifier)) {
					return task;
				}
			}
			throw new TaskIdException("Task with id : '" + taskIdentifier + "' does not exists");
	}

	@Override
	public Client addTaskToClient(String clientloginName, String taskIdentifier) {
		Client client = null;
		if ((client = clientRepository.findByLoginName(clientloginName)) == null) {
			throw new ProductOwnerNotFoundException("Client with loginName : " + clientloginName + " does not exists");
		}
		Task task = null;
		if ((task = taskRepository.findByTaskIdentifier(taskIdentifier)) == null) {
			throw new TaskNotFoundException("Task with id : '" + taskIdentifier + "' does not exists");
		}
		List<Task> listOfTask = client.getTask();
		listOfTask.add(task);
		client.setTask(listOfTask);
		clientRepository.save(client);
		return client;
	}

	@Override
	public List<Client> getAllClients() {
		try {
			return clientRepository.findAll();
		} catch (Exception e) {
			throw new ClientNotFoundException("Clients not available");
		}

	}

}
