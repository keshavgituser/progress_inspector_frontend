package com.capgemini.piapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public final ResponseEntity<Object> handleDeveloperNotFoundException(DeveloperNotFoundException ex,
			WebRequest request) {

		DeveloperNotFoundExcpetionResponse exceptionResponse = new DeveloperNotFoundExcpetionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleDeveloperAlreadyExistException(DeveloperAlreadyExistException ex,
			WebRequest request) {

		DeveloperAlreadyExistExceptionResponse exceptionResponse = new DeveloperAlreadyExistExceptionResponse(
				ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleTaskIdException(TaskIdException ex, WebRequest request) {

		TaskIdExceptionResponse exceptionResponse = new TaskIdExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex, WebRequest request) {

		TaskNotFoundExceptionResponse exceptionResponse = new TaskNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleTeamLeaderAlreadyExists(TeamLeaderAlreadyExistsException ex,
			WebRequest request) {

		TeamLeaderAlreadyExistsExceptionResponse exceptionResponse = new TeamLeaderAlreadyExistsExceptionResponse(
				ex.getMessage());

		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleTeamLeaderNotFound(TeamLeaderNotFoundException ex, WebRequest request) {

		TeamLeaderNotFoundExceptionResponse exceptionResponse = new TeamLeaderNotFoundExceptionResponse(
				ex.getMessage());

		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleInvalidLoginException(InvalidLoginException ex, WebRequest request) {

		InvalidLoginExcpetionResponse exceptionResponse = new InvalidLoginExcpetionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleProductOwnerNotFoundException(ProductOwnerNotFoundException ex,
			WebRequest request) {

		ProductOwnerNotFoundExceptionResponse exceptionResponse = new ProductOwnerNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleClientAlreadyExistException(ClientAlreadyExistException ex, WebRequest request) {
		
		ClientAlreadyExistExceptionResponse exceptionResponse =  new ClientAlreadyExistExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleClientNotFoundException(ClientNotFoundException ex, WebRequest request){		
		ClientNotFoundExceptionResponse exceptionResponse=new ClientNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleProductOwnerAlreadyExistException(ProductOwnerAlreadyExistException ex, WebRequest request){		
		ProductOwnerAlreadyExistExceptionResponse exceptionResponse=new ProductOwnerAlreadyExistExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler
	public final ResponseEntity<Object> handleLoginException(LoginException ex, WebRequest request){		
		LoginExceptionResponse exceptionResponse=new LoginExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleClientPassedNullException(ClientPassedNullException ex, WebRequest request){		
		ClientPassedNullExceptionResponse exceptionResponse=new ClientPassedNullExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}

}
