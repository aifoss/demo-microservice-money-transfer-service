package com.demo.microservice.service;

import java.util.List;

import com.demo.microservice.model.Account;
import com.demo.microservice.model.AccountUpdate;

/**
 * @author sofia 
 * @date 2019-03-02
 */
public interface AccountService {

	List<Account> getAllAccounts();
	
	Account getAccount(long id);
	
	Account createAccount(long id, long deposit);
	
	AccountUpdate deposit(long id, long amount);
	
	AccountUpdate withdraw(long id, long amount);
	
	boolean deleteAccount(long id);

}
