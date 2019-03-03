package com.demo.microservice.exception;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@JsonPropertyOrder({"message", "details", "status-code"})
public class ErrorResponse {

	@JsonProperty("status-code")
	private int statusCode;
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private List<String> details;
	
	public ErrorResponse(String message, List<String> details) {
		this(0, message, details);
	}
	
	public ErrorResponse(int statusCode, String message, List<String> details) {
		this.statusCode = statusCode;
		this.message = message;
		this.details = details;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

}
