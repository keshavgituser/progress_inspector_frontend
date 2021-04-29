package com.capgemini.piapi.web;

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

@RestController
@RequestMapping("/api/owner/")
public class ProductOwnerController {

	// TO DO : Logger - INFO , ERROR in log file

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
	public ResponseEntity<?> handleProductOwnerLogin(@RequestBody Object owner, BindingResult result,
			HttpSession session) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		ProductOwner productOwner=(ProductOwner) owner;
		ProductOwner loggedInOwner = productOwnerService.authenticateProductOwner(productOwner.getLoginName(),
				productOwner.getPwd(), session);
		return new ResponseEntity<ProductOwner>(loggedInOwner, HttpStatus.OK);
	}

	// TO DO REMOVE TEST METHOD
	@GetMapping("/test")
	public ResponseEntity<?> test(HttpSession session) {
		return new ResponseEntity<Object>(session.getAttribute("productOwnerName"), HttpStatus.OK);
	}

	/**
	 * Method for logging out ProductOwner and terminating existing session.
	 * 
	 * @param session get current session details
	 * @return Success message
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> handleProductOwnerLogout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<String>("Logout Successfully | Have a nice day", HttpStatus.OK);
	}

	/**
	 * Method for Registration of new Product Owner and storing data to database.
	 * 
	 * @param productOwner Data collected to save.
	 * @param result       contains validation result and errors.
	 * @return saved productOwner in database
	 */
	@PostMapping("/add")
	public ResponseEntity<?> registerOwner(@Valid @RequestBody ProductOwner productOwner, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		ProductOwner savedProductOwner = productOwnerService.saveProductOwner(productOwner);
		return new ResponseEntity<ProductOwner>(savedProductOwner, HttpStatus.CREATED);
	}

	@PatchMapping("/update")
	public ResponseEntity<?> updateOwner(@Valid @RequestBody ProductOwner productOwner, BindingResult result) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationError(result);
		if (errorMap != null)
			return errorMap;
		ProductOwner savedProductOwner = productOwnerService.updateProductOwner(productOwner);
		return new ResponseEntity<ProductOwner>(savedProductOwner, HttpStatus.CREATED);
	}

	/**
	 * Method to find productOwner by loginName
	 * 
	 * @param loginName of the ProductOwner
	 * @return productOwner if found
	 */
	@GetMapping("/{loginName}")
	public ResponseEntity<?> findProductOwnerByLoginName(@PathVariable String loginName) {
		ProductOwner productOwner = productOwnerService.findProductOwnerByLoginName(loginName);
		return new ResponseEntity<ProductOwner>(productOwner, HttpStatus.OK);
	}

	/**
	 * Method to find all productOwners
	 * 
	 * @return list of all productOwner
	 */
	@GetMapping("/all")
	public ResponseEntity<?> findAll() {
		List<ProductOwner> owners = productOwnerService.findAll();
		return new ResponseEntity<List<ProductOwner>>(owners, HttpStatus.OK);
	}

	/**
	 * Method to delete productOwner by loginName
	 * 
	 * @param loginName of the productOwner
	 */

	@DeleteMapping("/{loginName}")
	public ResponseEntity<?> deleteProductOwner(@PathVariable String loginName) {
		productOwnerService.deleteProductOwnerByLoginName(loginName);
		return new ResponseEntity<String>("Product Owner with loginName :" + loginName + " is deleted", HttpStatus.OK);
	}

	/**
	 * Method to get all the tasks
	 * 
	 * @return list of all tasks
	 */
	@GetMapping("/tasks")
	public ResponseEntity<?> getTaskList(HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("ProductOwner")) {
			List<Task> tasks = productOwnerService.getAllTasks();
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
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

			Task task = productOwnerService.getTaskByTaskIdentifier(taskIdentifier);
			return new ResponseEntity<Task>(task, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Method to authorize client to view task
	 * 
	 * @param clientLoginName
	 * @param taskIdentifier
	 * @return client if task is authorized
	 */

	@GetMapping("/addClient/{clientLoginName}/{taskIdentifier}")
	public ResponseEntity<?> addTaskToClient(@PathVariable String clientLoginName, @PathVariable String taskIdentifier,
			HttpSession session) {
		if (session.getAttribute("userType") != null && session.getAttribute("userType").equals("ProductOwner")) {

			Client client = productOwnerService.addTaskToClient(clientLoginName, taskIdentifier);
			return new ResponseEntity<Client>(client, HttpStatus.OK);
		}
		return new ResponseEntity<String>("You do not have Access!!!", HttpStatus.BAD_REQUEST);

	}
}
