package com.cooper.concert.domain.queues.infrastructure.rdb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.concert.domain.queues.models.QueueToken;

public interface JpaQueueTokenRepository extends JpaRepository<QueueToken, Long> {
}
