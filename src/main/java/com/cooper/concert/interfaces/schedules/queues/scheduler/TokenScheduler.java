package com.cooper.concert.interfaces.schedules.queues.scheduler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.common.annotations.Scheduler;
import com.cooper.concert.interfaces.schedules.queues.usecase.TokenSchedulerUseCase;

@Scheduler
@RequiredArgsConstructor
@Transactional
public class TokenScheduler {

	private final TokenSchedulerUseCase tokenSchedulerUseCase;

	@Scheduled(fixedRateString = "${queue.processing.rate.minutes}", timeUnit = TimeUnit.MINUTES)
	public Integer updateTokenToProcessing() {
		return tokenSchedulerUseCase.updateToProcessing(LocalDateTime.now());
	}

	@Scheduled(fixedRateString = "${token.expire.rate.minutes}", timeUnit = TimeUnit.MINUTES)
	public Integer updateTokenToExpired() {
		return tokenSchedulerUseCase.expireToken(LocalDateTime.now());
	}

}
