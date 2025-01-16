package com.cooper.concert.interfaces.api.queues.usecase;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.concert.interfaces.components.annotations.Facade;
import com.cooper.concert.domain.queues.service.QueueTokenIssueService;
import com.cooper.concert.domain.queues.service.WaitingQueueService;
import com.cooper.concert.domain.queues.service.dto.QueueTokenIssueResult;
import com.cooper.concert.domain.queues.service.dto.response.QueueTokenIssueInfo;
import com.cooper.concert.domain.queues.service.dto.response.WaitingTokenPositionInfo;
import com.cooper.concert.domain.users.service.UserReadService;
import com.cooper.concert.domain.users.service.response.UserReadResult;

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
			waitingQueueService.updateWaitingToCanceled(userId);
		}

		final QueueTokenIssueInfo queueTokenIssueInfo = queueTokenIssueService.issueQueueToken(userId);
		final WaitingTokenPositionInfo waitingTokenPositionInfo =
			waitingQueueService.getWaitingTokenPosition(queueTokenIssueInfo.tokenId());

		return new QueueTokenIssueResult(queueTokenIssueInfo.tokenId(), waitingTokenPositionInfo.position());
	}
}
