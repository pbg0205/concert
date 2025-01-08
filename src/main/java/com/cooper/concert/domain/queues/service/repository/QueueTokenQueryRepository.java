package com.cooper.concert.domain.queues.service.repository;

import java.util.UUID;

import com.cooper.concert.domain.queues.models.QueueToken;

public interface QueueTokenQueryRepository {
	QueueToken findByTokenId(UUID tokenId);
}
