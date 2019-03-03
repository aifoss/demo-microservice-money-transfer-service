package com.demo.microservice.service;

import com.demo.microservice.model.Transfer;

/**
 * @author sofia 
 * @date 2019-03-02
 */
public interface MoneyTransferService {
	
	Transfer transfer(long fromId, long toId, long amount);

}
