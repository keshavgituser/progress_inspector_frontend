package com.capgemini.piapi.web;

import javax.servlet.http.HttpSession;
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

/**
 * Developer Controller is used to navigate url request and send the
 * 
 * @author Harsh Joshi
 *
 */
@RestController
@RequestMapping("/api/developer")
public class DeveloperController {

	private static final Logger log = LoggerFactory.getLogger(DeveloperController.class);

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private DeveloperService developerService;

	/**
	 * This method is used to create new developer in data base
	 * 
	 * @param developer
	 * @param result
	 * @return Response Entity with new created developer
	 */
	@PostMapping("/register")
	public ResponseEntity<?> createNewDeveloper(@Valid @RequestBody Developer developer, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		Developer savedDeveloper = developerService.saveDeveloper(developer);
		return new ResponseEntity<Developer>(savedDeveloper, HttpStatus.CREATED);
	}

	/**
	 * This method is used to delete developer on the basis of
	 * 
	 * @param developerLoginName
	 * @param session
	 * @return Response Entity with Deleted Developer if Developer exist
	 */
	@DeleteMapping("/{developerLoginName}")
	public ResponseEntity<?> deleteDeveloper(@PathVariable String developerLoginName, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Developer")
				&& session.getAttribute("developerLoginName").equals(developerLoginName)) {
			developerService.deleteDeveloperbyDeveloperLoginName(developerLoginName);
			return new ResponseEntity<String>("Developer with " + developerLoginName + " deleted successfully",
					HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * This method is used to update existing developer in the data base
	 * 
	 * @param developer
	 * @param result
	 * @param session
	 * @return Response Entity with updated developer
	 */
	@PatchMapping("/update")
	public ResponseEntity<?> updateDeveloper(@Valid @RequestBody Developer developer, BindingResult result,
			HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Developer")
				&& session.getAttribute("developerLeaderLoginName").equals(developer.getLoginName())) {
			ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
			if (errorMap != null)
				return errorMap;
			Developer savedDeveloper = developerService.updateDeveloper(developer);
			return new ResponseEntity<Developer>(savedDeveloper, HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * This method is used to update task status with for given task identifier and
	 * developer loginName
	 * 
	 * @param taskId
	 * @param developerLoginName
	 * @param task
	 * @param session
	 * @return Response Entity with updated task status if developer is logged in
	 *         else You do not have Access message is appeared with Http Status
	 */
	@PostMapping("/updatestatus/{taskId}/{developerLoginName}")
	public ResponseEntity<?> updateTaskStatus(@PathVariable String taskId, @PathVariable String developerLoginName,
			@RequestBody Task task, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Developer")
				&& session.getAttribute("developerLoginName").equals(developerLoginName)) {
			Task updateStatus = developerService.updateTaskStatus(taskId, developerLoginName, task);
			return new ResponseEntity<Task>(updateStatus, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * This method is used to add remark in the data base by developer based on task
	 * id and Developer Login name
	 * 
	 * @param taskId
	 * @param developerLoginName
	 * @param remark
	 * @param session
	 * @return Response Entity with added remark in task if developer is logged in
	 *         else You do not have Access message is appeared with Http Status
	 */
	@PostMapping("/addremark/{taskId}/{developerLoginName}")
	public ResponseEntity<?> addRemarkInTask(@PathVariable String taskId, @PathVariable String developerLoginName,
			@RequestBody Remark remark, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Developer")
				&& session.getAttribute("developerLoginName").equals(developerLoginName)) {
			Task addRemark = developerService.addRemark(taskId, developerLoginName, remark);
			return new ResponseEntity<Task>(addRemark, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * This method is used to login developer in the session
	 * 
	 * @param developer
	 * @param result
	 * @param session
	 * @return Response Entity with logged in developer in session
	 */
	@PostMapping("/login")
	public ResponseEntity<?> handleDeveloperLogin(@RequestBody Developer developer, BindingResult result,
			HttpSession session) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		Developer loggedInDeveloper = developerService.authenticateDeveloper(developer.getLoginName(),
				developer.getPwd(), session);
		return new ResponseEntity<Developer>(loggedInDeveloper, HttpStatus.OK);
	}

	/**
	 * This method is used to log out developer from user
	 * 
	 * @param session
	 * @return Response Entity with logged out developer in session with HttpStatus
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> logOutDeveloper(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<String>("LoggedOut Successfully ...", HttpStatus.OK);
	}

	/**
	 * This method is used to get task by task identifier and developer login name
	 * 
	 * @param taskID
	 * @param developerLoginName
	 * @param session
	 * @return Response Entity with Task if all executed successfully
	 */
	@GetMapping("/viewbytaskid/{taskID}/{developerLoginName}")
	public ResponseEntity<?> getTaskByTaskIdentifier(@PathVariable String taskID,
			@PathVariable String developerLoginName, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")
				&& session.getAttribute("developerLoginName").equals(developerLoginName)) {
			Task developer = developerService.findTaskByTaskIdentifierAndDevelpoerLoginName(taskID, developerLoginName);
			return new ResponseEntity<Task>(developer, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}
}
