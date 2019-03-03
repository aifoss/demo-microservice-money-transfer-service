package com.demo.microservice.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.microservice.exception.AccountNotFoundException;
import com.demo.microservice.model.Account;
import com.demo.microservice.model.AccountUpdate;
import com.demo.microservice.model.Transfer;
import com.demo.microservice.repository.AccountRepository;

/**
 * @author sofia
 * @date 2019-03-03
 */
public class MoneyTransferServiceImplTest {
	
	@InjectMocks
	private MoneyTransferServiceImpl moneyTransferService;
	
	@Mock
	private AccountRepository mockAccountRepository;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_transfer() {
		long fromId = 1L;
		long fromBalance = 5000L;
		long toId = 2L;
		long toBalance = 2000L;
		long amount = 3000L;
		
		Account fromAccount = new Account(fromId, fromBalance);
		Account toAccount = new Account(toId, toBalance);
		
		AccountUpdate fromAcctInfo = new AccountUpdate(fromId, fromBalance, fromBalance-amount);
		AccountUpdate toAcctInfo = new AccountUpdate(toId, toBalance, toBalance+amount);
		
		when(mockAccountRepository.findById(fromId)).thenReturn(Optional.of(fromAccount));
		when(mockAccountRepository.findById(toId)).thenReturn(Optional.of(toAccount));
		
		when(mockAccountRepository.save(fromAccount)).thenReturn(fromAccount);
		when(mockAccountRepository.save(toAccount)).thenReturn(toAccount);
		
		Transfer expected = new Transfer(
				fromId, toId, amount, Transfer.Status.SUCCESS, "Transfer Complete", fromAcctInfo, toAcctInfo);
		Transfer actual = moneyTransferService.transfer(fromId, toId, amount);
		
		verify(mockAccountRepository, times(1)).findById(fromId);
		verify(mockAccountRepository, times(1)).findById(toId);
		verify(mockAccountRepository, times(1)).save(fromAccount);
		verify(mockAccountRepository, times(1)).save(toAccount);
		
		assertEquals(expected, actual);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_transfer_fromAccountNotFound() {
		long nonExistentFromId = 4L;
		long toId = 2L;
		long amount = 3000L;
		
		when(mockAccountRepository.findById(nonExistentFromId)).thenReturn(Optional.empty());
		
		moneyTransferService.transfer(nonExistentFromId, toId, amount);
		
		verify(mockAccountRepository, times(1)).findById(nonExistentFromId);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_transfer_toAccountNotFound() {
		long fromId = 1L;
		long fromBalance = 5000L;
		long nonExistentToId = 5L;
		long amount = 3000L;
		
		Account fromAccount = new Account(fromId, fromBalance);
		
		when(mockAccountRepository.findById(fromId)).thenReturn(Optional.of(fromAccount));
		when(mockAccountRepository.findById(nonExistentToId)).thenReturn(Optional.empty());
		
		moneyTransferService.transfer(fromId, nonExistentToId, amount);
		
		verify(mockAccountRepository, times(1)).findById(fromId);
		verify(mockAccountRepository, times(1)).findById(nonExistentToId);
	}
	
	@Test
	public void test_transfer_insufficientBalance() {
		long fromId = 1L;
		long fromBalance = 2000L;
		long toId = 2L;
		long toBalance = 5000L;
		long amount = 3000L;
		
		Account fromAccount = new Account(fromId, fromBalance);
		Account toAccount = new Account(toId, toBalance);
		
		AccountUpdate fromAcctInfo = new AccountUpdate(fromId, fromBalance, fromBalance);
		AccountUpdate toAcctInfo = new AccountUpdate(toId, toBalance, toBalance);
		
		when(mockAccountRepository.findById(fromId)).thenReturn(Optional.of(fromAccount));
		when(mockAccountRepository.findById(toId)).thenReturn(Optional.of(toAccount));
		
		Transfer expected = new Transfer(
				fromId, toId, amount, Transfer.Status.FAILURE, "Insufficient Balance", fromAcctInfo, toAcctInfo);
		Transfer actual = moneyTransferService.transfer(fromId, toId, amount);
		
		verify(mockAccountRepository, times(1)).findById(fromId);
		verify(mockAccountRepository, times(1)).findById(toId);
		
		assertEquals(expected, actual);
	}
	
}
