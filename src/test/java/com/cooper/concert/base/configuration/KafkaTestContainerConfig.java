package com.cooper.concert.base.configuration;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

@Testcontainers
@Configuration
public class KafkaTestContainerConfig {

	@Container
	static final ConfluentKafkaContainer KAFKA_CONTAINER =
		new ConfluentKafkaContainer("confluentinc/cp-kafka:latest");

	static {
		KAFKA_CONTAINER.start();

		System.setProperty("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
		System.setProperty("spring.kafka.consumer.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
		System.setProperty("spring.kafka.producer.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
	}

}
