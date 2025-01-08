package com.cooper.concert.domain.queues.service.repository;

import java.util.List;

import com.cooper.concert.domain.queues.models.QueueToken;

public interface QueueTokenCommandRepository {
	QueueToken save(QueueToken queueToken);
	List<QueueToken> findAllByStatus(String status);
}
