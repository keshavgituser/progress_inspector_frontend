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
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleTeamLeaderAlreadyExists(TeamLeaderAlreadyExistsException ex,WebRequest request){
		
		TeamLeaderAlreadyExistsExceptionResponse exceptionResponse = new TeamLeaderAlreadyExistsExceptionResponse(ex.getMessage());
	
		return new ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleTeamLeaderNotFound(TeamLeaderNotFoundException ex,WebRequest request){
		
		TeamLeaderNotFoundExceptionResponse exceptionResponse = new TeamLeaderNotFoundExceptionResponse(ex.getMessage());
	
		return new ResponseEntity<Object>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
}
