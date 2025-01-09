package com.cooper.concert.interfaces.schedules.queues.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.annotations.Facade;
import com.cooper.concert.domain.queues.service.QueueTokenExpiredService;
import com.cooper.concert.domain.queues.service.WaitingQueueService;

@Facade
@RequiredArgsConstructor
@Transactional
public class TokenSchedulerUseCase {

	private final WaitingQueueService waitingQueueService;
	private final QueueTokenExpiredService queueTokenExpiredService;

	public Integer updateToProcessing(final LocalDateTime expiredAt) {
		return waitingQueueService.updateToProcessing(expiredAt);
	}

	public Integer expireToken(final LocalDateTime expiredAt) {
		final List<Long> expiredTokenUserIds = queueTokenExpiredService.updateToExpired(expiredAt);
		return expiredTokenUserIds.size();
	}
}
