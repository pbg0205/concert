package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.QueueTokenQueryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueueTokenValidationService {

	private final QueueTokenQueryRepository queueTokenQueryRepository;

	public boolean validateQueueTokenProcessing(final UUID tokenId, final LocalDateTime expiredAt) {
		final QueueToken queueToken = Optional.ofNullable(queueTokenQueryRepository.findByTokenId(tokenId))
			.orElseThrow(() -> new TokenNotFoundException(TokenErrorType.TOKEN_NOT_FOUND));

		return queueToken.isValidProcessingToken(expiredAt);
	}
}
