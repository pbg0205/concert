package com.cooper.concert.domain.queues.service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.cooper.concert.domain.queues.models.QueueTokenExpireOutbox;
import com.cooper.concert.domain.queues.models.QueueTokenOutboxStatus;

public interface QueueTokenExpireOutboxRepository {
	QueueTokenExpireOutbox save(QueueTokenExpireOutbox queueTokenExpireOutbox);
	QueueTokenExpireOutbox findByPaymentId(UUID paymentId);
	List<QueueTokenExpireOutbox> findAllByRetryAtAndType(LocalDateTime retryAt, QueueTokenOutboxStatus status);
}
