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

import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.service.DeveloperService;
import com.capgemini.piapi.serviceImpl.MapValidationErrorService;

@RestController
@RequestMapping("api/developers")
public class DeveloperController {

	private static final Logger log = LoggerFactory.getLogger(DeveloperController.class);

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private DeveloperService developerService;

	@PostMapping("/register")
	public ResponseEntity<?> createNewDeveloper(@Valid @RequestBody Developer developer, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		Developer savedDeveloper = developerService.saveDeveloper(developer);
		return new ResponseEntity<Developer>(savedDeveloper, HttpStatus.CREATED);
	}

	@GetMapping("/viewbyid/{devId}")
	public ResponseEntity<?> getDeveloperByDeveloperIdentifier(@PathVariable String devId) {
		Developer developer = developerService.findDeveloperByDevId(devId);
		return new ResponseEntity<Developer>(developer, HttpStatus.OK);
	}

	@GetMapping("/all")
	public Iterable<Developer> getAllDevelopers() {
		return developerService.fillAllDevelopers();
	}

	@DeleteMapping("/deletedeveloper/{devId}")
	public ResponseEntity<?> deleteDeveloper(@PathVariable String devId) {
		developerService.deleteDeveloperbyDevIdentifier(devId);
		return new ResponseEntity<String>("Developer with Identifier " + devId.toUpperCase() + " deleted successfully",
				HttpStatus.OK);
	}

	@PostMapping("/updatestatus/{taskId}/{devId}")
	public ResponseEntity<?> updateTaskStatus(@PathVariable String taskId, @PathVariable String devId,
			@RequestBody Task task) {
		Task updateStatus = developerService.updateTaskStatus(taskId, devId, task);
		return new ResponseEntity<Task>(updateStatus, HttpStatus.OK);
	}
	
	@PostMapping("/addremark/{taskId}/{devId}")
	public ResponseEntity<?> addRemarkInTask(@PathVariable String taskId, @PathVariable String devId,
			 @RequestBody Remark remark) {
		Task addRemark = developerService.addRemark(taskId, devId,remark);
		return new ResponseEntity<Task>(addRemark, HttpStatus.OK);
	}
}
