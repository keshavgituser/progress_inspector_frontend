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


@RestController
@RequestMapping("/api/client")
public class ClientController{
    
	private Logger logger = LoggerFactory.getLogger(ClientController.class);
	  
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@GetMapping("/viewtask/{loginName}/{task_id}")
	public ResponseEntity<?> getTaskByTaskIdentifier(@PathVariable String task_id,@PathVariable String loginName){
		
		Task task = clientService.viewTask(loginName, task_id);
		
		logger.info("--TASK--"+task);
		return new ResponseEntity<Task>(task,HttpStatus.OK);
		
	}
	@GetMapping("/viewalltask/{loginName}")
	public ResponseEntity<?> getAllTask(@PathVariable String loginName){
		
		List<Task> taskList = clientService.viewAllTask(loginName);
		
		logger.info("--TASK--"+taskList);
		return new ResponseEntity<List<Task>>(taskList,HttpStatus.OK);
		
	}
	@PostMapping("/addremark/{task_id}")
	public ResponseEntity<?> addRemark(@Valid @RequestBody Remark remark, BindingResult bindingResult,@PathVariable String task_id){
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(bindingResult);
		if(errorMap!=null) return errorMap;
		
		Remark addedRemark = clientService.addRemark(remark,task_id);
		return new ResponseEntity<>(addedRemark,HttpStatus.OK);
		
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
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/deleteClient/{loginName}")
	public ResponseEntity<?> deleteClient(@PathVariable String loginName,HttpSession session) 
	{
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Client"))
		{
		clientService.deleteClientByLoginName(loginName);
		return new ResponseEntity<String>("Client with loginName :" + loginName + " is deleted", HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/{loginName}")
	public ResponseEntity<?> findClientByLoginName(@PathVariable String loginName) 
	{
		Client client = clientService.findByLoginName(loginName);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	
	/*
	 *UnComment This Part If YOu Want TO Map A Request To Find all the Clients 
	 *
	@GetMapping("/allClients")
	public ResponseEntity<?> findAllClients() {
		
		List<Client> clients = clientService.getAllClients();
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
	}
	*/
	
	
	/*
	 * Login Mappings
	 */
	
	/**
	 * This Method Is REsponsible for login purpose of client
	 * @param Client
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
