package com.cooper.concert.interfaces.consumers.queueus.usecase;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.QueueTokenExpiredService;
import com.cooper.concert.interfaces.components.annotations.Facade;

@Facade
@RequiredArgsConstructor
public class QueueTokenExpireUseCase {

	private final QueueTokenExpiredService queueTokenExpiredService;

	public void expireToken(final Long userId) {
		queueTokenExpiredService.expireCompleteToken(userId);
	}
}
