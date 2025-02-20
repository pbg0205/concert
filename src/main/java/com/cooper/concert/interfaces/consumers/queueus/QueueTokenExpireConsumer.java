package com.cooper.concert.interfaces.consumers.queueus;

import java.util.concurrent.CompletableFuture;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cooper.concert.domain.queues.service.QueueTokenExpiredService;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenOutboxFailEvent;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenOutboxSuccessEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueTokenExpireConsumer {

	private final QueueTokenExpiredService queueTokenExpiredService;
	private final ApplicationEventPublisher applicationEventPublisher;

	@RetryableTopic(attempts = "1", timeout = "3000", dltStrategy = DltStrategy.FAIL_ON_ERROR)
	@KafkaListener(topics = "${token.expire.topic}", containerFactory = "queueTokenExpireListenerContainerFactory")
	public CompletableFuture<Void> consumeQueueTokenExpire(@Payload QueueTokenExpireEvent queueTokenExpireEvent) {
		return CompletableFuture.runAsync(() -> {
			queueTokenExpiredService.expireCompleteToken(queueTokenExpireEvent.userId());

			applicationEventPublisher.publishEvent(
				new QueueTokenOutboxSuccessEvent(queueTokenExpireEvent.paymentAltId()));

			log.debug("queue token expire successfully! - userid: {}", queueTokenExpireEvent.userId());
		}).exceptionally(exception -> {
			log.error("queue token expire fail! - userid: {} exception : {}",
				queueTokenExpireEvent.userId(), exception.toString());
			return null;
		});
	}

	@KafkaListener(topics = "${token.expire.dlt}", containerFactory = "queueTokenExpireListenerContainerFactory")
	public void consumeQueueTokenExpireDlt(@Payload QueueTokenExpireEvent queueTokenExpireEvent) {
		applicationEventPublisher.publishEvent(
			new QueueTokenOutboxFailEvent(queueTokenExpireEvent.paymentAltId()));

		log.debug("queue token expire dlt! - userid: {}", queueTokenExpireEvent.userId());
	}
}
