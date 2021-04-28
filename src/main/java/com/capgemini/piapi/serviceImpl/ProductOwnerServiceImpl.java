package com.capgemini.piapi.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.service.ProductOwnerService;
/**
 * This ProdcuctOwnerServiceImpl implements the business logic on the database.
 * @author Aadesh Juvekar
 *
 */
@Service
public class ProductOwnerServiceImpl implements ProductOwnerService {
	@Autowired
	private ProductOwnerRepository productOwnerRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	@Override
	public List<Task> getAllTasks() {
		List<Task> task=taskRepository.findAll();
		if(task==null) {
			throw new TaskNotFoundException("Tasks not available");
		}
		return task;
	}

	@Override
	public Task getTaskByTaskIdentifier(String taskIdentifier) {
		Task task=taskRepository.findByTaskIdentifier(taskIdentifier);
		if(task==null) {
			throw new TaskNotFoundException("Task with id : '"+taskIdentifier+"' does not exists");
		}
		return task;
	}

	@Override
	public ProductOwner saveOrUpdateProductOwner(ProductOwner productOwner) {
		// TODO Auto-generated method stub
		return productOwnerRepository.save(productOwner);
	}

	@Override
	public void deleteProductOwnerByLoginName(String loginName) {
		// TODO Auto-generated method stub
		productOwnerRepository.deleteByLoginName(loginName);;
		
	}

	@Override
	public List<ProductOwner> findAll() {
		// TODO Auto-generated method stub
		return productOwnerRepository.findAll();
	}

	@Override
	public ProductOwner findProductOwnerByLoginName(String loginName) {
		// TODO Auto-generated method stub
		return productOwnerRepository.findByLoginName(loginName);
	}

	@Override
	public ProductOwner authenticateProductOwner(String loginName, String pwd, HttpSession session) {
		ProductOwner productOwner = productOwnerRepository.findByLoginNameAndPwd(loginName,pwd);
		addProductOwnerInSession(productOwner, session);
		return productOwner;
	}
	
	
	/**
	 * This method is used to add the productOwner to the session
	 * @param productOwner to be added to session
	 * @param session created during login
	 */
	private void addProductOwnerInSession(ProductOwner productOwner, HttpSession session) {
		
		session.setAttribute("userType", "ProductOwner");
		session.setAttribute("productOwnerId", productOwner.getId());
		session.setAttribute("productOwner", productOwner);

		
	}

}
