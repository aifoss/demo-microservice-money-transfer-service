package com.demo.microservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

	static final long serialVersionUID = 1L;
	
	public AccountNotFoundException(String message) {
		super(message);
	}

}
