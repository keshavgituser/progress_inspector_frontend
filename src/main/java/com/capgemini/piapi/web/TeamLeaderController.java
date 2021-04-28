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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.service.TeamLeaderService;
import com.capgemini.piapi.serviceImpl.MapValidationErrorService;

@RestController
@RequestMapping("/TeamLeader")
public class TeamLeaderController {

	Logger logger = LoggerFactory.getLogger(TeamLeaderController.class);
	@Autowired
	private TeamLeaderService teamLeaderService; 
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerTeamLeader( @Valid @RequestBody TeamLeader teamLeader,BindingResult result){
		
		//If Error Occured Display error otherwise save  and display the TeamLeader 
		ResponseEntity <?> errorMap =mapValidationErrorService.mapValidationError(result);
		if(errorMap!=null) return errorMap;
		
		 TeamLeader savedTeamLeader = teamLeaderService.registerTeamLeader(teamLeader);
		return new  ResponseEntity<TeamLeader>(savedTeamLeader,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateTeamLeader( @Valid @RequestBody TeamLeader teamLeader,BindingResult result,HttpSession session){
		
		ResponseEntity <?> errorMap =mapValidationErrorService.mapValidationError(result);
		if(errorMap!=null) return errorMap;
		
		 TeamLeader validTeamLeader = teamLeaderService.authenticateTeamLeader(teamLeader.getLoginName(),teamLeader.getPwd(), session);
		return new  ResponseEntity<TeamLeader>(validTeamLeader,HttpStatus.CREATED);
	}
	@GetMapping("/logout") 
	public ResponseEntity<?> logOutTeamLeader(HttpSession session){
		session.invalidate();
		return new ResponseEntity<String>("LoggedOut Successfully ...",HttpStatus.OK);
	}
	
	@GetMapping("/{teamLeaderId}")
	public ResponseEntity<?> getTeamLeader(@PathVariable Long teamLeaderId){
		TeamLeader teamLeader = teamLeaderService.findTeamLeader(teamLeaderId);
		logger.info("--TEAMLEADER--"+ teamLeader);
		return new ResponseEntity<TeamLeader>(teamLeader, HttpStatus.OK);
	}
	
	@DeleteMapping("/{teamLeaderId}")
	public ResponseEntity<?> deleteTeamLeader(@PathVariable Long teamLeaderId){
		teamLeaderService.deleteTeamLeader(teamLeaderId);
		return new ResponseEntity<String>("TeamLeader Deleted with Id "+ teamLeaderId,HttpStatus.OK);
	}
}
