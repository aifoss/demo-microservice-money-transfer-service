package com.demo.microservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.microservice.model.Account;

/**
 * @author sofia 
 * @date 2019-03-02
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}
