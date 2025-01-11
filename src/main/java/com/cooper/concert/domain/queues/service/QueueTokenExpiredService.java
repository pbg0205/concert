package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.QueueTokenCommandRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QueueTokenExpiredService {

	private final QueueTokenCommandRepository queueTokenCommandRepository;

	public List<Long> expireExpiredTokens(final LocalDateTime expiredAt) {
		final List<QueueToken> expiringQueueTokens =
			queueTokenCommandRepository.findAllByStatus(QueueTokenStatus.PROCESSING.name());

		List<Long> expiredTokenUserIds = new ArrayList<>();
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

	public boolean expireCompleteToken(final UUID tokenId) {
		final QueueToken queueToken = Optional.ofNullable(queueTokenCommandRepository.findByTokenId(tokenId))
			.orElseThrow(() -> new TokenNotFoundException(TokenErrorType.TOKEN_NOT_FOUND));

		return queueToken.complete(LocalDateTime.now());
	}
}
