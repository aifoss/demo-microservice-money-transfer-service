package com.demo.microservice.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@ControllerAdvice
@Component
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException e, WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Account Not Found", getDetails(e));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AccountExistingException.class)
	public ResponseEntity<Object> handleAccountExistingxception(AccountExistingException e, WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Account Already Present", getDetails(e));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<Object> handleMoneyTransferFailureException(InsufficientBalanceException e, WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Money Transfer Failure", getDetails(e));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failure: Method Argument Type Mismatch", getDetails(e));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(
			MissingPathVariableException e, 
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failure: Missing Path Variable", getDetails(e));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(
			TypeMismatchException e,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failure: Type Mismatch", getDetails(e));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException e,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		
		List<String> details = e.getBindingResult().getAllErrors().stream()
				.map(k -> k.getDefaultMessage())
				.collect(Collectors.toList());
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Parameter Validation Failed", details);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	private List<String> getDetails(Exception e) {
		return Arrays.asList(e.getLocalizedMessage());
	}
	
}
