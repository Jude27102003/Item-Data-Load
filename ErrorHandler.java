package com.item.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.item.dtos.ErrorResponse;
import com.item.exception.ItemNotFoundException;
import com.item.exception.ValidationException;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandler {

	//Handles ItemNotFoundException and returns 404 NOT FOUND
	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleItemNotFound(ItemNotFoundException ex) {

		ErrorResponse response = new ErrorResponse();
		response.setMessage(ex.getMessage());

		return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
	}

	
	//Handles custom validation exceptions
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(ex.getMessage());
		
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
			
	}

	//Handles validation errors from @Valid annotated request bodies
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {

		StringBuilder sb = new StringBuilder();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {

			sb.append(fieldError.getField() + ":" + fieldError.getDefaultMessage());
		}

		ErrorResponse response = new ErrorResponse(sb.toString());

		return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}

	//Handles access denied exceptions and returns 403 FORBIDDEN
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", HttpStatus.FORBIDDEN.value());
		response.put("error", "Forbidden");
		response.put("message", ex.getMessage());

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	//Handles expired JWT token exceptions
	@ExceptionHandler
	public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
		return new ResponseEntity<>("Expired Token", HttpStatus.BAD_REQUEST);
	}
	
	//Handles SQL integrity constraint violations
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleSqlIntegrityConstraintErrors(SQLIntegrityConstraintViolationException ex){
		ErrorResponse response= new ErrorResponse();
		response.setMessage("SQL Constraint Violation :"+ex.getMessage());
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.BAD_REQUEST);
	}
	
	//Handles constraint violations from validation annotations
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationErrors(ConstraintViolationException ex){
		ErrorResponse response= new ErrorResponse();
		response.setMessage("Constraint Violation :"+ex.getCause());
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.BAD_REQUEST);
	}
	
	//Handles method argument type mismatches, such as passing a string instead of an integer. 
	//Returns a METHOD_NOT_ALLOWED (405) status with the exception message.
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArguments(MethodArgumentTypeMismatchException ex){
		ErrorResponse response = new ErrorResponse();
		response.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	//Handles HTTP method not supported exceptions.
    //Returns a METHOD_NOT_ALLOWED (405) status with the exception message.
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleInvalidMethod(HttpRequestMethodNotSupportedException ex){
		ErrorResponse response = new ErrorResponse();
		response.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	//Handles cases where the request body is not readable or has invalid format.
    //Typically occurs when JSON is malformed or fields have incorrect types.
	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(HttpMessageNotReadableException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setMessage("Invalid input format. Please check field types.");
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.BAD_REQUEST);
    }
	
}
