package com.cooper.concert.domain.queues.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.models.QueueTokenStatus;
import com.cooper.concert.domain.queues.repository.WaitingQueueQueryRepository;
import com.cooper.concert.domain.queues.service.dto.response.WaitingTokenPositionInfo;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitingQueueService {

	private final WaitingQueueQueryRepository waitingQueueQueryRepository;

	public WaitingTokenPositionInfo getWaitingTokenPosition(final UUID tokenId) {
		final Long position = waitingQueueQueryRepository.findTokenPositionByTokenId(
			tokenId, QueueTokenStatus.WAITING.name());

		return new WaitingTokenPositionInfo(position);
	}
}
