package com.cooper.concert.interfaces.consumers.queueus;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.dto.event.QueueTokenExpireEvent;
import com.cooper.concert.interfaces.consumers.queueus.usecase.QueueTokenExpireUseCase;

@Component
@RequiredArgsConstructor
public class QueueTokenExpireTxEventListener {

	private final QueueTokenExpireUseCase queueTokenExpireUseCase;

	@Async("taskExecutor")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void consumeQueueTokenExpireEvent(final QueueTokenExpireEvent queueTokenExpireEvent) {
		queueTokenExpireUseCase.expireToken(queueTokenExpireEvent.userId());
	}
}
