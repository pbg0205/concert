package com.cooper.concert.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean
	public TaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

		taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(20);
		taskExecutor.setQueueCapacity(50);

		taskExecutor.setTaskDecorator(new MDCTaskDecorator());
		taskExecutor.setThreadNamePrefix("async-task");
		taskExecutor.setThreadNamePrefix("async-group");

		return taskExecutor;
	}
}
