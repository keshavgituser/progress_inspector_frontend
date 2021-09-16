package com.capgemini.piapi.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.service.ProductOwnerService;
import com.capgemini.piapi.serviceImpl.MapValidationErrorService;

/**
 * Product Owner controller is used to handle requests and responses.
 * 
 * @author Tejas Naik
 *
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private ProductOwnerService productOwnerService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	/**
	 * Method for logging out ProductOwner and terminating existing session.
	 * 
	 * @param session get current session details
	 * @return Logout Success message
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> handleProductOwnerLogout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<String>("Logout Successful", HttpStatus.OK);
	}
	

	/**
	 * Method for getting Session Information.
	 * 
	 * @param session get current session details
	 * @return Logout Success message
	 */

	@GetMapping("/getSession")
	public ResponseEntity<?> getUserSesssion(HttpSession session) {
		//ProductOwner loggedInOwner = productOwnerService.authenticateProductOwner("owner", "owner", session);
		
		HashMap<String, String> sessionMap = new HashMap<>();
		if(session.getAttribute("userType") != null) {
			String userType = (String)session.getAttribute("userType"); 
			
			switch (userType) {
			case "ProductOwner":
				sessionMap.put("userType", "ProductOwner");
				sessionMap.put("loginName", (String) session.getAttribute("loginName"));
				break;
	
			case "TeamLeader":
				sessionMap.put("userType", "TeamLeader");
				sessionMap.put("loginName", (String) session.getAttribute("teamLeaderLoginName"));
				break;
	
			case "Developer":
				sessionMap.put("userType", "Developer");
				sessionMap.put("loginName", (String) session.getAttribute("developerLoginName"));
				break;
	
			case "Client":
				sessionMap.put("userType", "Client");
				sessionMap.put("loginName", (String) session.getAttribute("loginName"));
				break;
	

			default:
				sessionMap.put("userType", "notLoggedIn");
				sessionMap.put("loginName","notLoggedIn");
				break;
			}
			
			return new ResponseEntity<HashMap<String, String>>(sessionMap, HttpStatus.OK);	
		}
		else {
			sessionMap.put("userType", "notLoggedIn");
			sessionMap.put("loginName","notLoggedIn");
			return new ResponseEntity<HashMap<String, String>>(sessionMap, HttpStatus.OK);
		}

	}

	@GetMapping("/test")
	public ResponseEntity<?> test(HttpSession session) {

		ProductOwner loggedInOwner = productOwnerService.authenticateProductOwner("owner", "owner", session);
		ArrayList<String> al = new ArrayList<>();
		al.add((String) session.getAttribute("userType"));
		al.add((String) session.getAttribute("loginName"));

		HashMap<String, String> hm = new HashMap<>();
		hm.put("userType", (String) session.getAttribute("userType"));
		hm.put("loginName", (String) session.getAttribute("loginName"));
		return new ResponseEntity<HashMap<String, String>>(hm, HttpStatus.OK);
//			return new ResponseEntity<HttpSession>(session, HttpStatus.OK);

	}

}
