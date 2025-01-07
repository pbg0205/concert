package com.cooper.concert.domain.queues.service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WaitingQueueQueryRepository {
	Long findTokenPositionByTokenId(UUID tokenId, String status);

	boolean existsTokenByUserIdAndStatus(final Long userId, String status);

	Integer countsTokenByStatusAndExpiredAt(String status, LocalDateTime expiredAt);

	List<Long> findAccessibleIdsByStatusOrderByIdAsc(String status, Integer accessibleCount);
}
