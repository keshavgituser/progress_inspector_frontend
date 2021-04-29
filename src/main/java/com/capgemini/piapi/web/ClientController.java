package com.capgemini.piapi.web;

import java.util.List;

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
}
