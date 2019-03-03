package com.demo.microservice.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author sofia 
 * @date 2019-03-03
 */
@Configuration
public class CustomAsyncConfig extends AsyncConfigurerSupport {
	
	@Value("${app.thread.core-pool}")
	private int corePoolSize;
	
	@Value("${app.thread.max-pool}")
	private int maxPoolSize;
	
	@Value("${app.queue.capacity}")
	private int queueCapacity;

	@Value("${app.thread.timeout}")
	private int threadTimeout;
	
	@Override
	public Executor getAsyncExecutor() {
		return threadPoolTaskExecutor();
	}
	
	@Bean
	@Qualifier("threadPoolExecutor")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
		threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
		threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
		threadPoolTaskExecutor.setKeepAliveSeconds(threadTimeout);
		return threadPoolTaskExecutor;
	}

}
