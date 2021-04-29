package com.capgemini.piapi.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.service.TaskService;
import com.capgemini.piapi.serviceImpl.MapValidationErrorService;


@RestController
@RequestMapping("/api/task")
public class TaskController {


	@Autowired
	private TaskService taskService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	
	@PostMapping("/register")
	//This method is used to create the task at the database if found error returns same page
	public ResponseEntity<?> saveOrUpdate(@Valid @RequestBody Task task, BindingResult result) {
		
		ResponseEntity <?> errorMap =mapValidationErrorService.mapValidationError(result);
		if(errorMap!=null) return errorMap;
		
		Task savedTask = taskService.saveOrUpdate(task);
		return new ResponseEntity<Task>(savedTask, HttpStatus.CREATED);
		
		
	}
	
	@DeleteMapping("/{taskId}")
	//This method is used to delete the task and sends back to task service and then send to QA team
	public ResponseEntity<?> deleteTaskByTasktIdentifier(@PathVariable String taskId){
		
		taskService.deleteTaskByTasktIdentifier(taskId);
		return new ResponseEntity<String>("Task with Id : '"+taskId.toUpperCase()+"'was deleted", HttpStatus.OK);
		
	}
	
}
