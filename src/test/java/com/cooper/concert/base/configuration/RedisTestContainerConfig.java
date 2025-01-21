package com.cooper.concert.base.configuration;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Configuration
public class RedisTestContainerConfig {

	static {
		GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>("redis:7.4.2")
			.withExposedPorts(6379)
			.withReuse(true);

		REDIS_CONTAINER.start();

		System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
		System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
	}

}
