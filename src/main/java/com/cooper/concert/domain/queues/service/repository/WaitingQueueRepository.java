package com.cooper.concert.domain.queues.service.repository;

import java.util.List;

public interface WaitingQueueRepository {
	Long enqueueUserId(Long userId);
	boolean existsUserId(Long userId);
	boolean removeUserId(Long userId);
	List<Long> dequeueUserIds(Integer availableActiveTokenCount);
}
