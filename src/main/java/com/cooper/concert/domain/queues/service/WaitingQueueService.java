package com.cooper.concert.domain.queues.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueToken;
import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.service.dto.response.WaitingTokenPositionInfo;
import com.cooper.concert.domain.queues.service.errors.TokenErrorType;
import com.cooper.concert.domain.queues.service.errors.exception.TokenNotFoundException;
import com.cooper.concert.domain.queues.service.repository.WaitingQueueCommandRepository;
import com.cooper.concert.domain.queues.service.repository.WaitingQueueQueryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitingQueueService {

	private final WaitingQueueQueryRepository waitingQueueQueryRepository;
	private final WaitingQueueCommandRepository waitingQueueCommandRepository;

	public WaitingTokenPositionInfo getWaitingTokenPosition(final UUID tokenId) {
		final Long position = waitingQueueQueryRepository.findTokenPositionByTokenId(
			tokenId, QueueTokenStatus.WAITING.name());

		return new WaitingTokenPositionInfo(position);
	}

	public boolean existQueueToken(final Long userId) {
		return waitingQueueQueryRepository.existsTokenByUserIdAndStatus(userId, QueueTokenStatus.WAITING.name());
	}

	public boolean updateWaitingToCanceled(final Long userId) {
		final QueueToken queueToken = Optional.ofNullable(
				waitingQueueCommandRepository.findByUserIdAndStatus(userId, QueueTokenStatus.WAITING.name()))
			.orElseThrow(() -> new TokenNotFoundException(TokenErrorType.TOKEN_NOT_FOUND));

		return queueToken.updateCanceled();
	}
}
