package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.Collections;
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

	public List<Long> updateToExpired(final LocalDateTime expiredAt) {
		final List<QueueToken> expiringQueueTokens =
			queueTokenCommandRepository.findAllByStatus(QueueTokenStatus.PROCESSING.name());

		List<Long> expiredTokenUserIds = Collections.emptyList();
		if (expiringQueueTokens.isEmpty()) {
			return expiredTokenUserIds;
		}

		for (QueueToken expiringQueueToken : expiringQueueTokens) {
			if (expiringQueueToken.expire(expiredAt)) {
				expiredTokenUserIds.add(expiringQueueToken.getUserId());
			}
		}

		return expiredTokenUserIds;
	}
}
