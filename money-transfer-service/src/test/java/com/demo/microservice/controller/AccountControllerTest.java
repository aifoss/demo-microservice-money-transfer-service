package com.demo.microservice.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.microservice.exception.AccountExistingException;
import com.demo.microservice.exception.AccountNotFoundException;
import com.demo.microservice.exception.InsufficientBalanceException;
import com.demo.microservice.model.Account;
import com.demo.microservice.model.AccountUpdate;
import com.demo.microservice.model.ActionResult;
import com.demo.microservice.service.AccountService;

/**
 * @author sofia
 * @date 2019-03-03
 */
public class AccountControllerTest {

	@InjectMocks
	private AccountController accountController;
	
	@Mock
	private AccountService mockAccountService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_getAllAccounts() throws Exception {
		Account acct1 = new Account(1L, 1000L);
		Account acct2 = new Account(2L, 2000L);
		Account acct3 = new Account(3L, 3000L);
		
		List<Account> allAccounts = Arrays.asList(acct1, acct2, acct3);
		
		ActionResult<List<Account>> actionResult = new ActionResult<List<Account>>(ActionResult.Action.RETRIEVE, allAccounts);
		ResponseEntity<ActionResult<List<Account>>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		CompletableFuture<ResponseEntity<ActionResult<List<Account>>>> result = CompletableFuture.completedFuture(response);
		
		when(mockAccountService.getAllAccounts()).thenReturn(allAccounts);
		
		CompletableFuture<ResponseEntity<ActionResult<List<Account>>>> expected = result;
		CompletableFuture<ResponseEntity<ActionResult<List<Account>>>> actual = accountController.getAllAccounts();
		
		verify(mockAccountService, times(1)).getAllAccounts();
		
		assertEquals(expected.get(), actual.get());
	}
	
	@Test
	public void test_getAccount() throws Exception {
		long id = 1L;
		
		Account account = new Account(id, 1000L);
		
		ActionResult<Account> actionResult = new ActionResult<Account>(ActionResult.Action.RETRIEVE, account);
		ResponseEntity<ActionResult<Account>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		CompletableFuture<ResponseEntity<ActionResult<Account>>> result = CompletableFuture.completedFuture(response);
		
		when(mockAccountService.getAccount(id)).thenReturn(account);
		
		CompletableFuture<ResponseEntity<ActionResult<Account>>> expected = result;
		CompletableFuture<ResponseEntity<ActionResult<Account>>> actual = accountController.getAccount(id);
		
		verify(mockAccountService, times(1)).getAccount(id);
		
		assertEquals(expected.get(), actual.get());
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_getAccount_accountNotFound() throws Exception {
		long nonExistentId = 4L;
		
		AccountNotFoundException e = new AccountNotFoundException("Account with id "+nonExistentId+" not found");
		
		when(mockAccountService.getAccount(nonExistentId)).thenThrow(e);
		
		accountController.getAccount(nonExistentId);
		
		verify(mockAccountService, times(1)).getAccount(nonExistentId);
	}
	
	@Test
	public void test_createAccount() throws Exception {
		long id = 4L;
		long deposit = 4000L;
		
		Account account = new Account(id, deposit);
		
		ActionResult<Account> actionResult = new ActionResult<Account>(ActionResult.Action.CREATE, account);
		ResponseEntity<ActionResult<Account>> response = new ResponseEntity<>(actionResult, HttpStatus.CREATED);
		CompletableFuture<ResponseEntity<ActionResult<Account>>> result = CompletableFuture.completedFuture(response);
		
		when(mockAccountService.createAccount(id, deposit)).thenReturn(account);
		
		CompletableFuture<ResponseEntity<ActionResult<Account>>> expected = result;
		CompletableFuture<ResponseEntity<ActionResult<Account>>> actual = accountController.createAccount(id, deposit);
		
		verify(mockAccountService, times(1)).createAccount(id, deposit);
		
		assertEquals(expected.get(), actual.get());
	}
	
	@Test(expected = AccountExistingException.class)
	public void test_createAccount_accountAlreadyExists() throws Exception {
		long existingId = 2L;
		long deposit = 2000L;
		
		AccountExistingException e = new AccountExistingException("Account with id "+existingId+" already exists");
		
		when(mockAccountService.createAccount(existingId, deposit)).thenThrow(e);
		
		accountController.createAccount(existingId, deposit);
		
		verify(mockAccountService, times(1)).createAccount(existingId, deposit);
	}
	
	@Test
	public void test_deposit() throws Exception {
		long id = 1L;
		long balance = 1000L;
		long amount = 2000L;
		
		AccountUpdate accountUpdate = new AccountUpdate(id, balance, balance+amount);
		
		when(mockAccountService.deposit(id, amount)).thenReturn(accountUpdate);
		
		ActionResult<AccountUpdate> actionResult = new ActionResult<AccountUpdate>(ActionResult.Action.DEPOSIT, accountUpdate);
		ResponseEntity<ActionResult<AccountUpdate>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>> result = CompletableFuture.completedFuture(response);
		
		CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>> expected = result;
		CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>> actual = accountController.deposit(id, amount);
		
		verify(mockAccountService, times(1)).deposit(id, amount);
	
		assertEquals(expected.get(), actual.get());
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_deposit_accountNotFound() throws Exception {
		long nonExistentId = 4L;
		long amount = 4000L;
		
		AccountNotFoundException e = new AccountNotFoundException("Account with id "+nonExistentId+" not found");
		
		when(mockAccountService.deposit(nonExistentId, amount)).thenThrow(e);
		
		accountController.deposit(nonExistentId, amount);
		
		verify(mockAccountService, times(1)).deposit(nonExistentId, amount);
	}
	
	@Test
	public void test_withdraw() throws Exception {
		long id = 2L;
		long balance = 2000L;
		long amount = 1000L;
		
		AccountUpdate accountUpdate = new AccountUpdate(id, balance, balance-amount);
		
		when(mockAccountService.withdraw(id, amount)).thenReturn(accountUpdate);
		
		ActionResult<AccountUpdate> actionResult = new ActionResult<AccountUpdate>(ActionResult.Action.WITHDRAW, accountUpdate);
		ResponseEntity<ActionResult<AccountUpdate>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>> result = CompletableFuture.completedFuture(response);
		
		CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>> expected = result;
		CompletableFuture<ResponseEntity<ActionResult<AccountUpdate>>> actual = accountController.withdraw(id, amount);
		
		verify(mockAccountService, times(1)).withdraw(id, amount);
	
		assertEquals(expected.get(), actual.get());
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_withdraw_accountNotFound() throws Exception {
		long nonExistentId = 4L;
		long amount = 4000L;
		
		AccountNotFoundException e = new AccountNotFoundException("Account with id "+nonExistentId+" not found");
		
		when(mockAccountService.withdraw(nonExistentId, amount)).thenThrow(e);
		
		accountController.withdraw(nonExistentId, amount);
		
		verify(mockAccountService, times(1)).withdraw(nonExistentId, amount);
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void test_withdraw_insufficientBalance() throws Exception {
		long id = 2L;
		long balance = 2000L;
		long amount = 3000L;
		
		InsufficientBalanceException e = new InsufficientBalanceException("Insufficient balance: "+balance);
		
		when(mockAccountService.withdraw(id, amount)).thenThrow(e);
		
		accountController.withdraw(id, amount);
		
		verify(mockAccountService, times(1)).withdraw(id, amount);
	}
	
	@Test
	public void test_deleteAccount() throws Exception {
		long id = 3L;
		
		ActionResult<String> actionResult = new ActionResult<String>(ActionResult.Action.DELETE, "Account "+id+" Deleted");
		ResponseEntity<ActionResult<String>> response =  new ResponseEntity<>(actionResult, HttpStatus.OK);
		CompletableFuture<ResponseEntity<ActionResult<String>>> result = CompletableFuture.completedFuture(response);
		
		when(mockAccountService.deleteAccount(id)).thenReturn(true);
		
		CompletableFuture<ResponseEntity<ActionResult<String>>> expected = result;
		CompletableFuture<ResponseEntity<ActionResult<String>>> actual = accountController.deleteAccount(id);
		
		verify(mockAccountService, times(1)).deleteAccount(id);
		
		assertEquals(expected.get(), actual.get());
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_deleteAccount_accountNotFound() throws Exception {
		long nonExistentId = 5L;
		
		AccountNotFoundException e = new AccountNotFoundException("Account with id "+nonExistentId+" not found");
		
		when(mockAccountService.deleteAccount(nonExistentId)).thenThrow(e);
		
		accountController.deleteAccount(nonExistentId);
		
		verify(mockAccountService, times(1)).deleteAccount(nonExistentId);
	}
	
}
