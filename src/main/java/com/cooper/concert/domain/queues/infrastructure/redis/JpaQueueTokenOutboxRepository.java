package com.cooper.concert.domain.queues.infrastructure.redis;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.concert.domain.queues.models.QueueTokenExpireOutbox;

public interface JpaQueueTokenOutboxRepository extends JpaRepository<QueueTokenExpireOutbox, Long> {
	QueueTokenExpireOutbox findByPaymentId(UUID paymentId);
}
