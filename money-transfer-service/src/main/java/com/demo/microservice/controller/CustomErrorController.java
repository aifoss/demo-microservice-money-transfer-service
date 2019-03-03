package com.demo.microservice.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.microservice.exception.ErrorResponse;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@RestController
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	@ResponseBody
	public ResponseEntity<Object> handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status.code");
		Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
		String errMsg = e == null ? "N/A" : e.getMessage();
		HttpStatus status = statusCode != null ? HttpStatus.resolve(statusCode) : HttpStatus.BAD_REQUEST;
		ErrorResponse error = new ErrorResponse(status.value(), "Error", Arrays.asList(errMsg));
		return new ResponseEntity<>(error, status);
	}
	
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
}
