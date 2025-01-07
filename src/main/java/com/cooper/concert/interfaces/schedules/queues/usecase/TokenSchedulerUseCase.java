package com.cooper.concert.interfaces.schedules.queues.usecase;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.annotations.Facade;
import com.cooper.concert.domain.queues.service.WaitingQueueService;

@Facade
@RequiredArgsConstructor
public class TokenSchedulerUseCase {

	private final WaitingQueueService waitingQueueService;

	public Integer updateToProcessing(final LocalDateTime expiredAt) {
		return waitingQueueService.updateToProcessing(expiredAt);
	}

}
