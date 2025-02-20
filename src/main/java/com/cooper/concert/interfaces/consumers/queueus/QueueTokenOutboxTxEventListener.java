package com.cooper.concert.interfaces.consumers.queueus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.QueueTokenOutboxService;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenOutboxEvent;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenOutboxFailEvent;
import com.cooper.concert.domain.queues.service.dto.event.QueueTokenOutboxSuccessEvent;

@Component
@RequiredArgsConstructor
public class QueueTokenOutboxTxEventListener {

	@Value("${token.expire.topic}")
	private String queueTokenExpireTopic;

	private final QueueTokenOutboxService queueTokenOutboxService;
	private final ObjectMapper objectMapper;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void consumeQueueTokenExpire(QueueTokenOutboxEvent queueTokenOutboxEvent)
		throws JsonProcessingException {
		final String jsonPayload = objectMapper.writeValueAsString(queueTokenOutboxEvent.payload());
		queueTokenOutboxService.saveQueueToken(queueTokenExpireTopic, jsonPayload, queueTokenOutboxEvent.paymentId());
	}

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void consumeQueueTokenExpireSuccess(QueueTokenOutboxSuccessEvent queueTokenOutboxSuccessEvent) {
		queueTokenOutboxService.updateSuccess(queueTokenOutboxSuccessEvent.paymentId());
	}

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void consumeQueueTokenExpireFail(QueueTokenOutboxFailEvent queueTokenOutboxFailEvent) {
		queueTokenOutboxService.updateFail(queueTokenOutboxFailEvent.paymentId());
	}
}
