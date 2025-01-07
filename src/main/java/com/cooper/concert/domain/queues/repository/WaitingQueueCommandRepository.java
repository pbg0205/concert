package com.cooper.concert.domain.queues.repository;

import com.cooper.concert.domain.queues.models.QueueToken;

public interface WaitingQueueCommandRepository {
	QueueToken findByUserIdAndStatus(Long userId, String status);
}
