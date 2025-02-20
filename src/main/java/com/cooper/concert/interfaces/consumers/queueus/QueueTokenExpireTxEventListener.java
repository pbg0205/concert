package com.cooper.concert.interfaces.consumers.queueus;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;
import com.cooper.concert.domain.queues.service.publisher.QueueTokenExpireEventPublisher;

@Component
@RequiredArgsConstructor
public class QueueTokenExpireTxEventListener {

	private final QueueTokenExpireEventPublisher queueTokenExpireEventPublisher;

	@Async("taskExecutor")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void consumeQueueTokenExpireEvent(QueueTokenExpireEvent queueTokenExpireEvent) {
		queueTokenExpireEventPublisher.send(queueTokenExpireEvent);
	}
}
