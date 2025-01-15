package com.cooper.concert.base.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;

@Configuration
public class ScheduleTestConfiguration {
	@Bean
	@Primary
	public TaskScheduler mockTaskScheduler() {
		return Mockito.mock(TaskScheduler.class);
	}
}
