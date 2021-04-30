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
import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.service.DeveloperService;
import com.capgemini.piapi.service.TeamLeaderService;
import com.capgemini.piapi.serviceImpl.MapValidationErrorService;
/**
 * This class is the controller 
 * for teamLeader
 * @author mantu,Shubham,Bhaskarrao
 *
 */
@RestController
@RequestMapping("/piapi/teamleader")
public class TeamLeaderController {

	private static final Logger logger = LoggerFactory.getLogger(TeamLeaderController.class);

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private DeveloperService developerService;

	@Autowired
	private TeamLeaderService teamLeaderService;

	/**
	 * This method is used to register teamleader in database
	 * 
	 * @param teamLeader
	 * @param result
	 * @return saved team leader in database
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerTeamLeader(@Valid @RequestBody TeamLeader teamLeader, BindingResult result) {

		// If Error occurred Display error otherwise save and return the TeamLeader
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;

		TeamLeader savedTeamLeader = teamLeaderService.registerTeamLeader(teamLeader);
		return new ResponseEntity<TeamLeader>(savedTeamLeader, HttpStatus.CREATED);
	}

	/**
	 * Method for handling Team Leader login and creating session.
	 * 
	 * @param teamLeader
	 * @param result
	 * @param session
	 * @return Response Entity with logged In Team Leader with HTTP Status
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticateTeamLeader(@RequestBody TeamLeader teamLeader, BindingResult result,
			HttpSession session) {

		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		TeamLeader validTeamLeader = teamLeaderService.authenticateTeamLeader(teamLeader.getLoginName(),
				teamLeader.getPwd(), session);
		return new ResponseEntity<TeamLeader>(validTeamLeader, HttpStatus.CREATED);
	}

	/**
	 * Method is used for loging out temaleader and invalidating session
	 * 
	 * @param session
	 * @return Response Entity with logged out Team Leader with HTTP Status
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> logOutTeamLeader(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<String>("LoggedOut Successfully ...", HttpStatus.OK);
	}

	/**
	 * Method is used for getting Team Leader on basis of loginName
	 * 
	 * @param teamLeaderloginname
	 * @return Response Entity with logged in Team Leader
	 */
	@GetMapping("/{teamleaderloginname}")
	public ResponseEntity<?> getTeamLeader(@PathVariable String teamleaderloginname) {
		TeamLeader teamLeader = teamLeaderService.findTeamLeaderByLoginName(teamleaderloginname);
		logger.info("--TEAMLEADER--" + teamLeader);
		return new ResponseEntity<TeamLeader>(teamLeader, HttpStatus.OK);
	}

	/**
	 * This method is used to delete Team Leader by Login Name
	 * 
	 * @param teamleaderloginname
	 * @return Response Entity of deleted Team Leader
	 */
	@DeleteMapping("/{teamleaderloginname}")
	public ResponseEntity<?> deleteTeamLeader(@PathVariable String teamleaderloginname) {
		teamLeaderService.deleteTeamLeaderByLoginName(teamleaderloginname);
		return new ResponseEntity<String>("TeamLeader Deleted with login Name " + teamleaderloginname, HttpStatus.OK);
	}

	/**
	 * This method is used to create a task into the DataBase Task is created on the
	 * basis of requirements given by Product Owner
	 * 
	 * @param task
	 * @param result
	 * @param productOwnerLoginName
	 * @param teamleaderLoginName
	 * @param session
	 * @return Response Entity of new created task on basis of ProductOwner
	 *         LoginName and TeamLeader LoginName with HttpStatus
	 */
	@PostMapping("/createtask/{productOwnerLoginName}/{teamleaderLoginName}")
	public ResponseEntity<?> createNewTask(@Valid @RequestBody Task task, BindingResult result,
			@PathVariable String productOwnerLoginName, @PathVariable String teamleaderLoginName, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
			if (errorMap != null)
				return errorMap;
			Task savedTask = teamLeaderService.createTask(task, productOwnerLoginName, teamleaderLoginName);
			return new ResponseEntity<Task>(savedTask, HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method is used to view all tasks created by Team Leader
	 * 
	 * @param session
	 * @return Response Entity with all task if Team Leader is logged in else You do
	 *         not have Access message is appeared with HttpStatus
	 */
	@GetMapping("/all/tasks")
	public ResponseEntity<?> getAllTasks(HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			List<Task> tasks = teamLeaderService.findAllTasks();
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method is used to view Task on the basis of task identifier
	 * 
	 * @param taskID
	 * @param session
	 * @return Response Entity with Task given by task identifier Team Leader is
	 *         logged in else You do not have Access message is appeared with
	 *         HttpStatus
	 */
	@GetMapping("/viewbytaskid/{taskID}")
	public ResponseEntity<?> getTaskByTaskIdentifier(@PathVariable String taskID, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			Task developer = teamLeaderService.findTaskByTaskIdentifier(taskID);
			return new ResponseEntity<Task>(developer, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method is used to delete task based on Task identifier
	 * 
	 * @param taskID
	 * @param session
	 * @return Response Entity with Deleted Task given by task identifier Team
	 *         Leader is logged in else You do not have Access message is appeared
	 *         with HttpStatus
	 */
	@DeleteMapping("/deletetask/{taskID}")
	public ResponseEntity<?> deleteTask(@PathVariable String taskID, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			teamLeaderService.DeleteTask(taskID);
			return new ResponseEntity<String>("Task with Identifier " + taskID.toUpperCase() + " deleted successfully",
					HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method is used to view list of all developers
	 * 
	 * @param session
	 * @return Response Entity with list of all developers if Team Leader is logged
	 *         in else You do not have Access message is appeared with HttpStatus
	 */
	@GetMapping("/all/developers")
	public ResponseEntity<?> getAllDevelopers(HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			List<Developer> developers = developerService.fillAllDevelopers();
			return new ResponseEntity<List<Developer>>(developers, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method is used to view developer on basis of developer login name
	 * 
	 * @param loginname
	 * @param session
	 * @return Response Entity with developer if Team Leader is logged in else You
	 *         do not have Access message is appeared with HttpStatus
	 */
	@GetMapping("/viewbydeveloperloginname/{loginname}")
	public ResponseEntity<?> getDeveloperByDeveloperLoginName(@PathVariable String loginname, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			Developer developer = developerService.findDeveloperByDeveloperLoginName(loginname);
			return new ResponseEntity<Developer>(developer, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method is method is used to assign task to developer
	 * 
	 * @param taskIdentifier
	 * @param devId
	 * @param session
	 * @return Resposne Entity with Task assigned to developer if Team Leader is
	 *         logged in else You do not have Access message is appeared with
	 *         HttpStatus
	 */
	@PatchMapping("/assigndev/{taskIdentifier}/{devId}")
	public ResponseEntity<?> assignDeveloperToTask(@PathVariable String taskIdentifier, @PathVariable String devId,
			HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			Task savedTask = teamLeaderService.assignDeveloper(taskIdentifier, devId);
			return new ResponseEntity<Task>(savedTask, HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method is used to view all remarks
	 * 
	 * @param session
	 * @return Response entity with list of all remarks if Team Leader is logged in
	 *         else You do not have Access message is appeared with HttpStatus
	 */
	@GetMapping("/all/remarks")
	public ResponseEntity<?> getAllRemarks(HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("TeamLeader")) {
			List<Remark> remarks = teamLeaderService.viewAllRemark();
			return new ResponseEntity<List<Remark>>(remarks, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

}
