package com.demo.microservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author sofia 
 * @date 2019-03-02
 */
public class AccountUpdate {
	
	@JsonProperty("account-id")
	private long accountId;

	@JsonProperty("balance-before")
	private long balanceBefore;
	
	@JsonProperty("balance-after")
	private long balanceAfter;
	
	public AccountUpdate() {
		
	}
	
	public AccountUpdate(long accountId, long currentBalance) {
		this(accountId, currentBalance, currentBalance);
	}

	public AccountUpdate(long accountId, long balanceBefore, long balanceAfter) {
		this.accountId = accountId;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
    	if (o == this) return true;
    	if (o.getClass() != this.getClass()) return false;
    	
    	AccountUpdate other = (AccountUpdate) o;
    	
    	return this.accountId == other.accountId &&
    			this.balanceBefore == other.balanceBefore &&
    			this.balanceAfter == other.balanceAfter;
	}
	
	@Override
	public int hashCode() {
		int hash = 31;
		
		hash = hash*17 + Long.hashCode(accountId);
		hash = hash*17 + Long.hashCode(balanceBefore);
		hash = hash*17 + Long.hashCode(balanceAfter);
		
		return hash;
	}

	@Override
    public String toString() {
    	try {
    		return new ObjectMapper().writeValueAsString(this);
    	} catch (Exception e) {
    		return "AccountUpdate[id="+accountId+", balanceBefore="+balanceBefore+", balanceAfter="+balanceAfter+"]";
    	}
    }
	
	public long getAccountId() {
		return accountId;
	}

	public long getBalanceBefore() {
		return balanceBefore;
	}

	public long getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(long balanceAfter) {
		this.balanceAfter = balanceAfter;
	}
	
}
