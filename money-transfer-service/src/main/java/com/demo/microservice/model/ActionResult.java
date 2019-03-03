package com.demo.microservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author sofia 
 * @date 2019-03-02
 */
public class ActionResult<T> {

	public enum Action {
		CREATE,
		RETRIEVE,
		DEPOSIT,
		WITHDRAW,
		DELETE,
		TRANSFER
	}
	
	@JsonProperty
	private Action action;
	
	@JsonProperty
	private T result;
	
	public ActionResult(Action action, T result) {
		this.action = action;
		this.result = result;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object o) {
		if (o == null) return false;
    	if (o == this) return true;
    	if (o.getClass() != this.getClass()) return false;
    	
    	ActionResult other = (ActionResult) o;
    	
    	return this.action == other.action && this.result.equals(other.result);
	}
	
	@Override
	public int hashCode() {
		int hash = 31;
		
		hash = hash*17 + action.hashCode();
		hash = hash*17 + result.hashCode();
		
		return hash;
	}
	
	@Override
    public String toString() {
    	try {
    		return new ObjectMapper().writeValueAsString(this);
    	} catch (Exception e) {
    		return "ActionResult[action="+action+", result="+result.toString()+"]";
    	}
    }

	public Action getAction() {
		return action;
	}

	public T getResult() {
		return result;
	}
	
}
