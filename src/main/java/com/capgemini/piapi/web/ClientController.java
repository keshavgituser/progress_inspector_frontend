package com.capgemini.piapi.web;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientAlreadyExistException;
import com.capgemini.piapi.service.ClientService;
import com.capgemini.piapi.serviceImpl.MapValidationErrorService;

/**
 * 
 * @author Keshav
 *
 */

@RestController
@RequestMapping("/api/client")
public class ClientController{
    
	private Logger logger = LoggerFactory.getLogger(ClientController.class);
	  
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	/**
	 * This controller will be used for calling the view task method from client service
	 * @param task_id will the unique task identifier
	 * @param loginName is the client login name 
	 * @return the Task object as a response entity
	 */
	@GetMapping("/viewtask/{task_id}")
	public ResponseEntity<?> getTaskByTaskIdentifier(@PathVariable String task_id,HttpSession session){
		
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Client"))
		{
		Task task = clientService.viewTask(session.getAttribute("loginName").toString(), task_id);

		logger.info("--TASK--"+task);
		return new ResponseEntity<Task>(task,HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
		
	}
	
	/**
	 * Thios controller is used for calling the view all task method from clinet service
	 * @param loginName is the client login name
	 * @return all the task objects from the list as a response entity
	 */
	@GetMapping("/viewalltask")
	public ResponseEntity<?> getAllTask(HttpSession session){
		
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Client"))
		{
		List<Task> taskList = clientService.viewAllTask(session);
		
		logger.info("--TASK--"+taskList);
		return new ResponseEntity<List<Task>>(taskList,HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
		
	}
	
	/**
	 * This controller is used for calling add remark method from client service.
	 * Will also be used for retrieving all the errors from input remark object.
	 * @param remark is the object of Remark to be saved
	 * @param task_id is the unique task identifier.
	 * @return saved remark if no errors found or map of the errors found in the input remark object.
	 */
	@PostMapping("/addremark/{task_id}")
	public ResponseEntity<?> addRemark(@Valid @RequestBody Remark remark, BindingResult bindingResult,@PathVariable String task_id,HttpSession session){
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Client"))
		{
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(bindingResult);
		if(errorMap!=null) return errorMap;
		
		Remark addedRemark = clientService.addRemark(remark,task_id);
		return new ResponseEntity<>(addedRemark,HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);

		
	}
	
	
	@PostMapping("/addClient")
	public ResponseEntity<?> addClient(@Valid @RequestBody Client client, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		try {
			Client savedClient = clientService.saveClient(client);
			return new ResponseEntity<Client>(savedClient, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ClientAlreadyExistException("Client Already exists ! Please Login");
		}
	}

	@PatchMapping("/updateClient")
	public ResponseEntity<?> updateClient(@Valid @RequestBody Client client, BindingResult result,HttpSession session) 
	{
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Client"))
		{
			
		
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		Client savedClient = clientService.updateClient(client);
		
		return new ResponseEntity<Client>(savedClient, HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	@DeleteMapping("/deleteClient/{loginName}")
	public ResponseEntity<?> deleteClient(@PathVariable String loginName,HttpSession session) 
	{
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Client"))
		{
		clientService.deleteClientByLoginName(loginName);
		return new ResponseEntity<String>("Client with loginName :" + loginName + " is deleted", HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}
	
	@GetMapping("/{loginName}")
	public ResponseEntity<?> findClientByLoginName(@PathVariable String loginName,HttpSession session) 
	{
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Client"))
		{
		Client client = clientService.findByLoginName(loginName);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}
	
	/*
	 * Login Mappings
	 */
	
	/**
	 * This Method Is REsponsible for login purpose of client
	 * @param client
	 * @param result
	 * @param session
	 * @return
	 */
	@PostMapping("/loginClient")
	public ResponseEntity<?> handleClientLogin(@RequestBody Client client, BindingResult result,
			HttpSession session) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		Client loggedInClient = clientService.authenticateClient(client.getLoginName(),
				client.getPwd(), session);
		return new ResponseEntity<Client>(loggedInClient, HttpStatus.OK);
	}

	@GetMapping("/logoutClient")
	public ResponseEntity<?> handleClientLogout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<String>("Logout Successfully | Have a nice day", HttpStatus.OK);
	}
	
	
}
