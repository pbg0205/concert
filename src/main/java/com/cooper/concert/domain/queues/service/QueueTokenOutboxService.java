package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.payments.service.errors.PaymentErrorType;
import com.cooper.concert.domain.payments.service.errors.exception.PaymentOutboxNotFoundException;
import com.cooper.concert.domain.queues.models.QueueTokenExpireOutbox;
import com.cooper.concert.domain.queues.models.QueueTokenOutboxStatus;
import com.cooper.concert.domain.queues.service.publisher.QueueTokenExpireEventPublisher;
import com.cooper.concert.domain.queues.service.repository.QueueTokenExpireOutboxRepository;

@Service
@RequiredArgsConstructor
public class QueueTokenOutboxService {

	private final QueueTokenExpireOutboxRepository queueTokenExpireOutboxRepository;
	private final QueueTokenExpireEventPublisher queueTokenExpireEventPublisher;

	@Transactional
	public void saveQueueToken(final String topic, final String payload, final UUID paymentId) {
		final QueueTokenExpireOutbox queueTokenExpireOutbox =
			QueueTokenExpireOutbox.createInitStatus(topic, payload, paymentId);
		queueTokenExpireOutboxRepository.save(queueTokenExpireOutbox);
	}

	@Transactional
	public void updateSuccess(final UUID paymentId) {
		final QueueTokenExpireOutbox queueTokenExpireOutbox =
			Optional.ofNullable(queueTokenExpireOutboxRepository.findByPaymentId(paymentId))
				.orElseThrow(() -> new PaymentOutboxNotFoundException(PaymentErrorType.PAYMENT_OUTBOX_NOT_FOUND));

		queueTokenExpireOutbox.updateSuccess();
	}


	@Transactional
	public void updateFail(final UUID paymentId) {
		final QueueTokenExpireOutbox queueTokenExpireOutbox =
			Optional.ofNullable(queueTokenExpireOutboxRepository.findByPaymentId(paymentId))
			.orElseThrow(() -> new PaymentOutboxNotFoundException(PaymentErrorType.PAYMENT_OUTBOX_NOT_FOUND));

		queueTokenExpireOutbox.updateFail();
	}

	public Integer retryExpiredTokens(final LocalDateTime retryAt) {
		final List<QueueTokenExpireOutbox> retryCandidates = queueTokenExpireOutboxRepository.findAllByRetryAtAndType(
			retryAt, QueueTokenOutboxStatus.SEND_FAIL);

		for (QueueTokenExpireOutbox queueTokenExpireOutbox : retryCandidates) {
			queueTokenExpireEventPublisher.send(queueTokenExpireOutbox.getTopic(), queueTokenExpireOutbox.getPayload());
		}

		return retryCandidates.size();
	}
}
