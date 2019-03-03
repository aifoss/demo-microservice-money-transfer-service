package com.demo.microservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@JsonPropertyOrder({"status", "message", "from", "to", "amount", "from-account-info", "to-account-info"})
public class Transfer {

    public enum Status {
        SUCCESS,
        FAILURE
    }

    @JsonProperty("from")
    private long fromAccountId;
    
    @JsonProperty("to")
    private long toAccountId;

    @JsonProperty
    private long amount;

    @JsonProperty
    private Status status;

    @JsonProperty
    private String message;
    
    @JsonProperty("from-account-info")
    private AccountUpdate fromAccountInfo;
    
    @JsonProperty("to-account-info")
    private AccountUpdate toAccountInfo;

    public Transfer() {

    }

    public Transfer(
    		long fromAccountId, 
    		long toAccountId, 
    		long amount, 
    		Status status, 
    		String message,
    		AccountUpdate fromAccountInfo,
    		AccountUpdate toAccountInfo) {
    	
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
        this.message = message;
        this.fromAccountInfo = fromAccountInfo;
        this.toAccountInfo = toAccountInfo;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == null) return false;
    	if (o == this) return true;
    	if (o.getClass() != this.getClass()) return false;
    	
    	Transfer other = (Transfer) o;
    	
    	return this.fromAccountId == other.fromAccountId &&
    			this.toAccountId == other.toAccountId &&
    			this.amount == other.amount &&
    			this.status == other.status &&
    			this.message.equals(other.message) &&
    			this.fromAccountInfo.equals(other.fromAccountInfo) &&
    			this.toAccountInfo.equals(other.toAccountInfo);
    }
    
    @Override
    public int hashCode() {
    	int hash = 31;
    	
    	hash = hash*17 + Long.hashCode(fromAccountId);
    	hash = hash*17 + Long.hashCode(toAccountId);
    	hash = hash*17 + Long.hashCode(amount);
    	hash = hash*17 + status.hashCode();
    	hash = hash*17 + message.hashCode();
    	hash = hash*17 + fromAccountInfo.hashCode();
    	hash = hash*17 + toAccountInfo.hashCode();
    	
    	return hash;
    }
    
    @Override
    public String toString() {
    	try {
    		return new ObjectMapper().writeValueAsString(this);
    	} catch (Exception e) {
    		return "Transfer[fromAccountId="+fromAccountId+", toAccountId="+toAccountId+", amount="+amount+
    				", status="+status+", message="+message+
    				", fromAccountInfo="+fromAccountInfo.toString()+", toAccountInfo="+toAccountInfo.toString()+"]";
    	}
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public long getAmount() {
        return amount;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

	public AccountUpdate getFromAccountInfo() {
		return fromAccountInfo;
	}

	public AccountUpdate getToAccountInfo() {
		return toAccountInfo;
	}
    
}
