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
@RequestMapping("/api/developers")
public class DeveloperController {

	private static final Logger log = LoggerFactory.getLogger(DeveloperController.class);

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private DeveloperService developerService;

	/**
	 * This method is used to delete developer on the basis of
	 * 
	 * @param loginname
	 * @param session
	 * @return Response Entity with Deleted Developer if Developer exist
	 */
	@DeleteMapping("/{developerLoginName}")
	public ResponseEntity<?> deleteDeveloper(@PathVariable String developerLoginName, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("Developer")
				&& session.getAttribute("developerLeaderLoginName").equals(developerLoginName)) {
			developerService.deleteDeveloperbyDeveloperLoginName(developerLoginName);
			return new ResponseEntity<String>("Developer with " + developerLoginName + " deleted successfully",
					HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
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
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

}
