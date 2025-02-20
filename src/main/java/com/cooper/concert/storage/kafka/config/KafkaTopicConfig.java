package com.cooper.concert.storage.kafka.config;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaTopicConfig {

	@Value("${token.expire.topic}")
	private String tokenExpireTopic;

	@Value("${token.expire.dlt}")
	private String tokenExpireDlt;

	private final KafkaAdmin kafkaAdmin;

	private NewTopic tokenExpireTopic() {
		return TopicBuilder.name(tokenExpireTopic)
			.partitions(2)
			.replicas(1)
			.build();
	}

	private NewTopic tokenExpireDlt() {
		return TopicBuilder.name(tokenExpireDlt)
			.partitions(2)
			.replicas(1)
			.build();
	}

	@PostConstruct
	public void init() {
		kafkaAdmin.createOrModifyTopics(tokenExpireTopic());
		kafkaAdmin.createOrModifyTopics(tokenExpireDlt());
	}

	@PreDestroy
	public void destroy() throws ExecutionException, InterruptedException {
		AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());

		DeleteTopicsResult result = adminClient.deleteTopics(List.of(tokenExpireTopic, tokenExpireDlt));

		result.all().get();
	}
}
