package com.cooper.concert.interfaces.api.queues.usecase;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.domain.queues.service.QueueTokenIssueService;
import com.cooper.concert.domain.queues.service.WaitingQueueService;
import com.cooper.concert.domain.queues.service.dto.QueueTokenIssueResult;
import com.cooper.concert.domain.users.service.UserReadService;
import com.cooper.concert.domain.users.service.response.UserReadResult;
import com.cooper.concert.interfaces.components.annotations.Facade;

@Facade
@RequiredArgsConstructor
@Transactional
public class QueueTokenIssueUseCase {

	private final QueueTokenIssueService queueTokenIssueService;
	private final WaitingQueueService waitingQueueService;
	private final UserReadService userReadService;

	public QueueTokenIssueResult issueQueueToken(final UUID userAltId) {
		final UserReadResult userReadResult = userReadService.findByAltId(userAltId);
		final Long userId = userReadResult.userId();

		if (waitingQueueService.existQueueToken(userId)) {
			waitingQueueService.removeFromWaitingQueue(userId);
		}

		final String token = queueTokenIssueService.issueQueueToken(userId);
		final Long position = waitingQueueService.enqueueUserId(userId);

		return new QueueTokenIssueResult(token, position);
	}
}
