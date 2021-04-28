package com.capgemini.piapi.web;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
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
	
	/**
	 * This is a temporary method to add the task.
	 * Only added this method for testing the view task method.
	 * Should be removed at the time of INTEGRATION.  
	 * 
	 */
	@PostMapping("")
	public ResponseEntity<?> createNewTask(@Valid @RequestBody Task task, BindingResult bindingResult) {
		
		ResponseEntity <?> errorMap =mapValidationErrorService.mapValidationError(bindingResult);
		if(errorMap!=null) return errorMap;
		
		Task savedTask = clientService.createNewTask(task);
		return new ResponseEntity<Task>(savedTask, HttpStatus.CREATED);
	}
	
	@GetMapping("/viewtask/{task_id}")
	public ResponseEntity<?> getTaskByTaskIdentifier(@Valid @PathVariable String task_id){
		
		Task task = clientService.viewTask(task_id);
		
		logger.info("--TASK--"+task);
		return new ResponseEntity<Task>(task,HttpStatus.OK);
		
	}
	@PostMapping("/addremark/{task_id}")
	public ResponseEntity<?> addRemark(@Valid @RequestBody Remark remark, BindingResult bindingResult,@PathVariable String task_id){
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(bindingResult);
		if(errorMap!=null) return errorMap;
		
		Remark addedRemark = clientService.addRemark(remark,task_id);
		return new ResponseEntity<>(addedRemark,HttpStatus.OK);
		
	}
}
