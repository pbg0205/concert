package com.cooper.concert.storage.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${token.expire.group-id}")
	private String queueTokenExpireConsumeGroupId;

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, QueueTokenExpireEvent> queueTokenExpireListenerContainerFactory() {
		final ConcurrentKafkaListenerContainerFactory<String, QueueTokenExpireEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(queueTokenExpireEventConsumerFactory());
		return factory;
	}

	public ConsumerFactory<String, QueueTokenExpireEvent> queueTokenExpireEventConsumerFactory() {
		final Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, queueTokenExpireConsumeGroupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(QueueTokenExpireEvent.class));
	}
}
