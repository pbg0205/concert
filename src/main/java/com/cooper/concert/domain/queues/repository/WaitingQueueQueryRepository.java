package com.cooper.concert.domain.queues.repository;

import java.util.UUID;

public interface WaitingQueueQueryRepository {
	Long findTokenPositionByTokenId(UUID tokenId, String status);

	boolean existsTokenByUserIdAndStatus(final Long userId, String status);
}