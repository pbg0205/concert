package com.cooper.concert.domain.queues.service.repository;

import java.util.List;

import com.cooper.concert.domain.queues.models.QueueToken;

public interface WaitingQueueCommandRepository {
	QueueToken findByUserIdAndStatus(Long userId, String status);
	List<QueueToken> findAllByIds(List<Long> accessibleQueueTokenIds);
}
