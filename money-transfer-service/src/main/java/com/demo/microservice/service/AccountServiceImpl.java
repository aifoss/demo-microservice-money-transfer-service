package com.demo.microservice.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.microservice.exception.AccountExistingException;
import com.demo.microservice.exception.AccountNotFoundException;
import com.demo.microservice.model.Account;
import com.demo.microservice.model.AccountUpdate;
import com.demo.microservice.repository.AccountRepository;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@Service
public class AccountServiceImpl implements AccountService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private AccountRepository repository;

	@Override
	public List<Account> getAllAccounts() {
		List<Account> allAccounts = (List<Account>) repository.findAll();
		LOGGER.info("{} accounts found", allAccounts.size());
		return allAccounts;
	}

	@Override
	public Account getAccount(long id) {
		Account account = findAccount(id);
		LOGGER.info("Account with id "+id+" found");
		return account;
	}
	
	@Override
	public Account createAccount(long id, long amount) {
		Optional<Account> account = repository.findById(id);
		if (account.isPresent()) {
			throw new AccountExistingException("Account with id "+id+" already exists");
		}
		
		Account acct = new Account(id, amount);
		repository.save(acct);
		
		LOGGER.info("Account created with id "+id+" with initial deposit "+amount);
		
		return acct;
	}
	
	@Override
	public AccountUpdate deposit(long id, long amount) {
		Account account = findAccount(id);
		AccountUpdate accountUpdate = new AccountUpdate(id, account.getBalance());
		
		account.addToBalance(amount);
		repository.save(account);
		
		LOGGER.info(amount+" deposited to account "+id);
		
		accountUpdate.setBalanceAfter(account.getBalance());
		
		return accountUpdate;
	}
	
	@Override
	public AccountUpdate withdraw(long id, long amount) {
		Account account = findAccount(id);
		AccountUpdate accountUpdate = new AccountUpdate(id, account.getBalance());
		
		account.subtractFromBalance(amount);
		repository.save(account);
		
		LOGGER.info(amount+" withdrawn from account "+id);
		
		accountUpdate.setBalanceAfter(account.getBalance());
		
		return accountUpdate;
	}
	
	@Override
	public boolean deleteAccount(long id) {
		Account account = findAccount(id);
		
		if (account.hasBalance()) {
			LOGGER.info("Account "+id+" has remaining balance, first withdrawing remaining balance ...");
			withdraw(id, account.getBalance());
		}
		
		repository.delete(account);
		
		LOGGER.info("Account "+id+" deleted");
		
		return true;
	}

	private Account findAccount(long id) {
		Optional<Account> account = repository.findById(id);
		if (!account.isPresent()) {
			throw new AccountNotFoundException("Account with id "+id+" not found");
		}
		return account.get();
	}
	
}
