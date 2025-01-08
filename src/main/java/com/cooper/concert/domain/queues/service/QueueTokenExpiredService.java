package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.service.repository.QueueTokenCommandRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QueueTokenExpiredService {

	private final QueueTokenCommandRepository queueTokenCommandRepository;

	public Integer updateToExpired(final LocalDateTime expiredAt) {
		final List<QueueToken> expiringQueueTokens =
			queueTokenCommandRepository.findAllByStatus(QueueTokenStatus.PROCESSING.name());

		if (expiringQueueTokens.isEmpty()) {
			return 0;
		}

		int successCount = 0;
		for (QueueToken expiringQueueToken : expiringQueueTokens) {
			successCount += expiringQueueToken.expire(expiredAt) ? 1 : 0;
		}

		return successCount;
	}
}
