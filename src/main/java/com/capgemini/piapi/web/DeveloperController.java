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
 * @author Vatsal Shah
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
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

}
