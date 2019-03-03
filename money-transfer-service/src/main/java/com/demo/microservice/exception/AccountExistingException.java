package com.demo.microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountExistingException extends RuntimeException {

	static final long serialVersionUID = 1L;
	
	public AccountExistingException(String message) {
		super(message);
	}
	
}
