package com.cooper.concert.domain.queues.service.repository;

import com.cooper.concert.domain.queues.models.QueueToken;

public interface QueueTokenCommandRepository {
	QueueToken save(QueueToken queueToken);
}
