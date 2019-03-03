package com.demo.microservice.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.microservice.exception.AccountNotFoundException;
import com.demo.microservice.exception.InsufficientBalanceException;
import com.demo.microservice.model.AccountUpdate;
import com.demo.microservice.model.ActionResult;
import com.demo.microservice.model.Transfer;
import com.demo.microservice.service.MoneyTransferService;

/**
 * @author sofia
 * @date 2019-03-03
 */
public class MoneyTransferControllerTest {

	@InjectMocks
	private MoneyTransferController moneyTransferController;
	
	@Mock
	private MoneyTransferService mockMoneyTransferService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test_transfer() throws Exception {
		long from = 1L;
		long fromBalance = 5000L;
		long to = 2L;
		long toBalance = 2000L;
		long amount = 3000L;
		
		AccountUpdate fromAcctInfo = new AccountUpdate(from, fromBalance, fromBalance-amount);
		AccountUpdate toAcctInfo = new AccountUpdate(to, toBalance, toBalance+amount);
		
		Transfer transfer = new Transfer(from, to, amount, Transfer.Status.SUCCESS, "Transfer Complete", fromAcctInfo, toAcctInfo);
		ActionResult<Transfer> actionResult = new ActionResult<Transfer>(ActionResult.Action.TRANSFER, transfer);
		ResponseEntity<ActionResult<Transfer>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		CompletableFuture<ResponseEntity<ActionResult<Transfer>>> result = CompletableFuture.completedFuture(response);
		
		when(mockMoneyTransferService.transfer(from, to, amount)).thenReturn(transfer);
		
		CompletableFuture<ResponseEntity<ActionResult<Transfer>>> expected = result;
		CompletableFuture<ResponseEntity<ActionResult<Transfer>>> actual = moneyTransferController.transfer(from, to, amount);
		
		verify(mockMoneyTransferService, times(1)).transfer(from, to, amount);
		
		assertEquals(expected.get(), actual.get());
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_transfer_fromAccountNotFound() throws Exception {
		long nonExistentFrom = 4L;
		long to = 2L;
		long amount = 3000L;
		
		AccountNotFoundException e = new AccountNotFoundException("Account with id "+nonExistentFrom+" not found");
		
		when(mockMoneyTransferService.transfer(nonExistentFrom, to, amount)).thenThrow(e);
		
		moneyTransferController.transfer(nonExistentFrom, to, amount);
		
		verify(mockMoneyTransferService, times(1)).transfer(nonExistentFrom, to, amount);
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void test_transfer_toAccountNotFound() throws Exception {
		long from = 2L;
		long nonExistentTo = 5L;
		long amount = 1000L;
		
		AccountNotFoundException e = new AccountNotFoundException("Account with id "+nonExistentTo+" not found");
		
		when(mockMoneyTransferService.transfer(from, nonExistentTo, amount)).thenThrow(e);
		
		moneyTransferController.transfer(from, nonExistentTo, amount);
		
		verify(mockMoneyTransferService, times(1)).transfer(from, nonExistentTo, amount);
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void test_transfer_insufficientBalance() throws Exception {
		long from = 1L;
		long fromBalance = 1000L;
		long to = 3L;
		long amount = 5000L;
		
		InsufficientBalanceException e = new InsufficientBalanceException("Insufficient balance: "+fromBalance);
		
		when(mockMoneyTransferService.transfer(from, to, amount)).thenThrow(e);
		
		moneyTransferController.transfer(from, to, amount);
		
		verify(mockMoneyTransferService, times(1)).transfer(from, to, amount);
	}

}
