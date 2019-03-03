package com.demo.microservice.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.microservice.exception.AccountNotFoundException;
import com.demo.microservice.exception.InsufficientBalanceException;
import com.demo.microservice.model.Account;
import com.demo.microservice.model.AccountUpdate;
import com.demo.microservice.model.Transfer;
import com.demo.microservice.repository.AccountRepository;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@Service
public class MoneyTransferServiceImpl implements MoneyTransferService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MoneyTransferServiceImpl.class);
	
	@Autowired
	private AccountRepository repository;
	
	@Override
	public Transfer transfer(long fromId, long toId, long amount) {
		Optional<Account> fromAccount = repository.findById(fromId);
		if (!fromAccount.isPresent()) {
			throw new AccountNotFoundException("Account with id "+fromId+" not found");
		}
		
		Optional<Account> toAccount = repository.findById(toId);
		if (!toAccount.isPresent()) {
			throw new AccountNotFoundException("Account with id "+toId+" not found");
		}
		
		Account from = fromAccount.get();
		Account to = toAccount.get();
		
		AccountUpdate fromAcctInfo = new AccountUpdate(from.getId(), from.getBalance());
		AccountUpdate toAcctInfo = new AccountUpdate(to.getId(), to.getBalance());
		
		try {
			from.subtractFromBalance(amount);
			to.addToBalance(amount);
			
			repository.save(from);
			repository.save(to);
			
			fromAcctInfo.setBalanceAfter(from.getBalance());
			toAcctInfo.setBalanceAfter(to.getBalance());
			
			LOGGER.info(amount+" transferred from account "+fromId+" to account "+toId);
			
			return new Transfer(fromId, toId, amount, Transfer.Status.SUCCESS, "Transfer Complete", fromAcctInfo, toAcctInfo);
			
		} catch (InsufficientBalanceException e) {
			
			LOGGER.info("Insufficient balance in account "+fromId+", aborting transfer ...");
			return new Transfer(fromId, toId, amount, Transfer.Status.FAILURE, "Insufficient Balance", fromAcctInfo, toAcctInfo);
		}
	}

}
