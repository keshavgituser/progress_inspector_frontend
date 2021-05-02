package com.capgemini.piapi.serviceImpl;

import java.util.ArrayList;
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
 * This ProdcuctOwnerServiceImpl implements the product owner services.
 * 
 * @author Aadesh Juvekar
 *
 */
@Service
public class ProductOwnerServiceImpl implements ProductOwnerService {
	//TODO
	 /* --------------------- Comments and naming conventions ------------------
	 * 
	 * ------------------------Logger Implementation---------------------------
	 * 
	 *---------------------- Optimize Service Logic ----------------------------
	 */
	
	
	private static final Logger log = LoggerFactory.getLogger(ProductOwnerServiceImpl.class);

	
	@Autowired
	private ProductOwnerRepository productOwnerRepository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	//------------------------------------------Product Owner CRUD Operations------------------------------------------------
	@Override
	public ProductOwner saveProductOwner(ProductOwner productOwner) {
		//Check for Null Values
		if (productOwner.getLoginName() == null || productOwner.getName()==null || productOwner.getPwd()==null) {
			throw new NullPointerException("Please Fill the Required Fields");
		} 
		//Check if ProductOwner already exists
		if ((productOwnerRepository.findByLoginName(productOwner.getLoginName())) != null) {
			throw new ProductOwnerAlreadyExistException("Product owner already exists");
		} 
		//Register new  ProductOwner
		return productOwnerRepository.save(productOwner);
	}

	@Override
	public ProductOwner updateProductOwner(ProductOwner productOwner) {
		ProductOwner oldProductOwner =null;
		
		if (productOwner.getLoginName() == null) {
			throw new NullPointerException("Please Fill the Required Fields");
		}
		
		if ((oldProductOwner=productOwnerRepository.findByLoginName(productOwner.getLoginName())) == null) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + productOwner.getLoginName() + " does not exists");
		}
		
		productOwner.setId(oldProductOwner.getId());
		
		oldProductOwner = productOwner;
		return productOwnerRepository.save(oldProductOwner);
	}

	@Override
	public void deleteProductOwnerByLoginName(String loginName) {
		
		ProductOwner productOwner = null;
		
		if (loginName == null) {
			throw new NullPointerException("Please Provide Login Name");
		}
		
		if ((productOwner = productOwnerRepository.findByLoginName(loginName)) == null) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + loginName + " does not exists");
		}
		
		productOwnerRepository.delete(productOwner);

	}

	@Override
	public List<ProductOwner> findAll() {

		try {
			return productOwnerRepository.findAll();
		} catch (Exception e) {
			throw new ProductOwnerNotFoundException("No Product Owner Found");
		}
	}

	@Override
	public ProductOwner findProductOwnerByLoginName(String loginName) {
		try {
			return productOwnerRepository.findByLoginName(loginName);
		} catch (Exception e) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + loginName + " does not exist");
		}

	}
	//--------------------------------------- Product Owner Login --------------------------------------
	@Override
	public ProductOwner authenticateProductOwner(String loginName, String pwd, HttpSession session) {
		ProductOwner productOwner = null;

		if (loginName == null || pwd == null) {
			throw new LoginException("Please Enter Credentials");
		}

		if ((productOwner = productOwnerRepository.findByLoginName(loginName)) == null) {
			throw new ProductOwnerNotFoundException("Product Owner with loginName : " + loginName + " does not exist");
		}

		if (productOwner.getPwd().equals(pwd)) {
			addProductOwnerInSession(productOwner, session);
			return productOwner;
		}
			throw new LoginException("Login Failed ! Invalid Credentials");

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
	//--------------------------------------------Task Operations --------------------------------------------
	@Override
	public List<Task> getAllTasks(HttpSession session) {
		List<Task> tasks =new ArrayList<Task>();
		ProductOwner productOwner = productOwnerRepository.findByLoginName((String) session.getAttribute("loginName"));
		tasks= productOwner.getTask();
		if (tasks==null) {
			throw new TaskNotFoundException("Tasks not available");
		}
		return tasks;
	}

	@Override
	public Task getTaskByTaskIdentifier(String taskIdentifier, HttpSession session) {
			Task savedTask=null;
			
			if(taskIdentifier==null) {
				throw new NullPointerException("Please Provide Task Identifier");
			}
			ProductOwner productOwner = productOwnerRepository.findByLoginName((String) session.getAttribute("loginName"));
			List<Task> tasks = productOwner.getTask();
			for (Task task : tasks) {
				if (task.getTaskIdentifier().equals(taskIdentifier)) {
					savedTask= task;
				}
			}
			if(savedTask==null) {
			throw new TaskNotFoundException("Task with id : '" + taskIdentifier + "' does not exists");
			}
			return savedTask;
	}
	//------------------------------------------Client Operations-------------------------------------------------
	@Override
	public Client addTaskToClient(String clientloginName, String taskIdentifier) {
		Client client = null;
		Task task = null;
		if(clientloginName==null || taskIdentifier==null) {
			throw new NullPointerException("Please Provide Required Fields");

		}
		if ((client = clientRepository.findByLoginName(clientloginName)) == null) {
			throw new ClientNotFoundException("Client with loginName : " + clientloginName + " does not exists");
		}		
		
		if ((task = taskRepository.findByTaskIdentifier(taskIdentifier)) == null) {
			throw new TaskNotFoundException("Task with id : " + taskIdentifier + " does not exists");
		}
		List<Task> listOfTask = new ArrayList<>();
		if(client.getTask()!=null) {
			listOfTask=client.getTask();
		}		
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
