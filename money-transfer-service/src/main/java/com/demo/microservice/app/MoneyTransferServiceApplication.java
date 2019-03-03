package com.demo.microservice.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author sofia
 * @date 2019-03-02
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
@ComponentScan(basePackages = {"com.demo.microservice"})
@EntityScan(basePackages = {"com.demo.microservice.model"})
@EnableJpaRepositories(basePackages = {"com.demo.microservice.repository"})
public class MoneyTransferServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MoneyTransferServiceApplication.class, args);
	}

}
