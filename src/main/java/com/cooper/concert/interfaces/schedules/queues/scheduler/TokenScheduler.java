package com.cooper.concert.interfaces.schedules.queues.scheduler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.schedule.components.annotations.Scheduler;
import com.cooper.concert.support.logging.annotations.SchedulerLog;
import com.cooper.concert.interfaces.schedules.queues.usecase.TokenSchedulerUseCase;

@Scheduler
@RequiredArgsConstructor
@Transactional
public class TokenScheduler {

	@Value("${token.processing.valid.minutes}")
	private Integer expireValidMinutes;

	private final TokenSchedulerUseCase tokenSchedulerUseCase;

	@SchedulerLog
	@Scheduled(fixedRateString = "${queue.processing.rate}", timeUnit = TimeUnit.SECONDS)
	@SchedulerLock(name = "tokenActiveScheduler", lockAtLeastFor = "PT5S", lockAtMostFor = "PT8S")
	public Integer tokenActiveScheduler() {
		return tokenSchedulerUseCase.updateToProcessing(LocalDateTime.now().plusMinutes(expireValidMinutes));
	}

	@SchedulerLog
	@Scheduled(fixedRateString = "${token.expire.rate}", timeUnit = TimeUnit.SECONDS)
	@SchedulerLock(name = "tokenExpireScheduler", lockAtLeastFor = "PT5S", lockAtMostFor = "PT8S")
	public Integer tokenExpireScheduler() {
		return tokenSchedulerUseCase.expireToken(LocalDateTime.now());
	}

}
