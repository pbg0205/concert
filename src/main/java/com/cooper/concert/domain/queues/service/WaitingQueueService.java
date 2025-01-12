package com.cooper.concert.domain.queues.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${queue.processing.capacity}")
	private Integer processingTokenCapacity;

	@Value("${token.processing.valid.minutes}")
	private Integer processingTokenMinutes;

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

	public Integer updateToProcessing(final LocalDateTime expiredAt) {
		final Integer processingTokenCount =
			waitingQueueQueryRepository.countsTokenByStatusAndExpiredAt(QueueTokenStatus.PROCESSING.name(), expiredAt);

		final int accessibleCount = processingTokenCapacity - processingTokenCount;
		if (accessibleCount <= 0) {
			return 0;
		}

		final List<Long> accessibleQueueTokenIds = waitingQueueQueryRepository.findAccessibleIdsByStatusOrderByIdAsc(
			QueueTokenStatus.WAITING.name(),
			accessibleCount);

		return waitingQueueCommandRepository.findAllByIds(accessibleQueueTokenIds)
			.stream()
			.mapToInt(queueToken -> queueToken.updateProcessing(LocalDateTime.now(), processingTokenMinutes) ? 1 : 0)
			.sum();
	}

}
