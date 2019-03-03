package com.demo.microservice.model;

import com.demo.microservice.exception.InsufficientBalanceException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private long id;
    
    @Column(name = "balance")
    @JsonProperty
    private long balance;

    public Account() {

    }
    
    public Account(long id, long deposit) {
    	this.id = id;
    	this.balance = deposit;
    }

    public synchronized void addToBalance(long amount) {
        balance += amount;
    }

    public synchronized void subtractFromBalance(long amount) {
        if (balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance: "+balance);
        }

        balance -= amount;
    }
    
    public synchronized boolean hasBalance() {
    	return balance > 0;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == null) return false;
    	if (o == this) return true;
    	if (o.getClass() != this.getClass()) return false;
    	
    	Account other = (Account) o;
    	
    	return this.id == other.id && this.balance == other.balance;
    }
    
    @Override
    public int hashCode() {
    	int hash = 31;
    	
    	hash = hash*17 + Long.hashCode(id);
    	hash = hash*17 + Long.hashCode(balance);
    	
    	return hash;
    }
    
    @Override
    public String toString() {
    	try {
    		return new ObjectMapper().writeValueAsString(this);
    	} catch (Exception e) {
    		return "Account[id="+id+", balance="+balance+"]";
    	}
    }
    
    public long getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

}
