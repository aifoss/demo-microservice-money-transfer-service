package com.demo.microservice.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.microservice.model.Account;
import com.demo.microservice.model.AccountUpdate;
import com.demo.microservice.model.ActionResult;
import com.demo.microservice.service.AccountService;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@RestController
@Validated
@RequestMapping("/account")
@Async("threadPoolExecutor")
public class AccountController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping("/all")
	public CompletableFuture<ResponseEntity<ActionResult<List<Account>>>> getAllAccounts() {
		LOGGER.info("getAllAccounts() handled by Thread-"+Thread.currentThread().getName());
		
		List<Account> allAccounts = accountService.getAllAccounts();
		ActionResult<List<Account>> actionResult = new ActionResult<List<Account>>(ActionResult.Action.RETRIEVE, allAccounts);
		ResponseEntity<ActionResult<List<Account>>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		
		return CompletableFuture.completedFuture(response);
	}

	@RequestMapping("/id/{id}")
	public CompletableFuture<ResponseEntity<ActionResult<Account>>> getAccount(
			@PathVariable(value = "id", required = true) @Valid Long id) {
		
		LOGGER.info("getAccount() handled by Thread-"+Thread.currentThread().getName());
		
		Account account = accountService.getAccount(id);
		ActionResult<Account> actionResult = new ActionResult<Account>(ActionResult.Action.RETRIEVE, account);
		ResponseEntity<ActionResult<Account>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		
		return CompletableFuture.completedFuture(response);
	}
	
	@RequestMapping("/create/id/{id}/deposit/{deposit}")
	public CompletableFuture<ResponseEntity<ActionResult<Account>>> createAccount(
			@PathVariable(value = "id", required = true) @Valid Long id,
			@PathVariable(value = "deposit", required = true) @Valid Long deposit) {
		
		LOGGER.info("createAccount() handled by Thread-"+Thread.currentThread().getName());
		
		Account account = accountService.createAccount(id, deposit);
		ActionResult<Account> actionResult = new ActionResult<Account>(ActionResult.Action.CREATE, account);
		ResponseEntity<ActionResult<Account>> response = new ResponseEntity<>(actionResult, HttpStatus.CREATED);
		
		return CompletableFuture.completedFuture(response);
	}
	
	@RequestMapping("/deposit/id/{id}/amount/{amount}")
	public CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>>deposit(
			@PathVariable(value = "id", required = true) @Valid Long id,
			@PathVariable(value = "amount", required = true) @Valid Long amount) {
		
		LOGGER.info("deposit() handled by Thread-"+Thread.currentThread().getName());
		
		AccountUpdate accountUpdate = accountService.deposit(id, amount);
		ActionResult<AccountUpdate> actionResult = new ActionResult<AccountUpdate>(ActionResult.Action.DEPOSIT, accountUpdate);
		ResponseEntity<ActionResult<AccountUpdate>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		
		return CompletableFuture.completedFuture(response);
	}
	
	@RequestMapping("/withdraw/id/{id}/amount/{amount}")
	public CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>> withdraw(
			@PathVariable(value = "id", required = true) @Valid Long id,
			@PathVariable(value = "amount", required = true) @Valid Long amount) {
		
		LOGGER.info("withdraw() handled by Thread-"+Thread.currentThread().getName());
		
		AccountUpdate accountUpdate = accountService.withdraw(id, amount);
		ActionResult<AccountUpdate> actionResult = new ActionResult<AccountUpdate>(ActionResult.Action.WITHDRAW, accountUpdate);
		ResponseEntity<ActionResult<AccountUpdate>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		
		return CompletableFuture.completedFuture(response);
	}
	
	@RequestMapping("/delete/id/{id}")
	public CompletableFuture<ResponseEntity<ActionResult<String>>> deleteAccount(
			@PathVariable(value = "id", required = true) @Valid Long id) {
		
		LOGGER.info("deleteAccount() handled by Thread-"+Thread.currentThread().getName());
		
		accountService.deleteAccount(id);
		ActionResult<String> actionResult = new ActionResult<String>(ActionResult.Action.DELETE, "Account "+id+" Deleted");
		ResponseEntity<ActionResult<String>> response =  new ResponseEntity<>(actionResult, HttpStatus.OK);
		
		return CompletableFuture.completedFuture(response);
	}
	
}
