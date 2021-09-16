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
 * @author Aadesh Juvekar
 *
 */
@RestController
@RequestMapping("/api/productOwner")
public class ProductOwnerController {

	@Autowired
	private ProductOwnerService productOwnerService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	/**
	 * Method for handling Product Owner login and creating session.
	 * 
	 * @param productOwner
	 * @param result       contains the result and error of validation
	 * @param session      Creates New Session
	 * @return Response Entity with logged In Product Owner with HTTP Status
	 */
	@PostMapping("/login")
	public ResponseEntity<?> handleProductOwnerLogin(@RequestBody ProductOwner productOwner, BindingResult result,
			HttpSession session) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null) {
			return errorMap;
		}
		ProductOwner loggedInOwner = productOwnerService.authenticateProductOwner(productOwner.getLoginName(),
				productOwner.getPwd(), session);
		//return new ResponseEntity<ProductOwner>(loggedInOwner, HttpStatus.OK);
		return new ResponseEntity<String>("Login Successful", HttpStatus.OK);
	}

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
	 * Method for Registration of new Product Owner and storing data to database.
	 * 
	 * @param productOwner Data collected to save.
	 * @param result       contains validation result and errors.
	 * @return saved productOwner in database
	 */
	@PostMapping("/register")
	public ResponseEntity<?> registerOwner(@Valid @RequestBody ProductOwner productOwner, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		ProductOwner savedProductOwner = productOwnerService.saveProductOwner(productOwner);
		//return new ResponseEntity<ProductOwner>(savedProductOwner, HttpStatus.CREATED);
		return new ResponseEntity<String>("Registration Successful", HttpStatus.CREATED);
	}

	/**
	 * Method for updating Product Owner in database.
	 * 
	 * @param productOwner Data collected to update.
	 * @param result       contains validation result and errors.
	 * @return saved productOwner in database
	 */
	@PatchMapping("/update")
	public ResponseEntity<?> updateOwner(@Valid @RequestBody ProductOwner productOwner, BindingResult result,
			HttpSession session) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		if (session.getAttribute("loginName") != null && session.getAttribute("userType").equals("ProductOwner") && session.getAttribute("loginName").equals(productOwner.getLoginName())) {
			ProductOwner savedProductOwner = productOwnerService.updateProductOwner(productOwner);
			return new ResponseEntity<ProductOwner>(savedProductOwner, HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}
	
	/**
	 * Method to delete productOwner by loginName
	 * 
	 * @param loginName of the productOwner
	 */

	@DeleteMapping("/{loginName}")
	public ResponseEntity<?> deleteProductOwner(@PathVariable String loginName, HttpSession session) {
		if (session.getAttribute("loginName") != null&& session.getAttribute("userType").equals("ProductOwner") && session.getAttribute("loginName").equals(loginName)) {

		productOwnerService.deleteProductOwnerByLoginName(loginName);
		return new ResponseEntity<String>("Product Owner with loginName :" + loginName + " is deleted", HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Method to get all the task list
	 * 
	 * @return list of all task list
	 */
	@GetMapping("/tasks")
	public ResponseEntity<?> getTaskList(HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("ProductOwner")) {
			List<Task> tasks = productOwnerService.getAllTasks(session);
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Method to find the Task by taskIdentifier
	 * 
	 * @param taskIdentifier
	 * @return task if found
	 */
	@GetMapping("/task/{taskIdentifier}")
	public ResponseEntity<?> getTaskByTaskIdentifier(@PathVariable String taskIdentifier, HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("ProductOwner")) {

			Task task = productOwnerService.getTaskByTaskIdentifier(taskIdentifier.toUpperCase(), session);
			return new ResponseEntity<Task>(task, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);
	}

	/**
	 * Method to get all tasks
	 * @return list of clients
	 */

	@GetMapping("/clients")
	public ResponseEntity<?> getAllClients(HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("ProductOwner")) {

			List<Client> clients = productOwnerService.getAllClients();
			return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);

	}
	/**
	 * Method to authorize client to view task
	 * 
	 * @param clientLoginName
	 * @param taskIdentifier
	 * @return client if task is authorized
	 */

	@GetMapping("/authorizeClient/{clientLoginName}/{taskIdentifier}")
	public ResponseEntity<?> addTaskToClient(@PathVariable String clientLoginName, @PathVariable String taskIdentifier,
			HttpSession session) {
//		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("ProductOwner")) {

			Client client = productOwnerService.addTaskToClient(clientLoginName, taskIdentifier.toUpperCase());
			return new ResponseEntity<String>("Client authorised to View Task", HttpStatus.OK);
//		}
//		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.UNAUTHORIZED);

	}
	
	@GetMapping("/test")
	public ResponseEntity<?> test(HttpSession session) {
			
		ProductOwner loggedInOwner = productOwnerService.authenticateProductOwner("owner","owner", session);
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
