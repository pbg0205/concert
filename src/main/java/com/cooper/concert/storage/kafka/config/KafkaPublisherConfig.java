package com.cooper.concert.storage.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;

@Configuration
@RequiredArgsConstructor
public class KafkaPublisherConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public KafkaTemplate<String, QueueTokenExpireEvent> queueTokenExpireKafkaTemplate() {
		return new KafkaTemplate<>(queueTokenExpireEventProducerFactory());
	}

	@Bean
	public ProducerFactory<String, QueueTokenExpireEvent> queueTokenExpireEventProducerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.RETRIES_CONFIG, 2);
		props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
		props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30000);
		return new DefaultKafkaProducerFactory<>(props);
	}

}
