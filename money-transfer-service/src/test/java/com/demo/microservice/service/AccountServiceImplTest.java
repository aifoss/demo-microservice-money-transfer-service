package com.demo.microservice.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.microservice.exception.AccountExistingException;
import com.demo.microservice.exception.AccountNotFoundException;
import com.demo.microservice.exception.InsufficientBalanceException;
import com.demo.microservice.model.Account;
import com.demo.microservice.model.AccountUpdate;
import com.demo.microservice.repository.AccountRepository;

/**
 * @author sofia 
 * @date 2019-03-03
 */
public class AccountServiceImplTest {
	
	@InjectMocks
	private AccountServiceImpl accountService;
	
	@Mock
	private AccountRepository mockAccountRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test_getAllAccounts() {
		Account acct1 = new Account(1L, 1000L);
		Account acct2 = new Account(2L, 2000L);
		Account acct3 = new Account(3L, 3000L);
		
		List<Account> accounts = Arrays.asList(acct1, acct2, acct3);
		
		when(mockAccountRepository.findAll()).thenReturn(accounts);
		
		List<Account> expected = accounts;
		List<Account> actual = accountService.getAllAccounts();
		
		verify(mockAccountRepository, times(1)).findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_getAccount() {
		long id = 1L;
		long balance = 1000L;

		Account account = new Account(id, balance);
		
		when(mockAccountRepository.findById(id)).thenReturn(Optional.of(account));
		
		Account expected = account;
		Account actual = accountService.getAccount(id);
		
		verify(mockAccountRepository, times(1)).findById(id);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_getAccount_accountNotFound() {
		long nonExistentId = 4L;
		
		when(mockAccountRepository.findById(nonExistentId)).thenReturn(Optional.empty());
		
		accountService.getAccount(nonExistentId);
		
		verify(mockAccountRepository, times(1)).findById(nonExistentId);
	}
	
	@Test
	public void test_createAccount() {
		long id = 4L;
		long deposit = 4000L;

		Account newAccount = new Account(id, deposit);
		
		when(mockAccountRepository.save(newAccount)).thenReturn(newAccount);
		
		Account expected = newAccount;
		Account actual = accountService.createAccount(id, deposit);
		
		verify(mockAccountRepository, times(1)).save(newAccount);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccountExistingException.class)
	public void test_createAccount_accountAlreadyExists() {
		long existingId = 3L;
		long balance = 3000L;
		
		Account account = new Account(existingId, balance);
		
		when(mockAccountRepository.findById(existingId)).thenReturn(Optional.of(account));
		
		accountService.createAccount(existingId, balance);
		
		verify(mockAccountRepository, times(1)).findById(existingId);
	}
	
	@Test
	public void test_deposit() {
		long id = 1L;
		long balance = 1000L;
		long amount = 2000L;
		
		Account account = new Account(id, balance);
		AccountUpdate accountUpdate = new AccountUpdate(id, balance, balance+amount);
		
		when(mockAccountRepository.findById(id)).thenReturn(Optional.of(account));
		when(mockAccountRepository.save(account)).thenReturn(account);
		
		AccountUpdate expected = accountUpdate;
		AccountUpdate actual = accountService.deposit(id, amount);
		
		verify(mockAccountRepository, times(1)).findById(id);
		verify(mockAccountRepository, times(1)).save(account);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_deposit_accountNotFound() {
		long nonExistentId = 5L;
		long amount = 5000L;
		
		when(mockAccountRepository.findById(nonExistentId)).thenReturn(Optional.empty());
		
		accountService.deposit(nonExistentId, amount);
		
		verify(mockAccountRepository, times(1)).findById(nonExistentId);
	}
	
	@Test
	public void test_withdraw() {
		long id = 2L;
		long balance = 2000L;
		long amount = 1000L;
		
		Account account = new Account(id, balance);
		AccountUpdate accountUpdate = new AccountUpdate(id, balance, balance-amount);
		
		when(mockAccountRepository.findById(id)).thenReturn(Optional.of(account));
		when(mockAccountRepository.save(account)).thenReturn(account);
		
		AccountUpdate expected = accountUpdate;
		AccountUpdate actual = accountService.withdraw(id, amount);
		
		verify(mockAccountRepository, times(1)).findById(id);
		verify(mockAccountRepository, times(1)).save(account);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_withdraw_accountNotFound() {
		long nonExistentId = 6L;
		long amount = 6000L;
		
		when(mockAccountRepository.findById(nonExistentId)).thenReturn(Optional.empty());
		
		accountService.withdraw(nonExistentId, amount);
		
		verify(mockAccountRepository, times(1)).findById(nonExistentId);
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void test_withdraw_insufficientBalance() {
		long id = 3L;
		long balance = 3000L;
		long amount = 4000L;
		
		Account account = new Account(id, balance);
		
		when(mockAccountRepository.findById(id)).thenReturn(Optional.of(account));
		
		accountService.withdraw(id, amount);
		
		verify(mockAccountRepository, times(1)).findById(id);
	}
	
	@Test
	public void test_deleteAccount() {
		long id = 2L;
		long balance = 0L;
		
		Account account = new Account(id, balance);
		
		when(mockAccountRepository.findById(id)).thenReturn(Optional.of(account));
		
		boolean expected = true;
		boolean actual = accountService.deleteAccount(id);
		
		verify(mockAccountRepository, times(1)).findById(id);
		verify(mockAccountRepository, times(1)).delete(account);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_deleteAccount_hasRemainingBalance() {
		long id = 2L;
		long balance = 2000L;
		
		Account account = new Account(id, balance);
		
		when(mockAccountRepository.findById(id)).thenReturn(Optional.of(account));
		
		boolean expected = true;
		boolean actual = accountService.deleteAccount(id);
		
		verify(mockAccountRepository, times(2)).findById(id);
		verify(mockAccountRepository, times(1)).save(account);
		verify(mockAccountRepository, times(1)).delete(account);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_deleteAccount_accountNotFound() {
		long nonExistentId = 7L;
		
		when(mockAccountRepository.findById(nonExistentId)).thenReturn(Optional.empty());
		
		accountService.deleteAccount(nonExistentId);
		
		verify(mockAccountRepository, times(1)).findById(nonExistentId);
	}

}
