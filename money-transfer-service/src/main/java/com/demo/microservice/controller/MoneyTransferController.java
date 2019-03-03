package com.demo.microservice.controller;

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

import com.demo.microservice.model.ActionResult;
import com.demo.microservice.model.Transfer;
import com.demo.microservice.service.MoneyTransferService;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@RestController
@Validated
@RequestMapping("/transfer")
@Async("threadPoolExecutor")
public class MoneyTransferController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MoneyTransferController.class);
	
	@Autowired
	private MoneyTransferService moneyTransferService;

	@RequestMapping("/from/{from}/to/{to}/amount/{amount}")
	public CompletableFuture<ResponseEntity<ActionResult<Transfer>>> transfer(
			@PathVariable(value = "from", required = true) @Valid Long from,
			@PathVariable(value = "to", required = true) @Valid Long to,
			@PathVariable(value = "amount", required = true) @Valid Long amount) {
		
		LOGGER.info("transfer() handled by Thread-"+Thread.currentThread().getName());
		
		Transfer transfer = moneyTransferService.transfer(from, to, amount);
		ActionResult<Transfer> actionResult = new ActionResult<Transfer>(ActionResult.Action.TRANSFER, transfer);
		ResponseEntity<ActionResult<Transfer>> response = new ResponseEntity<>(actionResult, HttpStatus.OK);
		
		return CompletableFuture.completedFuture(response);
	}

}
