package com.cooper.concert.domain.queues.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;
import com.cooper.concert.domain.queues.service.publisher.QueueTokenExpireEventPublisher;

@Slf4j
@Component
public class KafkaQueueTokenExpireEventPublisher implements QueueTokenExpireEventPublisher {

	@Value("${token.expire.topic}")
	private String queueTokenExpireTopic;

	private final KafkaTemplate<String, QueueTokenExpireEvent> queueTokenExpireEventKafkaTemplate;
	private final ObjectMapper objectMapper;

	public KafkaQueueTokenExpireEventPublisher(
		@Qualifier("queueTokenExpireKafkaTemplate") final KafkaTemplate<String, QueueTokenExpireEvent> kafkaTemplate,
		ObjectMapper objectMapper) {
		this.queueTokenExpireEventKafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	@Override
	public void send(final QueueTokenExpireEvent queueTokenExpireEvent) {
		try {
			queueTokenExpireEventKafkaTemplate.send(queueTokenExpireTopic, queueTokenExpireEvent);
			log.debug("queue token event published: {}", queueTokenExpireEvent);
		} catch (Exception e) {
			log.error("failed to send queue token event - exception: {}, ", e.toString());
		}
	}

	@Override
	@Async("taskExecutor")
	public void send(final String topic, final String payload) {
		try {
			final QueueTokenExpireEvent queueTokenExpireEvent = objectMapper.readValue(payload,
				QueueTokenExpireEvent.class);

			queueTokenExpireEventKafkaTemplate.send(topic, queueTokenExpireEvent);
			log.debug("queue token event published: {}", queueTokenExpireEvent);
		} catch (Exception e) {
			log.error("failed to send queue token event - exception: {}, ", e.toString());
		}
	}
}
