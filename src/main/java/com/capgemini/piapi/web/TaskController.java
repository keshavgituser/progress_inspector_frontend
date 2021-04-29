package com.capgemini.piapi.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.service.TaskServcie;
import com.capgemini.piapi.serviceImpl.MapValidationErrorService;

@RestController
@RequestMapping("/api/task")
public class TaskController {
	private static final Logger log = LoggerFactory.getLogger(DeveloperController.class);

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private TaskServcie taskService;

	@PostMapping("/register")
	public ResponseEntity<?> createNewTask(@Valid @RequestBody Task task, BindingResult result) {
		log.info("Controller" + task.toString());
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		Task savedTask = taskService.createTask(task);
		return new ResponseEntity<Task>(savedTask, HttpStatus.CREATED);
	}

	@GetMapping("/viewbyid/{taskID}")
	public ResponseEntity<?> getTaskByTaskIdentifier(@PathVariable String taskID) {
		Task developer = taskService.findTaskByTaskIdentifier(taskID);
		return new ResponseEntity<Task>(developer, HttpStatus.OK);
	}

	@GetMapping("/all")
	public Iterable<Task> getAllTasks() {
		return taskService.findAllTasks();
	}

	@DeleteMapping("/deletetask/{taskID}")
	public ResponseEntity<?> deleteTask(@PathVariable String taskID) {
		taskService.DeleteTask(taskID);
		return new ResponseEntity<String>("Task with Identifier " + taskID.toUpperCase() + " deleted successfully",
				HttpStatus.OK);
	}

	@PatchMapping("/assigndev/{taskIdentifier}/{devId}")
	public ResponseEntity<?> assignDeveloperToTask(@PathVariable String taskIdentifier, @PathVariable String devId) {
		Task savedTask = taskService.assignDeveloper(taskIdentifier, devId);
		return new ResponseEntity<Task>(savedTask, HttpStatus.CREATED);
	}
}
