package com.cooper.concert.domain.queues.service.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActiveTokenRepository {
	Integer addActiveQueueTokens(List<Long> userIds, LocalDateTime expiredAt);
	Integer countActiveTokens();
	boolean existsByUserId(Long userId);
	boolean removeActiveToken(Long userId);
	List<Long> removeAllLteExpiredAt(LocalDateTime expiredAt);
}
